/**
 *
 * CardBatteryAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.threadedtimertask;
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
public class CardBatteryAlert extends AbstractAlert{
	protected static final Log logger = LogFactory.getLog(CardBatteryAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("CardBatteryAlert job started...");
		List targets = new ArrayList<String>();
		List users = new ArrayList<Integer>();
		ResultSet rs = this.exeuteSQL(
				"SELECT target_id,target_usetime,battery_id FROM t_target "+
				"WHERE target_id IN (SELECT target_id FROM t_lbsdata)");
		try{
			while(rs.next()){
				int usetime = rs.getInt(2);
				String bat = rs.getString(3);
				String target = rs.getString(1);
				ResultSet rs1 = this.exeuteSQL(
						"SELECT avgtime FROM t_battery WHERE battery_id = \'"
						+ bat + "\'");
				rs1.next();
				if(usetime > rs1.getInt(1)){
					targets.add(target);
				}
			}
			Iterator<String> it = targets.iterator();
			while(it.hasNext()){
				ResultSet rs1 = this.exeuteSQL(
				    "SELECT DISTINCT user_id FROM t_targetmanager WHERE target_id=\'"
					+ it.next() + "\'");
				while(rs1.next()){
					 users.add(rs1.getInt(1));
				}
			}
			Iterator it1 = targets.iterator();
			Iterator it2 = users.iterator();
			while(it1.hasNext() && it2.hasNext()){
				 logger.info("CardBatteryAlert:targetID: " + it1.next() + 
						 " userID: " + it2.next());
			}
			   
			close();
			
		}catch(SQLException e){
			logger.error("error in checking card battery");
			e.printStackTrace();
		}
		
		
	}

}
