/**
 *
 * PM25Value.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 *
 */
package org.ciotc.middleware.adapter.pm25.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.pm25.util.NameSpace;

/**
 * @author ZhangMin.name
 *
 */
@XmlRootElement(namespace=NameSpace.SMSG_URI,name="PM25SensorData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PM25SensorData", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	"pm25Value"
})
public class PM25Value {
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String pm25Value;

	public String getPm25Value() {
		return pm25Value;
	}

	public void setPm25Value(String pm25Value) {
		this.pm25Value = pm25Value;
	}
	
}
