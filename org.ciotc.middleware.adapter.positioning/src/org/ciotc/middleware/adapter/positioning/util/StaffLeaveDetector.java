/**
 *
 * StaffLeaveDetector.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.notification.StaffMessageDto;


/**
 * @author ZhangMin.name
 *
 */
public class StaffLeaveDetector extends TimerTask {
	private static Map<String,Long> tracingTargets
			= new HashMap<String,Long>();
	private static final Log logingData = LogFactory.getLog("positiondata");
	private static final Log logger = LogFactory.getLog(StaffLeaveDetector.class);
	protected StaffAlertDAO staffAlertDAO;
	public void setStaffAlertDAO(StaffAlertDAO staffAlertDAO){
		this.staffAlertDAO = staffAlertDAO;
	}
	public static void put(StaffMessageDto smd){
		synchronized(tracingTargets){
			
			tracingTargets.put(smd.getCardID(), System.currentTimeMillis());
		}
	}
	public void run() {
		logingData.info("Staff Leave detector timer task started.");
		Map <String,Long> targets = tracingTargets;
		Set<String> keys = targets.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String cardID = it.next();
			long last = targets.get(cardID);
			long now = System.currentTimeMillis();
			if((now - last) > 3 * 1000){
				//Leaving
				logingData.info(cardID + " has left,time:" + last);
				//staffAlertDAO.updateEnterLeaveInfo(smd);
				it.remove();
			}
		}
	}

}
