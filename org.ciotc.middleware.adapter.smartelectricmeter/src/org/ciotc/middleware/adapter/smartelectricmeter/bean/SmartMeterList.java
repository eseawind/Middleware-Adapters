package org.ciotc.middleware.adapter.smartelectricmeter.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(namespace=NameSpace.SMSG_URI,name="SmartMeterList")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmartMeterList", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
    "SmartMeterData"
})
public class SmartMeterList {

    @XmlElement(required = true,namespace=NameSpace.SMSG_URI)
    protected ArrayList<SmartElectricMeter> smartElectricMeter;

	public ArrayList<SmartElectricMeter> getSmartElectricMeter() {
		return smartElectricMeter;
	}

	public void setSmartElectricMeter(ArrayList<SmartElectricMeter> meters) {
		this.smartElectricMeter = meters;
	}

}
