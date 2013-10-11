/**
 * PM25SensorFactory.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.pm25
 * 2013��8��20��
 */
package org.ciotc.middleware.adapter.pm25;
import java.util.Map;

import javax.management.MBeanInfo;

import org.ciotc.middleware.exceptions.InvalidStateException;
import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensorFactory;
/**
 * @author ZhangMin.name
 *
 */
public class PM25SensorFactory extends AbstractSensorFactory<PM25Sensor>{
	public static final String FACTORY_ID = "PM25Sensor";
	public static final String description = "A PM25Sensor Adapter";
	public static final String displayName = "PM25Sensor";
	private volatile NotifierService notifierService;
	
	@Override
	public void createInstance(String serviceID)
			throws IllegalArgumentException, InvalidStateException {
		if(serviceID == null){
			throw new IllegalArgumentException("ServiceID is null");
		}
		
		if(this.notifierService == null){
			throw new InvalidStateException("All service are not set");
		}
		
		PM25Sensor instance = new PM25Sensor();
		instance.setID(serviceID);
		instance.setNotifiyService(this.notifierService);
		instance.register(getContext(), "PM25Sensor");
	}

	@Override
	public MBeanInfo getServiceDescription(String factoryID) {
		return PM25Sensor.mbeaninfo;
	}

	@Override
	public String getFactoryID() {
		return this.FACTORY_ID;
	}

	@Override
	public String getDisplayName() {
		return this.displayName;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public void bindCommandConfiguration(
			AbstractCommandConfiguration<?> paramAbstractCommandConfiguration,
			Map<?, ?> paramMap) {
		
		
	}

	@Override
	public void unbindCommandConfiguration(
			AbstractCommandConfiguration<?> paramAbstractCommandConfiguration,
			Map<?, ?> paramMap) {
		
		
	}
	
	public void setNotifierService(NotifierService notifierService){
		this.notifierService = notifierService;
	}
}
