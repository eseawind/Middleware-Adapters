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
		//超期停留时间单位为s
		int interval = 60 * 60 * 1000;
		HashMap<String,Integer> targetToUsers = new HashMap<String,Integer>();
		List<TracingTargetDto> tts = staffAlertDAO.getTracingTargetsByLBSTraceTable();
		Iterator<TracingTargetDto> it = tts.iterator();
		while(it.hasNext()){
			TracingTargetDto tt = it.next();
			if(tt.getElTime() != null){
				Timestamp ts = tt.getElTime();
				long enterTime = System.currentTimeMillis()- ts.getTime();
				if(enterTime > interval){
					targetToUsers.put(tt.getTargetID(), tt.getUserID());
				}
			}
			
		}
		staffAlertDAO.alarm(2, 2, targetToUsers);
		
	}

}
