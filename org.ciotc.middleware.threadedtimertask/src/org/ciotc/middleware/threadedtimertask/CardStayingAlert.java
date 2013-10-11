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
public class CardStayingAlert extends AbstractAlert {
	private static final Log logger = LogFactory.getLog(CardStayingAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("CardStayingAlert job started...");
		int interval = 30 * 60 * 1000;
		HashMap<Integer,String> userToTarget = new HashMap<Integer,String>();
		ResultSet rs = this.exeuteSQL(
			"SELECT eltime,user_id,target_id FROM t_lbstracedata");
		try{
			while(rs.next()){
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date eltime = df.parse(rs.getString("eltime"));
				if((System.currentTimeMillis() - eltime.getTime()) > interval){
					userToTarget.put(rs.getInt("user_id"), rs.getString("target_id"));
				}
			}
			Set<Integer> users = userToTarget.keySet();
			Iterator<Integer> it = users.iterator();
			while(it.hasNext()){
				Integer user = it.next();
				logger.info("[CardStayingAlertInfo] user_id:" + user +
						"target_id:" + userToTarget.get(user));
			}
			  
			close();
			
		}catch(SQLException e){
			logger.error("error in checking card battery");
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
