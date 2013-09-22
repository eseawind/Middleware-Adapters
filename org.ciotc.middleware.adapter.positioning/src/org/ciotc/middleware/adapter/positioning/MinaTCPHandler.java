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
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.ciotc.middleware.adapter.positioning.pojo.GwMessage;
import org.ciotc.middleware.notification.StaffMessageDto;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.Sensor;

/**
 * @author ZhangMin.name
 *
 */
public class MinaTCPHandler extends IoHandlerAdapter{
	private static final Log logingData = LogFactory.getLog("positiondata");
	private static final Log logger = LogFactory.getLog(MinaTCPHandler.class);
	private static String readerID;
	private static Sensor sensor;
	public MinaTCPHandler(String readerID, AbstractSensor<?> sensor) {
		this.readerID = readerID;
		this.sensor = sensor;
		//set adapter specified log4j properties
		//PropertyConfigurator.configure(getClass().getResource("log4j.properties"));
	}
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		//cause.printStackTrace();
		logger.error("connection interrupt!");
		
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
	    //TODO logging received tags
		IoBuffer ib = (IoBuffer)message;
		logger.debug("Message received: " + ib.getHexDump());
		//将收到的数据分割成一个完整包后进行分析
		String rcvData = ib.getHexDump();
		String tmp = "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < rcvData.length() - 1; i++) {
			tmp = rcvData.substring(i, i + 2);
			if (tmp.equalsIgnoreCase("02")) {
				sb = new StringBuffer();
				sb.append(tmp);
			}else if(tmp.equalsIgnoreCase("03")) {
				sb.append(tmp);
				System.out.println("packet data:" + sb.toString());
				StaffMessageDto smd = GwMessage.parsePacket(GwMessage
						.hexToBytes(sb.toString()));
				if (null != smd) {
					
					logingData.info("AntennID:" + smd.getAntennID()
							+ ";BaseID:" + smd.getBaseID() + ";CardID:" + smd.getCardID()
							+ ";Time:" + smd.getTime());
					sensor.sendEvent(smd);
					IoBuffer resp = IoBuffer.wrap(GwMessage.makeHeartBeat());
					session.write(resp);
					logger.debug("response heartbeat packet send.");
				} else {
					logger.debug("heartbeat packet received: " + sb.toString());
				}
			}else{
				sb.append(tmp);
			}
			i += 2;
			sb.append(" ");
		}
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
		
	}

}
