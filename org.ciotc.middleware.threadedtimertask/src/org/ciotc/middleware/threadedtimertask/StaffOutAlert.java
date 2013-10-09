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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * @author ZhangMin.name
 *
 */
public class StaffOutAlert extends AbstractAlert{
	protected static final Log logger = LogFactory.getLog(StaffOutAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("StaffOutAlert started...");
		List<Integer> outAntenna = new ArrayList<Integer>();
		List<String> targets = new ArrayList<String>();
		List<Integer> users = new ArrayList<Integer>();
	   //获取大楼出口的天线id TODO 大楼出口devicetype_id暂定为5
	   ResultSet rs = exeuteSQL(
			   "SELECT antenna_id FROM t_antenna WHERE devicetype_id = 5");
	  
	   try {
		   while(rs.next()){
			   outAntenna.add(Integer.parseInt(rs.getString(1)));
		   }
		   //获取出楼宇的target_id和user_id
		   PreparedStatement stmt = conn.prepareStatement(
				   "SELECT target_id,user_id FROM t_targetmanager "+
				   "WHERE target_id IN (SELECT DISTINCT target_id "+
				   "FROM t_lbsdata WHERE elflag = 1 and antenna_id = ?)");
		   
		   Iterator it = outAntenna.iterator();
		   while(it.hasNext()){
			   stmt.setObject(1, it.next());
			   rs = stmt.executeQuery();
			   while(rs.next()){
				   targets.add(rs.getString(1));
				   users.add(rs.getInt(2));
			   }
		   }
		   Iterator it1 = targets.iterator();
		   Iterator it2 = users.iterator();
		   while(it1.hasNext() && it2.hasNext()){
			   logger.info("StaffoutAlert:targetID: " + it1.next() + " userID: " + it2.next());
		   }
		   
		   close();
	   } catch (SQLException e) {
		   logger.error("Error in retrieve data from ResultSet");
		   e.printStackTrace();
	   }
	}
	

}
