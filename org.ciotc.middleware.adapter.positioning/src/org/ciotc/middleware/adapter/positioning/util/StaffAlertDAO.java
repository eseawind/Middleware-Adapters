/**
 *
 * StaffAlertDAO.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.adapter.positioning.util;

import java.util.List;
import java.util.Map;

import org.ciotc.middleware.adapter.positioning.pojo.TargetInfoDto;
import org.ciotc.middleware.adapter.positioning.pojo.TracingTargetDto;
import org.ciotc.middleware.adapter.positioning.pojo.UserTargetOrgnaizeDto;
import org.ciotc.middleware.notification.StaffMessageDto;

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
	public UserTargetOrgnaizeDto getUTOByTargetID(String targetID);
	public List<UserTargetOrgnaizeDto> getUTOByTargetIDs(List<String> targetIDs);
	public List<TracingTargetDto> getTracingTargetsByLBSTraceTable();
	public List<TracingTargetDto> 
				getLeavingTracingTargetByAntennaID(String antennaID);
	public Map<String,Integer> 
				getTargetUserByTargetID(List<String> targets);
	public int getBatteryLifeByID(String battery);
	public String getAntennaIDByDevice(int deviceType);
	public void updateEnterLeaveInfo(StaffMessageDto smd);
	
}
