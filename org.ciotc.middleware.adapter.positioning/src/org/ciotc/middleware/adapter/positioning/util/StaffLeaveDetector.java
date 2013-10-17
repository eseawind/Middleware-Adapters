/**
 *
 * StaffLeaveDetector.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.notification.StaffMessageDto;


/**
 * @author ZhangMin.name
 *
 */
public class StaffLeaveDetector implements Runnable{
	private static final Log logingData = LogFactory.getLog("positiondata");
	private static Map<StaffMessageDto,Long> tracingTargets
		= Collections.synchronizedMap(new HashMap<StaffMessageDto,Long>());
	private static final Log logger = LogFactory.getLog(StaffLeaveDetector.class);
	protected StaffAlertDAO staffAlertDAO;
	public void setStaffAlertDAO(StaffAlertDAO staffAlertDAO){
		this.staffAlertDAO = staffAlertDAO;
	}
	public static synchronized void add(StaffMessageDto smd){
		tracingTargets.put(
					smd, System.currentTimeMillis());	
		
	}
	public static Map<StaffMessageDto, Long> getTracingTargets() {
		return tracingTargets;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	@Override
	public void run() {
		Set<StaffMessageDto> keys = tracingTargets.keySet();
		Iterator<StaffMessageDto> it = keys.iterator();
		while(it.hasNext()){
			StaffMessageDto smd = it.next();
			Long last = tracingTargets.get(smd);
			if((System.currentTimeMillis() - last) > 10 * 1000){
				//Leaving
				logingData.info(smd.getCardID() + " has left the Tracing area");
				//staffAlertDAO.updateEnterLeaveInfo(smd);
				tracingTargets.remove(smd);
			}
		}
	}

}
