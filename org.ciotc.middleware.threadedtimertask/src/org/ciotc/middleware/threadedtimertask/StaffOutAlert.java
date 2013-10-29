/**
 *
 * StaffOutAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.threadedtimertask;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.threadedtimertask.dao.TracingTargetDto;



/**
 * @author ZhangMin.name
 *
 */
public class StaffOutAlert extends AbstractAlert{
	protected static final Log logger = 
			LogFactory.getLog(StaffOutAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("StaffOutAlert started...");
	   //获取大楼出口的天线id TODO 大楼出口devicetype_id暂定为5
	   String antennaID = staffAlertDAO.getAntennaIDByDevice(5);
	   HashMap<String,Integer> targetToUsers = new HashMap<String,Integer>();
	   List<TracingTargetDto> tts = 
			   staffAlertDAO.getLeavingTracingTargetByAntennaID(antennaID);
	   Iterator<TracingTargetDto> it = tts.iterator();
	   while(it.hasNext()){
		   TracingTargetDto tt = it.next();
		   targetToUsers.put(tt.getTargetID(),tt.getUserID());
	   }
	   //TODO remove after test
	   Set<String> targets = targetToUsers.keySet();
	   Iterator<String> it1 = targets.iterator();
	   while(it1.hasNext()){
		   String target = it1.next();
		   int user = targetToUsers.get(target);
		   logger.info("[StaffOutAlert] user_id:" + user +
					"target_id:" + target);
	   }
	   //sad.alarm(2, 5, targetToUsers);

	}
	

}
