/**
 *
 * StaffLeaveDetector.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.adapter.positioning.pojo.TracingTargetDto;
import org.ciotc.middleware.notification.StaffMessageDto;


/**
 * @author ZhangMin.name
 *
 */
public class StaffLeaveDetector extends TimerTask {
	private static Map<String,StaffMessageDto> tracingTargets
			= new HashMap<String,StaffMessageDto>();
	private static final Log logingData = LogFactory.getLog("positiondata");
	private static final Log logger = LogFactory.getLog(StaffLeaveDetector.class);
	private StaffAlertDAO staffAlertDAO;
	
	public void setStaffAlertDAO(StaffAlertDAO staffAlertDAO){
		this.staffAlertDAO = staffAlertDAO;
	}

	public static void put(StaffMessageDto smd){
		synchronized(tracingTargets){
			
			tracingTargets.put(smd.getCardID(), smd);
		}
	}
	public void run() {
		logingData.info("Staff Leave detector timer task started.");
		Map <String,StaffMessageDto> targets = tracingTargets;
		Set<String> keys = targets.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String cardID = it.next();
			StaffMessageDto smd = targets.get(cardID);
			Timestamp ts = Timestamp.valueOf(smd.getTime());
			long last = ts.getTime();
			long now = System.currentTimeMillis();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(last);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tts = sdf.format(c.getTime());
			if((now - last) > 3 * 1000){
				//Leaving
				logingData.info(cardID + " has left,time:" + tts + ",now:" 
						+ sdf.format(new Date()));
			
				//test DAO
				it.remove();
			}
		}
		StaffMessageDto test = new StaffMessageDto();
		test.setAntennID("0");
		test.setCardID("7777");
		test.setTime("2013-10-18 19:11:11");
		test.setBaseID("172");
		staffAlertDAO.updateEnterLeaveInfo(test);
	
	}
	public void runAlertJob() {
		logingData.info("Staff Leave detector timer task started.");
		Map <String,StaffMessageDto> targets = tracingTargets;
		Set<String> keys = targets.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String cardID = it.next();
			StaffMessageDto smd = targets.get(cardID);
			Timestamp ts = Timestamp.valueOf(smd.getTime());
			long last = ts.getTime();
			long now = System.currentTimeMillis();
			Calendar c = Calendar.getInstance();
			c.setTimeInMillis(last);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tts = sdf.format(c.getTime());
			if((now - last) > 3 * 1000){
				//Leaving
				logingData.info(cardID + " has left,time:" + tts + ",now:" 
						+ sdf.format(new Date()));
			
				//staffAlertDAO.updateEnterLeaveInfo(smd);
				it.remove();
			}
		}
			StaffMessageDto test = new StaffMessageDto();
				test.setAntennID("0");
				test.setCardID("7777");
				test.setTime("2013-10-18 19:11:11");
				test.setBaseID("172");
				staffAlertDAO.updateEnterLeaveInfo(test);
				
	}
	public static void main(String[] args){
		
	}

}
