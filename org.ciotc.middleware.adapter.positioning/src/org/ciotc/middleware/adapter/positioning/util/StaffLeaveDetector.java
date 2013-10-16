/**
 *
 * StaffLeaveDetector.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.sql.Timestamp;
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
	private static Map<String,Long> tracingTargets
		= new HashMap<String,Long>();
	public static void add(StaffMessageDto smd){
		String cardID = smd.getCardID();
		tracingTargets.put(
					cardID, System.currentTimeMillis());	
		
	}
	public static Map<String, Long> getTracingTargets() {
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
		Set<String> keys = tracingTargets.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String cardID = it.next();
			Long last = tracingTargets.get(cardID);
			if((System.currentTimeMillis() - last) > 5 * 1000){
				//Leaving
				logingData.info(cardID + " has left the Tracing area");
				tracingTargets.remove(cardID);
			}
		}
	}

}
