/**
 *
 * Sensor11Data.java
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
 * 光敏探测器和紫外线传感器数据类
 * 传感器类型 11(0B)
 * @author ZhangMin.name
 */

@XmlRootElement(namespace=NameSpace.SMSG_URI,name="Sensor11Data")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Sensor11Data", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	""
})

public class Sensor11Data {
	String uvIndex;
	String lightIntesity;
	
}
