/**
 *
 * Sensor11Data.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.positioning.pojo;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.positioning.util.NameSpace;


/**
 * @author ZhangMin.name
 */

@XmlRootElement(namespace=NameSpace.SMSG_URI,name="CardInfo")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CardInfo", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
	"id"
})

public class CardInfo {
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private String id;
	
}
