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
	private volatile String wsdlUrl = "http://159.226.228.92:1009/Service1.asmx?wsdl";
	private Integer delayTime = Integer.valueOf(10);
	private Integer intervalTime = Integer.valueOf(60);
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
		if((!this.destroyed.get()) && (this.session.get() == null)){
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if(this.session.compareAndSet(null, new EnvSensorSession(this,Integer.toString(sessionID.intValue()),
					this.wsdlUrl,this.delayTime,this.intervalTime,this.notifierService,getID(),new HashSet()))){
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	public String createSensorSession(SessionDTO sessionDTO) throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(Integer.parseInt(sessionDTO.getID()));
			if (this.session.compareAndSet(null,new EnvSensorSession(this, Integer.toString(sessionID.intValue()), 
						this.wsdlUrl, this.delayTime, this.intervalTime,this.notifierService, getID(), new HashSet()))) {
				((EnvSensorSession) this.session.get()).restoreCommands(sessionDTO);
				
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
	
	@Property(displayName = "WSDL URL",
			description = "WSDL URL of the PM25 Sensor Web Service Server",
			writable = true,type = PropertyType.PT_STRING,category = "connection",
			defaultValue = "http://159.226.228.92:1009/Service1.asmx?wsdl",
			orderValue = 1.0F,maxValue = "",minValue = "")
    public String getWsdlUrl(){
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl){
		this.wsdlUrl = wsdlUrl;
	}
	
	@Property(displayName = "DelayTime",
			description = "Call webservice delay time,this time is in seconds",
			writable = true,type = PropertyType.PT_INTEGER,category = "connection",
			defaultValue = "10",orderValue = 2.0F,maxValue = "",minValue = "0")
	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	@Property(displayName = "IntervalTime", 
			description = "Call webservice interval time, the time in seconds", 
			writable = true, type = PropertyType.PT_INTEGER, category = "connection", 
			defaultValue = "60", orderValue = 3.0F, maxValue = "", minValue = "0")
	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}
	
}
