/**
 * PM25TimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 * 2013��8��20��
 */
package org.ciotc.middleware.adapter.pm25;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.ciotc.middleware.adapter.pm25.pojo.PM25Value;
import org.ciotc.middleware.adapter.pm25.util.Convertor;
import org.ciotc.middleware.notification.MessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;
/**
 * @author ZhangMin.name
 *
 */
public class PM25TimerTask extends TimerTask{
	private static final Log logger = LogFactory.getLog(PM25TimerTask.class);
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
		PM25Value pm25Value = new PM25Value();
		try{
			result = client.invoke("getPM25Value", "");
			if(result != null && !"".equals(result)){
				pm25 = Integer.parseInt(result[0].toString());
			}
		}catch(Exception e){
			logger.warn("Retrieve PM2.5 value failed,it will remain unchanged.");
		}
		
		if(pm25 < this.minValue){
			pm25 = this.minValue;
		}else if(pm25 > this.maxValue){
			pm25 = this.maxValue;
		}else{
		}
		pm25Value.setPm25Value(pm25.toString());
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		pm25Value.setTimestamp(dateFormat.format(new Date()));
		MessageDto msgDto = new MessageDto();
		msgDto.setReaderID(sensor.getID());
		msgDto.setSequence("0");
		try {
			msgDto.setXmlData(Convertor.objToXml(pm25Value, PM25Value.class));
			logger.info("PM25 value after processing: " 
			+ Convertor.objToXml(pm25Value, PM25Value.class));
			//Remove after test
			System.out.println("PM25: " + Convertor.objToXml(pm25Value, PM25Value.class));
		} catch (Exception e) {
			logger.warn("PM2.5 Value to xml failed,will recovered when next thread started.");
		}
		
		sensor.send(msgDto);
	}

}
