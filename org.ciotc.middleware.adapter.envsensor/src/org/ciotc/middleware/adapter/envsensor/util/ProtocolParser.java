/**
 *
 * ProtocolHandler.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.envsensor.util;

import org.ciotc.middleware.adapter.envsensor.pojo.Sensor11Data;
import org.ciotc.middleware.adapter.envsensor.pojo.Sensor12Data;

/**
 * @author ZhangMin.name
 *
 */
public class ProtocolParser {

	public static Object parse(String rcvData,int type){
		if(type == 11){
			Sensor11Data retVal = new Sensor11Data();
			double liv = 0.0;
			double ndata = 0.0;
			Integer uvv = null;
			Integer li = Integer.parseInt(rcvData.substring(28,32), 16);
			Integer uv = Integer.parseInt(rcvData.substring(32,36), 16);
			liv = li / 1.2 * 3.28;
			ndata = uv * 3.3 * 1000 / 4096 / 8;
			if(ndata < 50){
				uvv = 0;
			}else if(ndata > 50 && ndata <= 227){
				uvv = 1;
			}else if(ndata > 227 && ndata <= 318){
				uvv = 2;
			}else if(ndata > 318 && ndata <= 408){
				uvv = 3;
			}else if(ndata > 408 && ndata <= 503){
				uvv = 4;
			}else if(ndata > 503 && ndata <= 606){
				uvv = 5;
			}else if(ndata > 606 && ndata <= 696){
				uvv = 6;
			}else if(ndata > 696 && ndata <= 795){
				uvv = 7;
			}else if(ndata > 795 && ndata <= 881){
				uvv = 8;
			}else if(ndata > 881 && ndata <= 976){
				uvv = 9;
			}else if(ndata > 976 && ndata <= 1079){
				uvv = 10;
			}else if(ndata > 1079){
				uvv = 11;
			}else{ }
			retVal.setUvIndex(uvv.toString());
			retVal.setLightIntesity(new Double(liv).toString());
			return retVal;
		}else if(type == 12){
			Sensor12Data retVal = new Sensor12Data();
			double cav = Integer.parseInt(rcvData.substring(28,32), 16);
			double tv = 0.0;
			double hv = 0.0;
			double t = Integer.parseInt(rcvData.substring(32,36), 16);
			double h = Integer.parseInt(rcvData.substring(36,40), 16);
			tv = t * 0.01 - 39.66;
			hv = -4.0 + 0.0405 * h - (2.8E-6) * h * h;
			retVal.setCarbonDioxide(new Double(cav).toString());
			retVal.setHumidity(new Double(hv).toString());
			retVal.setTemperature(new Double(tv).toString());
			return retVal;
		}else{}
		return null;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
	}

}
