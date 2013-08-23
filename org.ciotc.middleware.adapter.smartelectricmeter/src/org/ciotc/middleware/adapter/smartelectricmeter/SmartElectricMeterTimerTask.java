/**
 *
 * SmartElectricMeterTimerTask.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 *
 */
package org.ciotc.middleware.adapter.smartelectricmeter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.ciotc.middleware.adapter.smartelectricmeter.bean.SmartMeter;
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
		List<SmartMeter> meters = new ArrayList<SmartMeter>();
		InputStream is = smh.getClass().getClassLoader().getResourceAsStream("data.xml");
		meters = smh.getMeters(is);
		
		System.out.println("===Print Result===");
		for(SmartMeter sm : meters){
			System.out.println(sm.getMeterID() + "," + sm.getMeterName() + "," + sm.getValue1());
		}
		//debug
		//System.out.println(sb.toString());
		MessageDto msgDto = new MessageDto();
		msgDto.setReaderID(sensor.getID());
		msgDto.setSequence("0");
		msgDto.setXmlData("");
		
		sensor.send(msgDto);
	}
}
