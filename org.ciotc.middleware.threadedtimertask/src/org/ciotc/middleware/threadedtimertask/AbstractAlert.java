/**
 *
 * AbstractAlert.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.threadedtimertask;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciotc.middleware.threadedtimertask.dao.StaffAlertDAO;
import org.ciotc.middleware.threadedtimertask.dao.StaffAlertDAOImpl;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * @author ZhangMin.name
 *
 */
public class AbstractAlert {
	private static final Log logger = LogFactory.getLog(AbstractAlert.class);
	protected StaffAlertDAO staffAlertDAO;
	public void setStaffAlertDAO(StaffAlertDAO staffAlertDAO){
		this.staffAlertDAO = staffAlertDAO;
	}
	public void runAlertJob(){ }
}
