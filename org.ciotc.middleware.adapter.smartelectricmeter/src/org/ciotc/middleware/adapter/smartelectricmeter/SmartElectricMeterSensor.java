/**
 * SmartElectricMeterSensor.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 * 2013年8月21日
 */
package org.ciotc.middleware.adapter.smartelectricmeter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import javax.management.MBeanInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

/**
 * @author ZhangMin.name
 *
 */
@JMXMBean
public class SmartElectricMeterSensor extends AbstractSensor< SmartElectricMeterSensorSession>{
	private static final Log logger = LogFactory.getLog(SmartElectricMeterSensor.class);
	private AtomicInteger sessionID = new AtomicInteger(0);
	private AtomicReference<SmartElectricMeterSensorSession> session = new AtomicReference();
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	private volatile String url = "http://192.168.0.195/MeterDataCache.xml";
	private Integer delayTime = Integer.valueOf(10);
	private Integer intervalTime = Integer.valueOf(60);
	private String displayName  = "SmartElectricMeterSensor";
	
	public static final MBeanInfo mbeaninfo;
	
	static{
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy.getMBeanInfo(SmartElectricMeterSensor.class);
	}
	
	@Override
	public String createSensorSession() throws CannotCreateSessionException {
		if((!this.destroyed.get()) && (this.session.get() == null)){
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if(this.session.compareAndSet(null, new SmartElectricMeterSensorSession(this,Integer.toString(sessionID.intValue()),
					this.url,this.delayTime,this.intervalTime,this.notifierService,getID(),new HashSet()))){
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	@Override
	public String createSensorSession(SessionDTO sessionDTO)
			throws CannotCreateSessionException {
		if ((!(this.destroyed.get())) && (this.session.get() == null)) {
			Integer sessionID = Integer.valueOf(Integer.parseInt(sessionDTO.getID()));
			if (this.session.compareAndSet(null,new SmartElectricMeterSensorSession(this, Integer.toString(sessionID.intValue()), 
						this.url, this.delayTime, this.intervalTime,this.notifierService, getID(), new HashSet()))) {
				((SmartElectricMeterSensorSession) this.session.get()).restoreCommands(sessionDTO);
				
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	@Override
	public Map<String, SensorSession> getSensorSessions() {
		Map ret = new HashMap();
		SmartElectricMeterSensorSession smartElectricSensorSession = (SmartElectricMeterSensorSession)this.session.get();
		if(smartElectricSensorSession != null){
			ret.put(smartElectricSensorSession.getID(), smartElectricSensorSession);
		}
		return ret;
	}

	@Override
	public void destroySensorSession(String sessionID)
			throws CannotDestroySensorException {
		SmartElectricMeterSensorSession smartElectricSensorSession = (SmartElectricMeterSensorSession) this.session.get();
		if((smartElectricSensorSession != null)  && (smartElectricSensorSession.getID().equals(sessionID))){
			this.session.set(null);
			smartElectricSensorSession.killAllCommands();
			smartElectricSensorSession.disconnect();
			this.notifierService.removeSessionEvent(getID(), sessionID);
		}else{
			String error = "Tried to delete a none existed session" + sessionID;
			throw new CannotDestroySensorException(error);
		}
		
		
	}

	@Override
	public void applyPropertyChanges() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getDisplayName() {
		return displayName;
	}

	@Override
	public void unbindCommandConfiguration(
			AbstractCommandConfiguration<?> paramAbstractCommandConfiguration,
			Map<?, ?> paramMap) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public MBeanInfo getMBeanInfo() {
		return ((MBeanInfo)mbeaninfo.clone());
	}
    
	protected void destory(){
		if(this.destroyed.compareAndSet(false, true)){
			super.destroy();
			SmartElectricMeterSensorSession smartElectricSensorSession = (SmartElectricMeterSensorSession) this.session.get();
			if(smartElectricSensorSession != null){
				try{
					destroySensorSession(smartElectricSensorSession.getID());
				}catch(CannotDestroySensorException e){
					logger.warn(e.getMessage());
				}
			}
		}
	}
	
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	@Property(displayName = "URL",description = "URL of the SmartElectricMeter server",writable = true,type = PropertyType.PT_STRING,category = "connection",defaultValue = "http://192.168.0.195/MeterDataCache.xml",orderValue = 0.0F,maxValue = "",minValue = "")
	public String getUrl(){
		return url;
	}
	public void setUrl(String url){
		this.url = url;
	}
	
	@Property(displayName = "DelayTime",description = "Call webservice delay time,this time is in seconds",writable = true,type = PropertyType.PT_INTEGER,category = "connection",defaultValue = "10",orderValue = 0.0F,maxValue = "",minValue = "0")
	public Integer getDelayTime() {
		return delayTime;
	}

	public void setDelayTime(Integer delayTime) {
		this.delayTime = delayTime;
	}

	@Property(displayName = "IntervalTime", description = "Call webservice interval time, the time in seconds", writable = true, type = PropertyType.PT_INTEGER, category = "connection", defaultValue = "60", orderValue = 0.0F, maxValue = "", minValue = "0")
	public Integer getIntervalTime() {
		return intervalTime;
	}

	public void setIntervalTime(Integer intervalTime) {
		this.intervalTime = intervalTime;
	}
}
