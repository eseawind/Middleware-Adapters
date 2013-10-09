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
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

/**
 * @author ZhangMin.name
 *
 */
public abstract class AbstractAlert {
	private static final Log logger = LogFactory.getLog(AbstractAlert.class);
	protected SimpleDriverDataSource dataSource;
	protected Connection conn;
	public void setDataSource(SimpleDriverDataSource sdds){
		this.dataSource = sdds;
	}
	public Connection getConnection(){
		if(conn == null){
			try {
				conn = dataSource.getConnection();
			} catch (SQLException e) {
				logger.error("can not get connection from dataSource");
			}
		}
		return conn;
	}
	abstract public void runAlertJob();
	public ResultSet exeuteSQL(String sql){
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
		}
		return rs;
	}
	public void close(){
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
}
