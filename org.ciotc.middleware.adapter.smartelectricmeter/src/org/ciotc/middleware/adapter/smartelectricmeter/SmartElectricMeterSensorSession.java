/**
 * SmartElectricMeterSensorSession.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 * 2013年8月21日
 */
package org.ciotc.middleware.adapter.smartelectricmeter;

import java.util.Set;
import java.util.TimerTask;
import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.sessions.AbstractWebServiceClientSensorSession;
import org.rifidi.edge.api.SessionStatus;

/**
 * @author ZhangMin.name
 *
 */
public class SmartElectricMeterSensorSession extends AbstractWebServiceClientSensorSession{
	private volatile NotifierService notifierService;
	private final String readerID;
	
	public SmartElectricMeterSensorSession(AbstractSensor<?> sensor,String id,String wsdl,int delayTime,int intervalTime,
			NotifierService notifierService,String readerID,Set<AbstractCommandConfiguration<?>> commands){
		super(sensor,id,wsdl,commands,new SmartElectricMeterTimerTask(sensor,wsdl),delayTime,intervalTime);
		this.notifierService = notifierService;
		this.readerID = readerID;
	}
	protected void setStatus(SessionStatus status){
		super.setStatus(status);
		
		this.notifierService.sessionStatusChanged(this.readerID, getID(), status);
	}
}
