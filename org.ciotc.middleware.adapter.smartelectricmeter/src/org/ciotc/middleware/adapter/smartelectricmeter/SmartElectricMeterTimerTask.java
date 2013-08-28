/**
 *
 * SmartElectricMeterTimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 *
 */
package org.ciotc.middleware.adapter.smartelectricmeter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.ciotc.middleware.adapter.smartelectricmeter.bean.SmartElectricMeter;
import org.ciotc.middleware.adapter.smartelectricmeter.bean.SmartElectricMeterList;
import org.ciotc.middleware.adapter.smartelectricmeter.util.SmartMeterHelper;
import org.ciotc.middleware.notification.MessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;

/**
 * @author ZhangMin.name
 *
 */
public class SmartElectricMeterTimerTask extends TimerTask{
	private static AbstractSensor sensor;
	private static String url;
	public SmartElectricMeterTimerTask(AbstractSensor sensor,String url){
		this.sensor = sensor;
		this.url = url;
	}
	@Override
	public void run() {
		System.out.println(" SmartElectricMeterTimerTask start ");
//		URL Url = null;
//		HttpURLConnection conn = null;
//		String line = null;
//		StringBuffer sb = new StringBuffer();
//		BufferedReader br = null;
//		try {
//			Url = new URL(this.url);
//			conn = (HttpURLConnection)Url.openConnection();
//			conn.connect();
//			br  = new BufferedReader(
//					 			new InputStreamReader(
//					 				conn.getInputStream()));
//			while((line = br.readLine()) != null){
//				sb.append(line);
//			}
//			br.close();
//			conn.disconnect();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		//mock
		SmartMeterHelper smh = new SmartMeterHelper();
		SmartElectricMeterList meters = new SmartElectricMeterList();
		String meterData = "";
		InputStream is = smh.getClass().getClassLoader().getResourceAsStream("data.xml");
		meters = smh.getMeters(is);
		
		System.out.println("===Print Result===");
		for(SmartElectricMeter sm : meters.getSmartElectricMeter()){
			System.out.println(sm.getMeterID() + "," + sm.getMeterName() + "," + sm.getUsage());
		}
		
		try {
			meterData = objToXml(meters,SmartElectricMeterList.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//debug
		System.out.println("Smart Meter Xml data: " + meterData);
		MessageDto msgDto = new MessageDto();
		msgDto.setReaderID(sensor.getID());
		msgDto.setSequence("0");
		msgDto.setXmlData(meterData);
		
		sensor.send(msgDto);
	}
	private String objToXml(Object obj,Class tclass) throws Exception {

		String retStr = null;
		if (obj != null) {
			try {
				OutputStream os = new ByteArrayOutputStream();// 32

				JAXBContext jc = JAXBContext.newInstance(tclass);
				Marshaller marshaller = jc.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION,
						"http://ciotc.org/wsn/Sensor/msg SensorMessage.xsd");
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.marshal(obj, os);

				retStr = os.toString();
			} catch (Exception e) {
				throw e;
			}
		}
		return retStr;
	}
}
