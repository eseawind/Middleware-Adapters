/**
 *
 * CardStayingAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.threadedtimertask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.threadedtimertask.dao.TracingTargetDto;

/**
 * @author ZhangMin.name
 *
 */
public class CardStayingAlert extends AbstractAlert {
	private static final Log logger = LogFactory.getLog(CardStayingAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("CardStayingAlert job started...");
		int interval = 30 * 60 * 1000;
		HashMap<String,Integer> targetToUser = new HashMap<String,Integer>();
		List<TracingTargetDto> tts = staffAlertDAO.getTracingTargetsByLBSTraceTable();
		Iterator<TracingTargetDto> it = tts.iterator();
		while(it.hasNext()){
			TracingTargetDto tt = it.next();
			Timestamp ts = tt.getElTime();
			if((System.currentTimeMillis() - ts.getTime()) > interval){
				targetToUser.put(tt.getTargetID(), tt.getUserID());
			}
		}
		//TODO remove after test
		Set<String> users = targetToUser.keySet();
		Iterator<String> it1 = users.iterator();
		while(it.hasNext()){
			String target = it1.next();
			int user = targetToUser.get(target);
			logger.info("[CardStayingAlertInfo] user_id:" + user +
					"target_id:" + target);
		}
		//sad.alarm(2, 2, targetToUser);
		
	}

}
