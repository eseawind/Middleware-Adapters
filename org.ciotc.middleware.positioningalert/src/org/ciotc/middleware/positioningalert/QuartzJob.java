/**
 *
 * QuartzJob.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.positioningalert
 *
 */
package org.ciotc.middleware.positioningalert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author ZhangMin.name
 *
 */
public class QuartzJob {
	private static final Log logger = LogFactory.getLog(QuartzJob.class);
	public void doJob(){
		logger.info("Quartz job started ...");
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
