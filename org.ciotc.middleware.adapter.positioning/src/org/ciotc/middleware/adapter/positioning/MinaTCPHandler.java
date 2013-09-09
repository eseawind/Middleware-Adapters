/**
 *
 * MinaTCPHandler.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning;

import java.net.InetSocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.ciotc.middleware.adapter.positioning.pojo.GwMessage;
import org.ciotc.middleware.notification.StaffMessageDto;
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
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		cause.printStackTrace();
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		IoBuffer ib = (IoBuffer)message;
		StaffMessageDto smd = new StaffMessageDto();
		GwMessage msg = new GwMessage(ib.array(),ib.array().length);
		if(msg.getType() == GwMessage.MT_LOCATION){
			
		}
		//GwMessage msg = (GwMessage)message;
		logger.info("Message received: " + ib.getHexDump());
		sensor.sendEvent(smd);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status)
			throws Exception {
		// TODO Auto-generated method stub
		super.sessionIdle(session, status);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
