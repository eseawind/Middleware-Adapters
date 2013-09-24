/**
 *
 * DBTester.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.positioningalert;

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
public class DBTester {
	private static final Log logger = LogFactory.getLog(DBTester.class);
	private SimpleDriverDataSource dataSource;
	
	public void setDataSource(SimpleDriverDataSource sdds){
		this.dataSource = sdds;
	}
	public void testSQL(){
		Connection conn = null;
		if(dataSource == null){
			logger.error("dataSource can not be null");
		}
		try {
			conn = dataSource.getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from t_lbsdata");
			while(rs.next()){
				logger.info("area id: "+ rs.getInt("area_id"));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
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
	public void finished(){
		logger.info("DBTester finished");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
