package org.ciotc.middleware.adapter.positioning;

import java.util.Set;

import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.sessions.AbstractServerMinaSocketSensorSession;
import org.rifidi.edge.api.SessionStatus;

public class PositioningSensorSession extends AbstractServerMinaSocketSensorSession {
	private volatile NotifierService notifierService;
	private String readerID = null;

	public PositioningSensorSession(AbstractSensor<?> sensor, String ID,
			NotifierService notifierService, String readerID, int serverSocketPort, 
			Set<AbstractCommandConfiguration<?>> commandConfigurations){
		super(sensor, ID, serverSocketPort, commandConfigurations, 
				new MinaTCPHandler(readerID, sensor));
		this.readerID = readerID;
		this.notifierService = notifierService;
	}

	protected void setStatus(SessionStatus status) {
		super.setStatus(status);

		this.notifierService.sessionStatusChanged(this.readerID, getID(), status);
	}
	
}
