package org.ciotc.middleware.adapter.envsensor;

import java.util.Map;

import javax.management.MBeanInfo;

import org.ciotc.middleware.exceptions.InvalidStateException;
import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensorFactory;

public class EnvSensorFactory extends AbstractSensorFactory<EnvSensor> {

	public static final String FACTORY_ID = "EnvSensor";
	private static final String description = "A EnvSensor Middlerware Adapter.  ";
	private static final String displayname = "EnvironmentSensor";
	private volatile NotifierService notifierService;

	public void setNotifierService(NotifierService notifierService) {
		this.notifierService = notifierService;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayname;
	}

	public void bindCommandConfiguration(AbstractCommandConfiguration commandConfiguration, Map properties) {
		
	}
	
	public void unbindCommandConfiguration(AbstractCommandConfiguration commandConfiguration, Map properties) {
		
	}

	public void createInstance(String serviceID) throws IllegalArgumentException, InvalidStateException {
		if (serviceID == null) {
			throw new IllegalArgumentException("ServiceID is null");
		}

		if (this.notifierService == null) {
			throw new InvalidStateException("All services are not set");
		}

		EnvSensor instance = new EnvSensor();
		instance.setID(serviceID);
		instance.setNotifiyService(this.notifierService);
		instance.register(getContext(), "EnvSensor");

	}

	public String getFactoryID() {
		return "EnvSensor";
	}

	public MBeanInfo getServiceDescription(String factoryID) {
		return EnvSensor.mbeaninfo;
	}

}
