/**
 *
 * GwMessage.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * name.zhangmin.java
 *
 */
package org.ciotc.middleware.adapter.positioning.pojo;

import java.text.SimpleDateFormat;

/**
 * 
 * 网关数据包
 * @author ZhangMin.name
 *
 */
public class GwMessage {
	public static byte MT_EMPTY = 1;
	public static byte START = 2;
	public static byte END = 3;
	public static byte MT_RESERVE04 = 4;
	public static byte MT_LOCATION = 5;
	public static byte MT_CARDINFO = 6;
	public static byte MT_READERINFO = 7;
	public static byte MT_ACTIVATORINFO = 8;
	public static byte MT_CUSTOM = 11;
	public static byte MT_UNKNOWN = -1;
	
	protected byte[] data;
	SimpleDateFormat dateFormate = new SimpleDateFormat("yy,MM,dd,HH,mm,SSSS");
	
	protected GwMessage(){ }
	
	public GwMessage(byte[] buf ,int length){
		data = new byte[length];
		for(int i = 0; i < length; i++){
			data[i] = buf[i];
		}
	}
	
	public byte[] getBytes(){
		return data;
	}
	
	//TODO implement function mergeTime()
	public void mergeTime(){
		
	}
	public byte getType(){
		if(2 == data.length ){
			return MT_EMPTY;
		}
		byte cmd = data[1];
		if((cmd >= MT_LOCATION) && (cmd <= MT_CUSTOM)){
			return cmd;
		}
		return MT_UNKNOWN;
	}
	
	public boolean isLocation(){
		byte t = getType();
		return (t == MT_LOCATION) || (t == MT_CARDINFO);
	}
}
