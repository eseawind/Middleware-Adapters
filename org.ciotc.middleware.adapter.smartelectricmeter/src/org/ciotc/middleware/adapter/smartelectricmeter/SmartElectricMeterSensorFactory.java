/**
 * SmartElectricMeterSensorFactory.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.smartelectricmeter
 * 2013年8月21日
 */
package org.ciotc.middleware.adapter.smartelectricmeter;

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
public class SmartElectricMeterSensorFactory extends AbstractSensorFactory<SmartElectricMeterSensor>{
	public static final String FACTORY_ID = "SmartElectricMeter Sensor";
	public static final String description = "A SmartElectricMeter Sensor Adapter";
	public static final String displayName = "SmartElectricMeter Sesnor";
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
		
		SmartElectricMeterSensor instance = new SmartElectricMeterSensor();
		instance.setID(serviceID);
		instance.setNotifiyService(this.notifierService);
		instance.register(getContext(), "SmartElectricMeter Sensor");
	}

	@Override
	public MBeanInfo getServiceDescription(String factoryID) {
		return SmartElectricMeterSensor.mbeaninfo;
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
