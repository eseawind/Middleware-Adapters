/**
 * PM25TimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 * 2013��8��20��
 */
package org.ciotc.middleware.adapter.pm25;

import java.util.Date;
import java.util.TimerTask;

import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.ciotc.middleware.notification.MessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;
/**
 * @author ZhangMin.name
 *
 */
public class PM25TimerTask extends TimerTask{
    private static AbstractSensor sensor;
    private static String wsdl;
    
    private Integer maxValue = null;
    private Integer minValue = null;
    
    public PM25TimerTask(AbstractSensor sensor,String wsdl){
    	this.sensor = sensor;
    	this.wsdl = wsdl;
    }
    
    public PM25TimerTask(String wsdl){
    	this.wsdl = wsdl;
    }
    
    public PM25TimerTask(AbstractSensor sensor,String wsdl,Integer maxValue,Integer minValue){
    	this.sensor = sensor;
    	this.wsdl = wsdl;
    	this.maxValue = maxValue;
    	this.minValue = minValue;
    }
	@Override
	public void run() {
		System.out.println(" PM25TimerTask start ");
		
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
	    Client client = dcf.createClient(wsdl);
		Object[] result = null;
		Integer pm25 = null;
		
		try{
			result = client.invoke("getPM25Value", "");
			if(result != null && !"".equals(result)){
				pm25 = Integer.parseInt(result[0].toString());
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		//For Debug
		System.out.println(new Date()+" getPM25Value response: " + pm25);
		if(pm25 < this.minValue){
			pm25 = this.minValue;
		}else if(pm25 > this.maxValue){
			pm25 = this.maxValue;
		}else{
		}
		//For Debug
		System.out.println("pm25 value after processing :" + pm25);
		MessageDto msgDto = new MessageDto();
		msgDto.setReaderID(sensor.getID());
		msgDto.setSequence("0");
		//TODO change pm25 value to xml format
		msgDto.setXmlData(pm25.toString());
		
		sensor.send(msgDto);
	}

}
