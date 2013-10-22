package org.ciotc.middleware.adapter.positioning;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.MBeanInfo;
import org.ciotc.middleware.adapter.positioning.util.StaffLeaveDetector;
import org.ciotc.middleware.configuration.AnnotationMBeanInfoStrategy;
import org.ciotc.middleware.configuration.JMXMBean;
import org.ciotc.middleware.configuration.Property;
import org.ciotc.middleware.configuration.PropertyType;
import org.ciotc.middleware.exceptions.CannotCreateSessionException;
import org.ciotc.middleware.exceptions.CannotDestroySensorException;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensor;
import org.ciotc.middleware.sensors.SensorSession;
import org.rifidi.edge.api.SessionDTO;

@JMXMBean
public class PositioningSensor extends AbstractSensor<PositioningSensorSession> {
	private Integer port = Integer.valueOf(5002);
	private String host = "192.168.109.203";
	private AtomicInteger sessionID = new AtomicInteger(0);
	private String displayName = "PositioningSensor";
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	private AtomicReference<PositioningSensorSession> session = new AtomicReference();
	//private Timer timer = null;
	public static final MBeanInfo mbeaninfo;
	//private volatile StaffLeaveDetector staffLeaveDetector;
	
	static {
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy.getMBeanInfo(PositioningSensor.class);
	}

	public void applyPropertyChanges() {
	}

	public String createSensorSession() throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if (this.session.compareAndSet(null, new PositioningSensorSession(this,
					Integer.toString(sessionID.intValue()), this.notifierService,
					super.getID(), this.host,this.port.intValue(), new HashSet()))) {
				this.notifierService.addSessionEvent(getID(), 
						Integer.toString(sessionID.intValue()));
				//启动后台线程检测人员离开
				//timer = new Timer(true);
				//timer.schedule(staffLeaveDetector,0, 3000);
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	public String createSensorSession(SessionDTO sessionDTO) 
			throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if (this.session.compareAndSet(null, new PositioningSensorSession(this,
					Integer.toString(sessionID.intValue()), this.notifierService, 
					super.getID(), this.host,this.port.intValue(), new HashSet()))) {
				this.notifierService.addSessionEvent(getID(), 
						Integer.toString(sessionID.intValue()));
				//启动后台线程检测人员离开
				//timer = new Timer(true);
				//timer.schedule(new StaffLeaveDetector(),0, 3000);
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	public void destroySensorSession(String id) throws CannotDestroySensorException {
		PositioningSensorSession positioningSensorSession = 
				(PositioningSensorSession)this.session.get();
	    if ((positioningSensorSession != null) && 
	    		(positioningSensorSession.getID().equals(id))) {
	      this.session.set(null);
	      positioningSensorSession.killAllCommands();
	      positioningSensorSession.disconnect();

	      this.notifierService.removeSessionEvent(getID(), id);
	      //停止后台任务
	      //timer.cancel();
	    } else {
	      String error = "Tried to delete a non existend session: " + id;
	      throw new CannotDestroySensorException(error);
	    }
	}

	public Map<String, SensorSession> getSensorSessions() {
		Map ret = new HashMap();
		PositioningSensorSession genericsession = 
				(PositioningSensorSession) this.session.get();
		if (genericsession != null)
			ret.put(genericsession.getID(), genericsession);

		return ret;
	}

	public void unbindCommandConfiguration(
			AbstractCommandConfiguration<?> commandConfiguration,
			Map<?, ?> properties) {
	}

	public MBeanInfo getMBeanInfo() {
		return mbeaninfo;
	}
	
	@Property(displayName = "PositioningtSensor",description = "PositioningtSensor display name",
			writable = true,type = PropertyType.PT_STRING,category = "connection",
			defaultValue = "PositioningtSensor",
			orderValue = 0.0F,maxValue = "",minValue = "")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	@Property(displayName = "Host",
			description = "The host that the reader will listen for incoming connections from.",
			writable = true,type = PropertyType.PT_STRING,category = "connection",
			defaultValue = "192.168.109.203",
			orderValue = 1.0F,maxValue = "",minValue = "")
	public String getHost() {
		return host;
	}
	public void setHost(String host){
		this.host = host;
	}
	
	@Property(displayName = "Port", 
			description = "The port that the reader will listen for incoming connections from.", 
			writable = true, type = PropertyType.PT_INTEGER, category = "connection", 
			defaultValue = "5002", orderValue = 2.0F, maxValue = "", minValue = "")
	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

}
