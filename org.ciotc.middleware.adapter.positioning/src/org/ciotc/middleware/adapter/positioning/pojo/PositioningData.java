/**
 *
 * Sensor11Data.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.positioning.pojo;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.ciotc.middleware.adapter.positioning.util.NameSpace;


/**
 * 定位数据类
 * @author ZhangMin.name
 */

@XmlRootElement(namespace=NameSpace.SMSG_URI,name="PositioningData")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PositioningData", namespace = "http://ciotc.org/wsn/Sensor/msg", propOrder = {
		"cardID",
		"antennaID",
		"baseID",
		"time"
})

public class PositioningData {
	/** 定位器号 */
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private Integer cardID;
	/** 阅读器（基站）号 */
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private Integer readerID;
	/** 激活器（天线）号 */
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private Integer antennaID;
	/** 接收日期  年月日时分秒*/
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private Date date;
	/** 接收时间  毫秒*/
	@XmlElement(required = true,namespace=NameSpace.SMSG_URI)
	private Integer millisecond;
	
}
