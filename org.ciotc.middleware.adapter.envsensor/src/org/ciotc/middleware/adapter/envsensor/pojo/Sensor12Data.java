/**
 *
 * Sensor12Data.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.envsensor.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.envsensor.util.NameSpace;

/**
 * 二氧化碳和空气温湿度传感器数据类
 * 传感器类型 12(0C)
 * @author ZhangMin.name
 *
 */
@XmlRootElement(namespace=NameSpace.SMSG_URI,name="Sensor12Data")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sensor12Data", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	""
})
public class Sensor12Data {
	String carbonDioxide;
	String humidity;
	String temperature;
}
