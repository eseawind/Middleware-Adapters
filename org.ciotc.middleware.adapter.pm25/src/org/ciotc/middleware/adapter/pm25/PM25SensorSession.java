/**
 * PM25SensorSession.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 * 2013Äê8ÔÂ20ÈÕ
 */
package org.ciotc.middleware.adapter.pm25;
import java.util.Set;
import java.util.TimerTask;

import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.sessions.AbstractWebServiceClientSensorSession;
import org.rifidi.edge.api.SessionStatus;
/**
 * PM25 Sensor Session
 * @author ZhangMin.name
 *
 */
public class PM25SensorSession extends AbstractWebServiceClientSensorSession{
	private volatile NotifierService notifierService;
	private final String readerID;

	public PM25SensorSession(AbstractSensor<?> sensor,String id,String wsdl,int delayTime,int intervalTime,
			NotifierService notifierService,String readerID,Set<AbstractCommandConfiguration<?>> commands,Integer maxValue,Integer minValue){
		super(sensor,id,wsdl,commands,new PM25TimerTask(sensor,wsdl,maxValue,minValue),delayTime,intervalTime);
		this.notifierService = notifierService;
		this.readerID = readerID;
	}
	
	protected void setStatus(SessionStatus status){
		super.setStatus(status);
		
		this.notifierService.sessionStatusChanged(this.readerID, getID(), status);
	}
}
