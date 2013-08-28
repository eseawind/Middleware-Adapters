package org.ciotc.middleware.adapter.smartelectricmeter.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.smartelectricmeter.util.NameSpace;


@XmlRootElement(namespace=NameSpace.SMSG_URI,name="SmartElectricMeterDataList")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SmartElectricMeterDataList", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
    "smartElectricMeterData"
})
public class SmartElectricMeterList {

    @XmlElement(required = true,namespace=NameSpace.SMSG_URI)
    protected ArrayList<SmartElectricMeter> smartElectricMeterData;

	public ArrayList<SmartElectricMeter> getSmartElectricMeter() {
		return smartElectricMeterData;
	}

	public void setSmartElectricMeter(ArrayList<SmartElectricMeter> meters) {
		this.smartElectricMeterData = meters;
	}

}
