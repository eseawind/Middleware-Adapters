/**
 *
 * MinaTCPHandler.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.Sensor;

/**
 * @author ZhangMin.name
 *
 */
public class MinaTCPHandler extends IoHandlerAdapter{
	private static final Log logger = LogFactory.getLog(MinaTCPHandler.class);
	private static String readerID;
	private static Sensor sensor;
	public MinaTCPHandler(String readerID, AbstractSensor<?> sensor) {
		this.readerID = readerID;
		this.sensor = sensor;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
