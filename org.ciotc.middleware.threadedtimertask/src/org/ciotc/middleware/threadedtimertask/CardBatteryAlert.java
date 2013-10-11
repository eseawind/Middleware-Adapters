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
import org.ciotc.middleware.threadedtimertask.dao.TargetInfoDto;

/**
 * @author ZhangMin.name
 *
 */
public class CardBatteryAlert extends AbstractAlert{
	protected static final Log logger = LogFactory.getLog(CardBatteryAlert.class);
	@Override
	public void runAlertJob() {
		logger.info("CardBatteryAlert job started...");
		List<TargetInfoDto> targets = sad.getTargetsInfoByLBSTraceTable();
		List<String> alertTargets = new ArrayList<String>();
		Iterator<TargetInfoDto> it = targets.iterator();
		while(it.hasNext()){
			TargetInfoDto ttd = it.next();
			int usetime = ttd.getTargetUseTime();
			String bat = ttd.getBattery();
			String target = ttd.getTargetID();
			int avgtime = sad.getBatteryLifeByID(bat);
			if(usetime > avgtime){
				alertTargets.add(target);
			}
		}
		//TODO remove after test
		Map<String,Integer> targetToUser = sad.getTargetUserByTargetID(alertTargets);
		Set<String> users = targetToUser.keySet();
		Iterator<String> it1 = users.iterator();
		while(it1.hasNext()){
			String target = it1.next();
			int user = targetToUser.get(target);
			logger.info("[ExpireCardInfo] target_id:" + target +
					"user_id:" + user);	
		}
		//sad.alarm(2, 1, targetToUsers);
		
	}

}
