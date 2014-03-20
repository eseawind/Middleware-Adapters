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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.notification.StaffMessageDto;
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
	   //获取大楼出口的天线id
	   List<String> antennaIDs = staffAlertDAO.getAntennaIDsByDevice(2);
	   HashMap<String,Integer> targetToUsers = new HashMap<String,Integer>();
	   List<TracingTargetDto> tts = 
			   staffAlertDAO.getLeavingTracingTargetByAntennaIDs(antennaIDs);
	   Iterator<TracingTargetDto> it = tts.iterator();
	   while(it.hasNext()){
		   TracingTargetDto tt = it.next();
		   if(tt.getTargetID() != null){
			   Timestamp ts = tt.getElTime();
			   //t_lbstracedata 中5分钟内没有更新，则认为已经离开
			   if(System.currentTimeMillis() - ts.getTime() > 30 * 1000){
				   StaffMessageDto smd = new StaffMessageDto();
				   smd.setCardID(tt.getTargetID());
				   smd.setTime(tsToString(tt.getElTime()));
				   staffAlertDAO.updateEnterLeaveInfo(smd);
				   targetToUsers.put(tt.getTargetID(),tt.getUserID());
			   }
		   }
		  
	   }
	   staffAlertDAO.alarm(2, 5, targetToUsers);

	}
	/**
	 * 将一个Timestamp对象格式化为 yyyy-MM-dd HH:mm:ss,
	 * 因为Timestamp自带方法均已被废弃
	 * @param ts
	 * @return
	 */
	public static String tsToString(Timestamp ts){
		if(ts == null){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ts.getTime());
		StringBuffer sb = new StringBuffer();
		sb.append(sdf.format(c.getTime()));
		return sb.toString();
	}

}
