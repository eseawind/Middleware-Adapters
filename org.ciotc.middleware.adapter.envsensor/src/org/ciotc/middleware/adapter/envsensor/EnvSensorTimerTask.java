/**
 *
 * EnvSensorTimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.envsensor;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.ciotc.middleware.notification.MessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;

/**
 * @author ZhangMin.name
 *
 */
public class EnvSensorTimerTask extends TimerTask{
	private static final Log logger = LogFactory.getLog(EnvSensorTimerTask.class);
    private static AbstractSensor sensor;
    private static String wsdl;
    
    public EnvSensorTimerTask(AbstractSensor sensor,String wsdl){
    	this.sensor = sensor;
    	this.wsdl = wsdl;
    }
    
    public EnvSensorTimerTask(String wsdl){
    	this.wsdl = wsdl;
    }
    
	@Override
	public void run() {
		System.out.println(" EnvSensorTimerTask started ");
		try{
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
			Client client = dcf.createClient(wsdl);
			Object[] result = null;
			int i = 2;
			result = client.invoke("GetData", i);
			 
			if(result != null && !"".equals(result)){
				//TODO remove after test
				//System.out.println("Result: " + result[0].toString());
				//FileOutputStream fos = new FileOutputStream(new File("C:/result.txt"));
				//fos.write(result[0].toString().getBytes());
				MessageDto msgDto = new MessageDto();
				msgDto.setReaderID(sensor.getID());
				msgDto.setSequence("0");
				msgDto.setXmlData(result[0].toString());
				sensor.send(msgDto);
			}
		}catch(Exception e){
			logger.warn("A error occured while retrieving sensor data.While recovered soon");
		}
		
		
	}
}
