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



/**
 * @author ZhangMin.name
 *
 */
@XmlRootElement(namespace=NameSpace.SMSG_URI,name="SmartElectricMeterData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmartElectricMeterData", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	"meterID",
	"meterName",
	"usage1"
})
public class SmartElectricMeter {
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String meterID;
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String meterName;
	
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String usage1;
	
	private String usage2;
	private String usage3;
	private String usage4;
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
	public String getValue1() {
		return usage1;
	}
	public void setValue1(String value1) {
		this.usage1 = value1;
	}
	public String getValue2() {
		return usage2;
	}
	public void setValue2(String value2) {
		this.usage2 = value2;
	}
	public String getValue3() {
		return usage3;
	}
	public void setValue3(String value3) {
		this.usage3 = value3;
	}
	public String getValue4() {
		return usage4;
	}
	public void setValue4(String value4) {
		this.usage4 = value4;
	}
	
}
