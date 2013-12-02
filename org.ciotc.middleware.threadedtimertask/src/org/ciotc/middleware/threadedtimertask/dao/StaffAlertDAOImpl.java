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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.notification.StaffMessageDto;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * 数据库操作封装类
 * @author ZhangMin.name
 *
 */
public class StaffAlertDAOImpl implements StaffAlertDAO{
	private static final Log logger = LogFactory.getLog(StaffAlertDAOImpl.class);
	protected SimpleDriverDataSource dataSource;
	public void setDataSource(SimpleDriverDataSource sdds){
		this.dataSource = sdds;
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
	public void closeStmt(Statement stmt){
		if( stmt != null){
			try {
				stmt.close();
			} catch (SQLException e) {
				logger.error("Statement close error!");
				e.printStackTrace();
			}
		}
	}
	/**
	 * 向t_manageeventlog表中插入告警事件，如果已经被处理则取消插入。
	 */
	@Override
	public void insertEventLog(int eventTypeID, int subEventType, String targetID,
			int userID) {
		Connection conn = getConnection();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp eventTime = Timestamp.valueOf(sdf.format(new Date()));
		try {
			Statement statement = conn.createStatement();
			String sql = 
					"SELECT handlestatus FROM t_manageeventlog WHERE eventtype_id=" 
					+ eventTypeID 
					+ " AND subevent_type=" + subEventType + " AND user_id=" 
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
				closeStmt(ps);
				logger.info("Insert AlertEvent: " + "Type: " + 
						eventTypeID + ",SubType: " +
						subEventType + ",user_id: " + 
						userID + ",target_id: " + targetID);
			}
			closeStmt(statement);
			
		} catch (SQLException e) {
			logger.error("SQLException: " + e.getMessage());
		} finally{
			
			close(conn);
			
		}
	}
	/**
	 * 从实时定位表t_lbstracedata中查询正在被定位的target_id
	 */
	@Override
	public List<String> getTargetsFromLBSTraceTable() {
		List<String> targets = new ArrayList<String>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT target_id FROM t_lbstracedata");
			while(rs.next()){
				targets.add(rs.getString(1));
			}
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return targets;
	}
	/**
	 * 根据target_id获取Map<target_id,user_id>
	 */
	@Override
	public Map<String, Integer> getTargetUserByTargetID(
			List<String> targets) {
		Map<String,Integer> targetToUser = new HashMap<String,Integer>();
		Iterator<String> it = targets.iterator();
		while(it.hasNext()){
			String target = it.next();
			try {
				Statement stmt = this.getConnection().createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT target_id,user_id FROM T_UserTargetOrgnaize " +
						" Where target_id = \'" + target + "\'");
				while(rs.next()){
					targetToUser.put(rs.getString(1), rs.getInt(2));
				}
				closeStmt(stmt);
			} catch (SQLException e) {
				logger.error("error occured when executing sql");
				e.printStackTrace();
			}	
		}
		return targetToUser;
	}
	/**
	 * 根据电池类型获得电池估计寿命
	 */
	@Override
	public int getBatteryLifeByID(String battery) {
		int avgtime = -1;
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT avgtime FROM t_battery WHERE battery_id = \'"
							+ battery + "\'");
			while(rs.next()){
				avgtime = rs.getInt(1);
			}
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return avgtime;
	}
	/**
	 * 根据实时定位表中的target_id获取Target对象
	 */
	@Override
	public List<TargetInfoDto> getTargetsInfoByLBSTraceTable() {
		List<TargetInfoDto> targets = new ArrayList<TargetInfoDto>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM t_target WHERE target_id "+
					"IN (SELECT DISTINCT target_id FROM t_lbstracedata)");
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
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return targets;
	}
	/**
	 * 获取正在被定为对象的UserTargetOrganize属性封装类
	 */
	@Override
	public List<UserTargetOrgnaizeDto> getUTOByLBSTraceTable() {
		List<UserTargetOrgnaizeDto> utos = 
				new ArrayList<UserTargetOrgnaizeDto>();
	
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT * FROM T_UserTargetOrgnaize" +
					" Where target_id IN " +
				    "(SELECT DISTINCT target_id FROM T_LBSTraceData)");
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
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return utos;
	}
	/**
	 * 根据target_id和user_id 及事件类型插入告警数据库
	 */
	@Override
	public void alarm(int eventTypeID, int subEventType,
			Map<String, Integer> targetToUsers) {
		Set<String> users = targetToUsers.keySet();
		Iterator<String> it2 = users.iterator();
		Connection conn = null;
		
		Date date = new Date();
		Timestamp eventTime = new Timestamp(date.getTime());
		
		while(it2.hasNext()){
			String target = it2.next();
			int user = targetToUsers.get(target);
			try{
				conn = this.dataSource.getConnection();
				Statement statement = conn.createStatement();
				String sql = "SELECT handlestatus FROM t_manageeventlog WHERE eventtype_id=" + eventTypeID 
					+ " AND subevent_type=" + subEventType + " AND target_id='" + target + "' AND handlestatus=0";
				ResultSet rs = statement.executeQuery(sql);
				
				if(!rs.next()) {
					String insertSql = "INSERT INTO t_manageeventlog(event_time, eventtype_id, subevent_type, user_id, target_id) " +
						"VALUES (?, ?, ?, ?, ?)";
					PreparedStatement ps = conn.prepareStatement(insertSql);
					ps.setTimestamp(1, eventTime);
					ps.setInt(2, eventTypeID);
					ps.setInt(3, subEventType);
					ps.setInt(4, user);
					ps.setString(5, target);
					ps.executeUpdate();
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
	
		}
		
	}
	/**
	 * 从t_lbstracedata中获取当前正在被追踪定位的对象
	 */
	@Override
	public List<TracingTargetDto> getTracingTargetsByLBSTraceTable() {
		List<TracingTargetDto> tts = new ArrayList<TracingTargetDto>();
	
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT * FROM t_lbstracedata");
			while(rs.next()){
				TracingTargetDto tt = new TracingTargetDto();
				tt.setAreaID(rs.getInt("area_id"));
				tt.setElFlag(rs.getInt("elflag"));
				tt.setElTime(rs.getTimestamp("eltime"));
				tt.setTargetID(rs.getString("target_id"));
				tt.setUserID(rs.getInt("user_id"));
				tts.add(tt);
			}
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return tts;
	}
	/**
	 * 根据antenna_id 和进去标志elflag查询当前定位对象中在要离开的定位对象
	 */
	@Override
	public List<TracingTargetDto> getLeavingTracingTargetByAntennaID(
			String antennaID) {
		List<TracingTargetDto> tts = new ArrayList<TracingTargetDto>();
		
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT * FROM t_lbstracedata WHERE area_id " +
				"IN ( SELECT DISTINCT area_id FROM g_area WHERE " +
				" antenna_id = " + antennaID + " )");
			while(rs.next()){
				TracingTargetDto tt = new TracingTargetDto();
				tt.setAreaID(rs.getInt("area_id"));
				tt.setElFlag(rs.getInt("elflag"));
				tt.setElTime(rs.getTimestamp("eltime"));
				tt.setTargetID(rs.getString("target_id"));
				tt.setUserID(rs.getInt("user_id"));
				tts.add(tt);
			}
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return tts;
	}
	/**
	 * 根据设备类型从表t_antenna中获取antenna_id
	 */
	@Override
	public String getAntennaIDByDevice(int deviceType) {
		String antenna = null;
		
		try {
			Statement stmt = this.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(
				"SELECT antenna_id FROM t_antenna WHERE devicetype_id = " 
						+ deviceType);
			while(rs.next()){
				antenna = rs.getString(1);
			}
			closeStmt(stmt);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		
		return antenna;
	}
	/**
	 * 当检测到有人员离开通讯基站可以覆盖的范围时，更新
	 * t_enterleaveinfo表，并删除t_lbstracedata表
	 * 中的相关条目。！作为一个事务执行。
	 */
	@Override
	public void updateEnterLeaveInfo(StaffMessageDto smd) {
		Connection conn = this.getConnection();
		try {
			Statement stmt = conn.createStatement();
			PreparedStatement stmti = null;
			Statement stmtd = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(
					"SELECT target_id,eltype FROM t_enterleaveinfo WHERE target_id = \'" 
							+ smd.getCardID() + "\' ORDER BY eltime DESC LIMIT 1");
			while(rs1.next()){
				if(rs1.getInt("eltype") == 0){
					String targetID = smd.getCardID();
					UserTargetOrgnaizeDto uto = this.getUTOByTargetID(targetID);
					String sql = "INSERT INTO t_enterleaveinfo(user_id,organize_id,target_id," +
					     "target_code,validdate,distributestatue,distributetime," +
					     "recyclestatue,recycletime,eltype,eltime)VALUES(?,?,?,?,?,?,?,?,?,?,?) ";
					stmti = conn.prepareStatement(sql);
					if(uto.getTargetID() == null){
						throw new NullPointerException();
					}
					stmti.setInt(1, uto.getUserID());
					stmti.setInt(2, uto.getOperaterID());
					stmti.setString(3, uto.getTargetID());
					stmti.setString(4, uto.getTargetCode());
					stmti.setTimestamp(5, uto.getValidDate());
					stmti.setInt(6, uto.getDistributeStatus());
					stmti.setTimestamp(7, uto.getDistributeTime());
					stmti.setInt(8, uto.getRecycleStatus());
					stmti.setTimestamp(9, uto.getRecycleTime());
					stmti.setInt(10,1);
					stmti.setTimestamp(11,strToTimestamp(smd.getTime()));
					int status = stmti.executeUpdate();
					if(status == 1){
						stmtd.executeUpdate(
								"DELETE FROM t_lbstracedata WHERE target_id = \'" +
	        	        smd.getCardID() +"\'");
					}
					stmti.close();
					
				}
//				else{
//					  
//				    stmtd.executeUpdate(
//        			"DELETE FROM t_lbstracedata WHERE target_id = \'" +
//        	        smd.getCardID() +"\'");
//				}
			}
			closeStmt(stmt);
			closeStmt(stmtd);
		    close(conn);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		} catch (NullPointerException e){
			logger.error("no related data in t_enterleaveinfo for target_id : " 
						+ smd.getCardID());
			e.printStackTrace();
		}
		
		
	}
	/**
	 * 将一个 'yyyy-MM-dd HH:mm:ss'转为Timestamp类型
	 */
	public Timestamp strToTimestamp(String str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = sdf.parse(str);
		} catch (ParseException e) {
			logger.error("parse error while convert str to timestamp");
			e.printStackTrace();
		}
		//DateFormat df = DateFormat.getInstance();
		Timestamp ret = new Timestamp(d.getTime());
		return ret;
		
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
	/*
	 * 根据target_id 获取UserTargetOrganizeDto
	 */
	@Override
	public UserTargetOrgnaizeDto getUTOByTargetID(String targetID) {
		UserTargetOrgnaizeDto uto = new UserTargetOrgnaizeDto();
	    Connection conn = this.getConnection();
		try {
			 Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(
						"SELECT * FROM T_UserTargetOrgnaize" +
						" Where target_id = \'" + targetID + "\'");
			while(rs.next()){
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
			}
			closeStmt(stmt);
			close(conn);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return uto;
	}
	/*
	 * 根据List<target_id> 获取UserTargetOrganizeDto
	 */
	@Override
	public List<UserTargetOrgnaizeDto> getUTOByTargetIDs(List<String> targetIDs) {
		List<UserTargetOrgnaizeDto> utos = 
				new ArrayList<UserTargetOrgnaizeDto>();
		Connection conn = this.getConnection();
		Iterator<String> it = targetIDs.iterator();
		try {
			PreparedStatement ps = conn.prepareStatement(
					"SELECT * FROM T_UserTargetOrgnaize" +
					" Where target_id = ? ");
			while(it.hasNext()){
				String targetID = it.next();
				ps.setString(1, targetID);
				ResultSet rs = ps.executeQuery();
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
			}
			closeStmt(ps);
			close(conn);
		} catch (SQLException e) {
			logger.error("error occured when executing sql");
			e.printStackTrace();
		}
		return utos;
	}
}