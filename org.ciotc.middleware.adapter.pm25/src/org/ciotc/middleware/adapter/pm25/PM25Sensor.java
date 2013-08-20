/**
 * PM25Sensor.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 * 2013年8月20日
 */
package org.ciotc.middleware.adapter.pm25;
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
 * A PM25 Sensor
 * @author ZhangMin.name
 *
 */
@JMXMBean
public class PM25Sensor extends AbstractSensor<PM25SensorSession>{
	private static final Log logger = LogFactory.getLog(PM25Sensor.class);
	private AtomicInteger sessionID = new AtomicInteger(0);
	private AtomicReference<PM25SensorSession> session = new AtomicReference();
	private AtomicBoolean destroyed = new AtomicBoolean(false);
	private volatile String wsdlUrl = "http://221.6.107.78:8022/PM25WebService.asmx?WSDL";
	private Integer delayTime = Integer.valueOf(10);
	private Integer intervalTime = Integer.valueOf(60);
	private String displayName  = "PM25 Sensor";
	//Max value of PM25
	private Integer maxValue = Integer.valueOf(300);
	//Min value of PM25
	private Integer minValue = Integer.valueOf(20);
	
	public static final MBeanInfo mbeaninfo;
	
	static{
		AnnotationMBeanInfoStrategy strategy = new AnnotationMBeanInfoStrategy();
		mbeaninfo = strategy.getMBeanInfo(PM25Sensor.class);
	}
	
	@Override
	public String createSensorSession() throws CannotCreateSessionException {
		if((!this.destroyed.get()) && (this.session.get() == null)){
			Integer sessionID = Integer.valueOf(this.sessionID.incrementAndGet());
			if(this.session.compareAndSet(null, new PM25SensorSession(this,Integer.toString(sessionID.intValue()),
					this.wsdlUrl,this.delayTime,this.intervalTime,this.notifierService,getID(),new HashSet(),maxValue,minValue))){
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
			if (this.session.compareAndSet(null,new PM25SensorSession(this, Integer.toString(sessionID.intValue()), 
						this.wsdlUrl, this.delayTime, this.intervalTime,this.notifierService, getID(), new HashSet(),maxValue,minValue))) {
				((PM25SensorSession) this.session.get()).restoreCommands(sessionDTO);
				
				this.notifierService.addSessionEvent(getID(), Integer.toString(sessionID.intValue()));
				return sessionID.toString();
			}
		}
		throw new CannotCreateSessionException();
	}

	@Override
	public Map<String, SensorSession> getSensorSessions() {
		Map ret = new HashMap();
		PM25SensorSession pm25SensorSession = (PM25SensorSession)this.session.get();
		if(pm25SensorSession != null){
			ret.put(pm25SensorSession.getID(), pm25SensorSession);
		}
		return ret;
	}

	@Override
	public void destroySensorSession(String sessionID)
			throws CannotDestroySensorException {
		PM25SensorSession pm25SensorSession = (PM25SensorSession) this.session.get();
		if((pm25SensorSession != null)  && (pm25SensorSession.getID().equals(sessionID))){
			this.session.set(null);
			pm25SensorSession.killAllCommands();
			pm25SensorSession.disconnect();
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
			PM25SensorSession pm25SensorSession = (PM25SensorSession) this.session.get();
			if(pm25SensorSession != null){
				try{
					destroySensorSession(pm25SensorSession.getID());
				}catch(CannotDestroySensorException e){
					logger.warn(e.getMessage());
				}
			}
		}
	}
	
	public void setDisplayName(String displayName){
		this.displayName = displayName;
	}
	
	@Property(displayName = "WSDL URL",description = "WSDL URL of the PM25 Sensor Web Service Server",writable = true,type = PropertyType.PT_STRING,category = "connection",defaultValue = "http://221.6.107.78:8022/PM25WebService.asmx?WSDL",orderValue = 0.0F,maxValue = "",minValue = "")
	
	public String getWsdlUrl(){
		return wsdlUrl;
	}
	public void setWsdlUrl(String wsdlUrl){
		this.wsdlUrl = wsdlUrl;
	}
	
	@Property(displayName = "DelayTime",description = "Call webservice delay time,this time is in seconds",writable = true,type = PropertyType.PT_STRING,category = "connection",defaultValue = "10",orderValue = 0.0F,maxValue = "",minValue = "0")
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
	@Property(displayName = "maxValue", description = "max value of PM25", writable = true, type = PropertyType.PT_INTEGER, category = "connection", defaultValue = "200", orderValue = 0.0F, maxValue = "", minValue = "0")
	public Integer getMaxValue(){
		return this.maxValue;
	}
	public void setMaxValue(Integer maxValue){
		this.maxValue = maxValue;
	}
	@Property(displayName = "minValue", description = "min value of PM25", writable = true, type = PropertyType.PT_INTEGER, category = "connection", defaultValue = "00", orderValue = 0.0F, maxValue = "", minValue = "0")
	public Integer getMinValue(){
		return this.minValue;
	}
	public void setMinValue(Integer minValue){
		this.minValue = minValue;
	}
}
