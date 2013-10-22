package org.ciotc.middleware.adapter.positioning;

import java.util.Map;

import javax.management.MBeanInfo;

import org.ciotc.middleware.adapter.positioning.util.StaffAlertDAO;
import org.ciotc.middleware.adapter.positioning.util.StaffLeaveDetector;
import org.ciotc.middleware.exceptions.InvalidStateException;
import org.ciotc.middleware.notification.NotifierService;
import org.ciotc.middleware.sensors.AbstractCommandConfiguration;
import org.ciotc.middleware.sensors.AbstractSensorFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

public class PositioningSensorFactory extends AbstractSensorFactory<PositioningSensor>
									 implements ApplicationContextAware{

	public static final String FACTORY_ID = "PositioningSensor";
	private static final String description = "A PositioningSensor Middlerware Adapter.  ";
	private static final String displayname = "PositioningSensor";
	private volatile NotifierService notifierService;
	private volatile StaffLeaveDetector staffLeaveDetector;
	private ApplicationContext context;
	public void setStaffLeaveDetector(StaffLeaveDetector sld){
		this.staffLeaveDetector = sld;
	}
	public void setNotifierService(NotifierService notifierService) {
		this.notifierService = notifierService;
	}

	public String getDescription() {
		return description;
	}

	public String getDisplayName() {
		return displayname;
	}

	public void bindCommandConfiguration(AbstractCommandConfiguration commandConfiguration, 
			Map properties) {
		
	}
	
	public void unbindCommandConfiguration(AbstractCommandConfiguration commandConfiguration,
			Map properties) {
		
	}

	public void createInstance(String serviceID) 
			throws IllegalArgumentException, InvalidStateException {
		if (serviceID == null) {
			throw new IllegalArgumentException("ServiceID is null");
		}

		if (this.notifierService == null) {
			throw new InvalidStateException("All services are not set");
		}

		PositioningSensor instance = new PositioningSensor();
		instance.setID(serviceID);
		instance.setNotifiyService(this.notifierService);
		instance.register(getContext(), "PositioningSensor");
		//instance.setStaffLeaveDetector(staffLeaveDetector);
		instance.setContext(context);
		
	}

	public String getFactoryID() {
		return "PositioningSensor";
	}

	public MBeanInfo getServiceDescription(String factoryID) {
		return PositioningSensor.mbeaninfo;
	}
	@Override
	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		this.context = context;
		
	}
	

}
