/**
 *
 * TracingTargetDto.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.threadedtimertask
 *
 */
package org.ciotc.middleware.threadedtimertask.dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * t_lbstracedata 属性封装类
 * @author ZhangMin.name
 *
 */
public class TracingTargetDto implements Serializable{
	private String targetID;
	private int areaID;
	private int elFlag;
	private Timestamp elTime;
	private int userID;
	public String getTargetID() {
		return targetID;
	}
	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}
	public int getAreaID() {
		return areaID;
	}
	public void setAreaID(int areaID) {
		this.areaID = areaID;
	}
	public int getElFlag() {
		return elFlag;
	}
	public void setElFlag(int elFlag) {
		this.elFlag = elFlag;
	}
	public Timestamp getElTime() {
		return elTime;
	}
	public void setElTime(Timestamp elTime) {
		this.elTime = elTime;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	
}
