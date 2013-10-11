/**
 *
 * CardExpireAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.threadedtimertask;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ZhangMin.name
 *
 */
public class CardExpireAlert extends AbstractAlert {
	private static final Log logger = LogFactory.getLog(CardExpireAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("CardExpireAlert job started...");
		HashMap<String,Integer> userToTarget = new HashMap<String,Integer>();
		ResultSet rs = this.exeuteSQL(
			"SELECT validdate,user_id,target_id FROM T_UserTargetOrgnaize" +
	        " Where target_id IN (SELECT DISTINCT target_id FROM T_LBSTraceData)" 
		    );
		try{
		    while(rs.next()){
		    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    	Date validDate = df.parse(rs.getString("validdate"));
		    	if(validDate.before(new Date())){
		    		userToTarget.put(rs.getString("user_id"), rs.getInt("target_id"));
		    	}
		    }
			
			Set<String> users = userToTarget.keySet();
			Iterator<String> it = users.iterator();
			while(it.hasNext()){
				String user = it.next();
				logger.info("[ExpireCardInfo] user_id:" + user +
						"target_id:" + userToTarget.get(user));
			}
			  
			close();
			
		}catch(SQLException e){
			logger.error("error in checking card expire job");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args){
		
	}

}
