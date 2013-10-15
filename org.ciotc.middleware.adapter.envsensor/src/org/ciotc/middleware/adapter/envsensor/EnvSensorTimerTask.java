/**
 *
 * EnvSensorTimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.envsensor;

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
		System.out.println(" EnvSensorTimerTask start ");
		
		JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
	    Client client = dcf.createClient(wsdl);
		Object[] result = null;
		try{
			int i = 2;
			result = client.invoke("GetData2", i);
			 
			if(result != null && !"".equals(result)){
				System.out.println("Result: " + result[0].toString());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		MessageDto msgDto = new MessageDto();
		msgDto.setReaderID(sensor.getID());
		msgDto.setSequence("0");
		try {
			//msgDto.setXmlData(Convertor.objToXml(pm25Value, PM25Value.class));
			//debug
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//sensor.send(msgDto);
	}
}
