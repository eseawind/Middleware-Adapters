/**
 *
 * Convertor.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 *
 */
package org.ciotc.middleware.adapter.envsensor.util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * @author ZhangMin.name
 *
 */
public class Convertor {
	public static  String objToXml(Object obj,Class tclass) throws Exception {

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
