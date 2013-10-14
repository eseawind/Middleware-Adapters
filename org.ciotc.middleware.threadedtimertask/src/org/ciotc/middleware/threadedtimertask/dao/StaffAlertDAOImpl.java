/**
 *
 * StaffAlertDAOImpl.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.threadedtimertask.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.threadedtimertask.AbstractAlert;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * @author ZhangMin.name
 *
 */
public class StaffAlertDAOImpl implements StaffAlertDAO{
	private static final Log logger = LogFactory.getLog(StaffAlertDAOImpl.class);
	protected SimpleDriverDataSource dataSource;
	public void setDataSource(SimpleDriverDataSource sdds){
		this.dataSource = sdds;
	}
	public ResultSet exeuteSQL(String sql){
		Connection conn = null;
		ResultSet rs = null;
		if(dataSource == null){
			logger.error("dataSource can not be null");
		}
		try {
			conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error("execute sql error " + sql);
		}finally{
			close(conn);
		}
		return rs;
	}
	public Connection getConnection(){
		Connection conn = null;
		if(dataSource == null){
			logger.error("dataSource can not be null");
		}
		try {
			 conn = dataSource.getConnection();
		} catch (SQLException e) {
			logger.error("Can not get connection from dataSource");
		}
		return conn;
	}
	public void close(Connection conn){
		try {
			if (conn != null) {
				conn.close();
				conn = null;
			}
		} catch (SQLException e) {
			logger.error("close SQLException!");
			e.printStackTrace();
		}
	}
	@Override
	public void insertEventLog(int eventTypeID, int subEventType, String targetID,
			int userID) {
		Connection conn = getConnection();
		Date date = new Date();
		Timestamp eventTime = new Timestamp(date.getTime());
		try {
			Statement statement = conn.createStatement();
			String sql = 
					"SELECT handlestatus FROM t_manageeventlog WHERE eventtype_id=" 
					+ eventTypeID 
					+ " AND subEventType=" + subEventType + " AND user_id=" 
					+ userID + " AND handlestatus=0";
			ResultSet rs = statement.executeQuery(sql);
			if(!rs.next()) {
				String insertSql = 
						"INSERT INTO t_manageeventlog(event_time, "
						+ "eventtype_id, subevent_type, target_id, user_id) " 
						+ "VALUES (?, ?, ?, ?, ?)";
				PreparedStatement ps = conn.prepareStatement(insertSql);
				ps.setTimestamp(1, eventTime);
				ps.setInt(2, eventTypeID);
				ps.setInt(3, subEventType);
				ps.setString(4, targetID);
				ps.setInt(5, userID);
				ps.executeUpdate();
			}
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage());
		} finally{
			close(conn);
		}
	}
	@Override
	public List<String> getTargetsFromLBSTraceTable() {
		List<String> targets = new ArrayList<String>();
		ResultSet rs = this.exeuteSQL(
				"SELECT target_id FROM t_lbstracedata");
		try {
			while(rs.next()){
				targets.add(rs.getString(1));
			}
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return targets;
	}
	@Override
	public List<String> getLeaveTargetsFromLBSTraceTableByAntennaID(
			List<String> anntennaIDs) {
		
		return null;
	}
	@Override
	public Map<String, Integer> getTargetUserByTargetID(
			List<String> targets) {
		Connection conn = this.getConnection();
		Map<String,Integer> targetToUser = new HashMap<String,Integer>();
		Iterator<String> it = targets.iterator();
		while(it.hasNext()){
			String target = it.next();
			try {
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT target_id,user_id FROM T_UserTargetOrgnaize " +
						" Where target_id = \'" + target + "\'");
				while(rs.next()){
					targetToUser.put(rs.getString(1), rs.getInt(2));
				}
				
			} catch (SQLException e) {
				logger.error("error occured when executing sql");
				e.printStackTrace();
			}
			
		}
		close(conn);
		return targetToUser;
	}
	@Override
	public int getBatteryLifeByID(String battery) {
		int avgtime = -1;
		ResultSet rs = this.exeuteSQL(
				"SELECT avgtime FROM t_battery WHERE battery_id = \'"
				+ battery + "\'");
		try {
			rs.next();
			avgtime = rs.getInt(1);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return avgtime;
	}
	@Override
	public List<TargetInfoDto> getTargetsInfoByLBSTraceTable() {
		List<TargetInfoDto> targets = new ArrayList<TargetInfoDto>();
		ResultSet rs = this.exeuteSQL(
				"SELECT * FROM t_target WHERE target_id "+
				"IN (SELECT DISTINCT target_id FROM t_lbsdata)");
		try {
			while(rs.next()){
				TargetInfoDto ttd = new TargetInfoDto();
				ttd.setTargetID(rs.getString("target_id"));
				ttd.setTargetUseTime(rs.getInt("target_usetime"));
				ttd.setBattery(rs.getString("battery_id"));
				ttd.setTargetStatus(rs.getInt("target_status"));
				ttd.setTargetBarCode(rs.getString("target_barcode"));
				ttd.setRemark(rs.getString("remark"));
				targets.add(ttd);
			}
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return targets;
	}
	@Override
	public List<UserTargetOrgnaizeDto> getUTOByLBSTraceTable() {
		List<UserTargetOrgnaizeDto> utos = 
				new ArrayList<UserTargetOrgnaizeDto>();
		ResultSet rs = this.exeuteSQL(
				"SELECT * FROM T_UserTargetOrgnaize" +
				" Where target_id IN " +
			    "(SELECT DISTINCT target_id FROM T_LBSTraceData)");
		try {
			while(rs.next()){
				UserTargetOrgnaizeDto uto = new UserTargetOrgnaizeDto();
				uto.setUserID(rs.getInt("user_id"));
				uto.setOrganizeID(rs.getInt("organize_id"));
				uto.setTargetID(rs.getString("target_id"));
				uto.setTargetCode(rs.getString("target_code"));
				uto.setValidDate(rs.getTimestamp("validdate"));
				uto.setDistributeStatus(rs.getInt("distributestatue"));
				uto.setDistributeTime(rs.getTimestamp("distributetime"));
				uto.setRecycleTime(rs.getTimestamp("recycletime"));
				uto.setRecycleStatus(rs.getInt("recyclestatue"));
				uto.setVersion(rs.getInt("version"));
				uto.setReason(rs.getString("reason"));
				uto.setOperaterID(rs.getInt("operater_id"));
				uto.setUsertypeID(rs.getInt("usertype_id"));
				utos.add(uto);
			}
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return utos;
	}
	@Override
	public void alarm(int eventTypeID, int subEventType,
			Map<String, Integer> targetToUsers) {
		Set<String> users = targetToUsers.keySet();
		Iterator<String> it2 = users.iterator();
		while(it2.hasNext()){
			String target = it2.next();
			int user = targetToUsers.get(target);
			logger.info("Insert AlertEvent: " + "Type:" + 
						eventTypeID + ",SubType:" +
						subEventType + ",user_id:" + 
						user + ",target_id:" + target);
			//TODO 暂时不插入数据库，方便测试
			//this.insertEventLog(eventTypeID, subEventType, target, user);
		}
		
	}
	@Override
	public List<TracingTargetDto> getTracingTargetsByLBSTraceTable() {
		List<TracingTargetDto> tts = new ArrayList<TracingTargetDto>();
		ResultSet rs = this.exeuteSQL(
				"SELECT * FROM t_lbstracedata");
		try {
			while(rs.next()){
				TracingTargetDto tt = new TracingTargetDto();
				tt.setAreaID(rs.getInt("area_id"));
				tt.setElFlag(rs.getInt("elflag"));
				tt.setElTime(rs.getTimestamp("eltime"));
				tt.setTargetID(rs.getString("target_id"));
				tt.setUserID(rs.getInt("user_id"));
				tts.add(tt);
			}
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return tts;
	}
	@Override
	public List<TracingTargetDto> getLeavingTracingTargetByAntennaID(
			String antennaID) {
		List<TracingTargetDto> tts = new ArrayList<TracingTargetDto>();
		ResultSet rs = this.exeuteSQL(
				"SELECT * FROM t_lbstracedata WHERE target_id " +
				"IN ( SELECT target_id FROM t_lbsdata WHERE elflag = 1 AND " +
				" antenna_id = " + antennaID + " )");
		try {
			while(rs.next()){
				TracingTargetDto tt = new TracingTargetDto();
				tt.setAreaID(rs.getInt("area_id"));
				tt.setElFlag(rs.getInt("elflag"));
				tt.setElTime(rs.getTimestamp("eltime"));
				tt.setTargetID(rs.getString("target_id"));
				tt.setUserID(rs.getInt("user_id"));
				tts.add(tt);
			}
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return tts;
	}
	@Override
	public String getAntennaIDByDevice(int deviceType) {
		String antenna = null;
		ResultSet rs = this.exeuteSQL(
				"SELECT antenna_id FROM t_antenna WHERE devicetype_id = " 
						+ deviceType);
		try {
			rs.next();
			antenna = rs.getString(1);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		
		return antenna;
	}
}
