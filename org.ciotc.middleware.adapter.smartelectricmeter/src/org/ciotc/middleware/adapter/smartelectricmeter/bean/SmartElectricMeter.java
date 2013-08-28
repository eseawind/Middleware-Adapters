/**
 *
 * SmartMeter.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 *
 */
package org.ciotc.middleware.adapter.smartelectricmeter.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.smartelectricmeter.util.NameSpace;



/**
 * @author ZhangMin.name
 *
 */
@XmlRootElement(namespace=NameSpace.SMSG_URI,name="SmartElectricMeterData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmartElectricMeterData", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	"meterID",
	"meterName",
	"usage"
})
public class SmartElectricMeter {
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String meterID;
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String meterName;
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String usage;
	
	public String getMeterID() {
		return meterID;
	}
	public void setMeterID(String meterID) {
		this.meterID = meterID;
	}
	public String getMeterName() {
		return meterName;
	}
	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}
	public String getUsage() {
		return usage;
	}
	public void setUsage(String usage) {
		this.usage = usage;
	}
	
	
}
