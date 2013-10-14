/**
 *
 * StaffAlertDAO.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.threadedtimertask.dao;

import java.util.List;
import java.util.Map;

/**
 * @author ZhangMin.name
 *
 */
public interface StaffAlertDAO {
	public void insertEventLog(int eventTypeID, int subEventType,
						String targetID, int userID);
	public void alarm(int eventTypeID,int subEventType,
				Map<String,Integer> targetToUsers);
	public List<String> getTargetsFromLBSTraceTable();
	public List<TargetInfoDto> getTargetsInfoByLBSTraceTable();
	public List<UserTargetOrgnaizeDto> getUTOByLBSTraceTable();
	public List<TracingTargetDto> getTracingTargetsByLBSTraceTable();
	public List<TracingTargetDto> 
				getLeavingTracingTargetByAntennaID(String antennaID);
	public List<String> getLeaveTargetsFromLBSTraceTableByAntennaID(
				List<String> anntennaIDs);
	public Map<String,Integer> 
				getTargetUserByTargetID(List<String> targets);
	public int getBatteryLifeByID(String battery);
	public String getAntennaIDByDevice(int deviceType);
	
}
