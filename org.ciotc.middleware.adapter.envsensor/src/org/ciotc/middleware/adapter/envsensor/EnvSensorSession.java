package org.ciotc.middleware.adapter.envsensor;

import java.util.Set;
import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.sessions.AbstractServerMinaSocketSensorSession;
import org.ciotc.middleware.sensors.sessions.AbstractWebServiceClientSensorSession;
import org.rifidi.edge.api.SessionStatus;

public class EnvSensorSession extends AbstractWebServiceClientSensorSession {
	private volatile NotifierService notifierService;
	private String readerID = null;

	public EnvSensorSession(AbstractSensor<?> sensor,String id,String wsdl,int delayTime,int intervalTime,
			NotifierService notifierService,String readerID,Set<AbstractCommandConfiguration<?>> commands){
		super(sensor,id,wsdl,commands,new EnvSensorTimerTask(sensor,wsdl),delayTime,intervalTime);
		this.notifierService = notifierService;
		this.readerID = readerID;
	}

	protected void setStatus(SessionStatus status) {
		super.setStatus(status);

		this.notifierService.sessionStatusChanged(this.readerID, getID(), status);
	}
	
}
