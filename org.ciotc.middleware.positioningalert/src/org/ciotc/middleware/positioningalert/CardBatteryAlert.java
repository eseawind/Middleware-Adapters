/**
 *
 * CardBatteryAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.positioningalert;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author ZhangMin.name
 *
 */
public class CardBatteryAlert extends AbstractAlert{

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
				rs = this.exeuteSQL(
						"SELECT user_id FROM t_targetmanager WHERE target_id="
						+ it.next());
				users.add(rs.getInt(1));
			}
			//生成告警事件 TODO 调用工具类
			Iterator it1 = targets.iterator();
			Iterator it2 = users.iterator();
			while(it1.hasNext() && it2.hasNext()){
				 logger.info("targetID: " + it1.next() + " userID: " + it2.next());
			}
			   
			close();
			
		}catch(SQLException e){
			logger.error("error in checking card battery");
			e.printStackTrace();
		}
		
		
	}

}
