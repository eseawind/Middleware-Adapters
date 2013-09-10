/**
 *
 * ProtocolHandler.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import org.ciotc.middleware.adapter.positioning.pojo.GwMessage;
import org.ciotc.middleware.notification.StaffMessageDto;



/**
 * @author ZhangMin.name
 *
 */
public class ProtocolHelper {
	
	public GwMessage makePacket(){
		byte[] data = new byte[21];
		//命令字 0x05
		data[0] = 5;
		int p = 1;
		for(int i = 0;i < 3;i ++){
			byte[] id = generateID();
			for(int j = 0;j < 4;j ++){
				data[p] = id[j];
				p++;
			}
		}
		byte[] time = makeTime();
		for(int i = 0;i < time.length; i ++){
			data[p] = time[i];
			p++;
		}
		//debug 
		System.out.println("beforeEscape:" + dump(data));
		//转义包数据
		byte[] temp = escape(data);
		//添加包头，包尾
		byte[] pData = new byte[temp.length + 2];
		pData[0] = 2;
		System.arraycopy(temp, 0, pData, 1, temp.length);
		pData[pData.length - 1] = 3;
		return new GwMessage(pData,pData.length);
	}
	
	public StaffMessageDto parsePacket(byte[] oData){
		//去掉包头包尾
		byte[] tmp = new byte[oData.length - 2];
		System.arraycopy(oData, 1, tmp, 0, tmp.length);
		byte[] temp = unEscape(tmp);
		StaffMessageDto smd = new StaffMessageDto();
		if(temp[0] == GwMessage.MT_LOCATION){
			byte[] id = new byte[4];
			byte[] time = new byte[8];
			int j = 0;
			int t = 0;
			//读取定位器卡号，天线号，基站号
			for(int i = 1;i < temp.length;i ++){
				if(i < 13){
					id[j % 4] = temp[i];
					if(j == 3){
						smd.setCardID(bytesToInteger(id).toString());
					}else if(j == 7){
						smd.setAntennID(bytesToInteger(id).toString());
					}else if(j == 11){
						smd.setBaseID(bytesToInteger(id).toString());
					}else{ }
					j ++;
				}else{
					time[t ++] = temp[i];
				}
			}
			smd.setTime(bytesToTimeString(time));
		}
		return smd;
	}
	public Integer bytesToInteger(byte[] b){
		
		byte[] a = new byte[4];
		int i = a.length - 1,j = b.length - 1;
		for (; i >= 0 ; i--,j--) {//从b的尾部(即int值的低位)开始copy数据
			if(j >= 0)
				a[i] = b[j];
			else
				a[i] = 0;//如果b.length不足4,则将高位补0
		}
		//&0xff将byte值无差异转成int,避免Java自动类型提升后,会保留高位的符号位
		int v0 = (a[0] & 0xff) << 24;
		int v1 = (a[1] & 0xff) << 16;
		int v2 = (a[2] & 0xff) << 8;
		int v3 = (a[3] & 0xff) ;
		int ret = v0 + v1 + v2 + v3;
		
		//debug 
	    System.out.println("bytesToInteger:" + ret);
	    System.out.println("bytesToInteger:" + dump(b));
		return ret;
	}
	public String bytesToTimeString(byte[] b){
		StringBuilder sb = new StringBuilder();
		int year = (int)b[0];
		sb.append(year+2000);
		sb.append("/").append((int)b[1]).append("/").append((int)b[2])
			.append(" ").append((int)b[3]).append(":").append((int)b[4])
			.append(":").append((int)b[5]);
		byte[] millss = new byte[]{b[7],b[6]};
		int mills = this.bytesToInteger(millss);
		sb.append(" ").append(mills);
		//debug 
		System.out.println("Timebytes:" + dump(b));
		System.out.println("bytesToTimeString:" + sb.toString());
		return sb.toString();
	}
	public byte[] generateID(){
		byte[] id = new byte[4];
		Random rand = new Random();
		int idd = rand.nextInt(Integer.MAX_VALUE);
		//id = ByteBuffer.allocate(4).putInt(idd).array();
		id[0] = (byte)(idd >>> 24);
		id[1] = (byte)(idd >>> 16);
	    id[2] = (byte)(idd >>> 8);
		id[3] = (byte)idd ;
		//debug
		System.out.println("intId:" + idd + "\n" + "ids:" + dump(id));
		return id;
	}
	/**
	 * 对数据包内容进行转义处理。除了没有内容的空包之外，包内容中使用转义字符0x04表示包头包尾
	 * 占用的特殊值，使用0x0404表示0x04，使用0x0405表示0x02，使用0x0406表示0x03。
	 * @param oData 转义前的数据
	 * @return
	 */
	public byte[] escape(byte[] oData){
		byte[] nData = new byte[oData.length];
		int i = 0;
		for(byte b : oData){
			if( 2 == b){
				byte[] tmp = new byte[i+2];
				System.arraycopy(nData, 0, tmp, 0, i);
				tmp[i] = 4;
				tmp[i + 1] = 5;
				nData = new byte[nData.length + 1];
				System.arraycopy(tmp, 0, nData, 0, tmp.length);
				i += 2;
			}else if(3 == b){
				byte[] tmp = new byte[i+2];
				System.arraycopy(nData, 0, tmp, 0, i);
				tmp[i] = 4;
				tmp[i + 1] = 6;
				nData = new byte[nData.length + 1];
				System.arraycopy(tmp, 0, nData, 0, tmp.length);
				i += 2;
			}else if(4 == b){
				byte[] tmp = new byte[i+2];
				System.arraycopy(nData, 0, tmp, 0, i);
				tmp[i] = 4;
				tmp[i + 1] = 4;
				nData = new byte[nData.length + 1];
				System.arraycopy(tmp, 0, nData, 0, tmp.length);
				i += 2;
			}else{
				nData[i] = b;
				i++;
			}
		}
		//debug
		System.out.println("escape bytes:" + dump(nData));
		return nData;
	}
	
	public byte[] unEscape(byte[] oData){
		byte[] nData = new byte[oData.length];
		int i = 0;
		byte b;
		byte next;
		for(int j = 0;j < oData.length;j ++){
			b = oData[j];
			if(4 == b){
				if(j  == oData.length - 1){
					continue;
				}
				next = oData[j + 1];
				if(4 == next){
					nData[i] = oData[j];
					j ++;
				}else if(5 == next){
					nData[i] = 2;
					j ++;
				}else if(6 == next){
					nData[i] = 3;
					j ++;
				}else{
					nData[i] = oData[j];
				}
			}else {
				nData[i] = oData[j];
			}
			i ++;
		}
		byte[] ret = new byte[i];
		System.arraycopy(nData, 0, ret, 0, ret.length);
		//debug
		System.out.println("unEscapebyte:" + dump(ret));
		return ret;
	}
	/**
	 * 生成八字节时间字节：年月日时分秒（各1byte）
	 * 毫秒(2 bytes)年 = 一字节值+2000
	 * @return
	 */
	public byte[] makeTime(){
		byte[] tBytes = new byte[8];
		SimpleDateFormat dateFormat = new SimpleDateFormat("yy,MM,dd,HH,mm,ss,SSSS");
		String strDate = dateFormat.format(new Date());
		//debug
		System.out.println("makeTime:" +strDate);
		String[] dates = strDate.split(",");
		int i = 0;
		int v;
		for(String d : dates){
			v = Integer.parseInt(d);
			if(v > 255){
				//先取后一个字节
				tBytes[i] = ((byte) ( v & 0xFF));
				//再取前一个字节
				v >>= 8;
				i++;
			}
			tBytes[i] = (byte) v;
			i++;
		}
		//debug
		System.out.println("makeTime byte:" + dump(tBytes));
		return tBytes;
	}
	public String dump(byte[] buf){
		StringBuilder hexDisplay = new StringBuilder();
		int i = 0;
		while(i < buf.length){
			byte b = buf[i++];
			hexDisplay.append(toHex(b)).append(" ");
		}
		return hexDisplay.toString();
	}
	public boolean isPrint(byte b){
		return (b > 32) && (b < 126);
	}
	public String toHex(byte b){
		return String.format("%02X", new Object[]{Byte.valueOf(b)});
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProtocolHelper ph = new ProtocolHelper();
		GwMessage gm = ph.makePacket();
		byte[] data = gm.getBytes();
		//System.out.print(" "+ ph.dump(data));
		ph.parsePacket(data);
		
	}

}
