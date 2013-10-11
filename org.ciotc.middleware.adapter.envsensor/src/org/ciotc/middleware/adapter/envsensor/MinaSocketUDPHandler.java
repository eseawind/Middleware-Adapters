/**
 *
 * MinaSocketUDPHandler.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.envsensor
 *
 */
package org.ciotc.middleware.adapter.envsensor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.ciotc.middleware.notification.MessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.Sensor;
import org.ciotc.middleware.adapter.envsensor.pojo.*;
import org.ciotc.middleware.adapter.envsensor.util.*;

/**
 * @author ZhangMin.name
 *
 */
public class MinaSocketUDPHandler extends IoHandlerAdapter {
	private static final Log logger = LogFactory.getLog(MinaSocketUDPHandler.class);
	private static String readerID;
	private static Sensor sensor;
	public MinaSocketUDPHandler(String readerID, AbstractSensor<?> sensor) {
		this.readerID = readerID;
		this.sensor = sensor;
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(session, cause);
	}
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		logger.info("...receiving msg...");
		IoBuffer ib = (IoBuffer)message;
		MessageDto msg = new MessageDto();
		String rcvData = ib.getHexDump().replaceAll(" ", "");
		logger.debug(ib.getHexDump());
		
		Integer type = Integer.parseInt(rcvData.substring(10, 14), 16);
		
		if(11 == type){
			Sensor11Data sd = (Sensor11Data)ProtocolParser.parse(rcvData,11);
			msg.setReaderID(this.readerID);
			msg.setSequence("0");
			msg.setXmlData(Convertor.objToXml(sd, Sensor11Data.class));
			//debug
			logger.info("senser type 11's xmldata:" + 
					Convertor.objToXml(sd, Sensor11Data.class));
		}
		
		if(12 == type){
			Sensor12Data sd = (Sensor12Data)ProtocolParser.parse(rcvData, 12);
			msg.setReaderID(this.readerID);
			msg.setSequence("0");
			msg.setXmlData(Convertor.objToXml(sd, Sensor12Data.class));
			//debug
			logger.info("senser type 12's xmldata:" + Convertor.objToXml(sd, Sensor12Data.class));
		}
		
	}
	@Override
	public void sessionClosed(IoSession session) throws Exception {
		logger.debug("Environment Sensor's UDP Server session closed.");
	}
	@Override
	public void sessionCreated(IoSession session) throws Exception {
		logger.debug("Environment Sensor's UDP Server session created");
	}
	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		logger.debug("UDP Server IDLE :" + session.getIdleCount(status));
	}
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		logger.debug("Environment Sensor's UDP Server session opened.");
	}
	

}
