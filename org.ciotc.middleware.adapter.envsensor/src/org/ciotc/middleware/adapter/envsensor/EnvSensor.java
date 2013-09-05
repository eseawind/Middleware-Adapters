package org.ciotc.middleware.adapter.envsensor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.MBeanInfo;

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
public class EnvSensor extends AbstractSensor<EnvSensorSession> {
	private Integer port = Integer.valueOf(4568);
	private AtomicInteger sessionID = new AtomicInteger(0);
	private String displayName = "EnvironmentSensor";
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	private AtomicReference<EnvSensorSession> session = new AtomicReference();
	public static final MBeanInfo mbeaninfo;

	static {
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy.getMBeanInfo(EnvSensor.class);
	}

	public void applyPropertyChanges() {
	}

	public String createSensorSession() throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if (this.session.compareAndSet(null, new EnvSensorSession(this,
					Integer.toString(sessionID.intValue()), this.notifierService, super.getID(), this.port.intValue(), new HashSet()))) {
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	public String createSensorSession(SessionDTO sessionDTO) throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if (this.session.compareAndSet(null, new EnvSensorSession(this,
					Integer.toString(sessionID.intValue()), this.notifierService, super.getID(), this.port.intValue(), new HashSet()))) {
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	public void destroySensorSession(String id) throws CannotDestroySensorException {
		EnvSensorSession envSensorSession = (EnvSensorSession)this.session.get();
	    if ((envSensorSession != null) && (envSensorSession.getID().equals(id))) {
	      this.session.set(null);
	      envSensorSession.killAllCommands();
	      envSensorSession.disconnect();

	      this.notifierService.removeSessionEvent(getID(), id);
	    } else {
	      String error = "Tried to delete a non existend session: " + id;
	      throw new CannotDestroySensorException(error);
	    }
	}

	public Map<String, SensorSession> getSensorSessions() {
		Map ret = new HashMap();
		EnvSensorSession genericsession = (EnvSensorSession) this.session.get();
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
	@Property(displayName = "EnvironmentSensor",description = "EnvironmentSensor display name",
			writable = true,type = PropertyType.PT_STRING,category = "connection",
			defaultValue = "EnvironmentSensor",
			orderValue = 0.0F,maxValue = "",minValue = "")
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	@Property(displayName = "Port", 
			description = "The port that the reader will listen for incoming connections from.", 
			writable = true, type = PropertyType.PT_INTEGER, category = "connection", 
			defaultValue = "4568", orderValue = 1.0F, maxValue = "", minValue = "")
	public Integer getPort() {
		return this.port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}
	
}
