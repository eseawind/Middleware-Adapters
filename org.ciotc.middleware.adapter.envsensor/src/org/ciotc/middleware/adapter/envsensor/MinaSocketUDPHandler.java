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
		String data = ib.getHexDump().replaceAll(" ", "");
		System.out.println(ib.getHexDump());
		
		Integer type = Integer.parseInt(data.substring(10, 14), 16);
		
		if(11 == type){
			logger.info(" type 11 ");
		}
		
		if(12 == type){
			
			//温度值
			Integer tv = Integer.parseInt(data.substring(28,32),16);
			double t = tv * 0.01 - 39.66;
			//湿度值
			Integer hv = Integer.parseInt(data.substring(32,36),16);
			double h = -4.0 + 0.0405 * hv -(2.8E-6)*hv*hv;
			//二氧化碳值
			Integer dv = Integer.parseInt(data.substring(36,40),16);
		    logger.info("carbon dioxide:" + dv + " temperature :" + t + "humidity :" + h);
			
		}
		//MessageDto msg = new MessageDto();
		
		
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
