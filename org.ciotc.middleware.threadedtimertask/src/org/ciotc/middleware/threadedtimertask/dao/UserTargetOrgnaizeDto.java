package org.ciotc.middleware.threadedtimertask.dao;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * t_usertargetorgaize 属性封装类
 * @author ZhangMin.name
 *
 */
public class UserTargetOrgnaizeDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private int userID;
	private int organizeID;
	private String targetID;
	private String targetCode;
	private Timestamp validDate;
	private int distributeStatus;
	private Timestamp distributeTime;
	private int recycleStatus;
	private Timestamp recycleTime;
	private int version;
	private String reason;
	private int operaterID;
	private int usertypeID;
	
	public UserTargetOrgnaizeDto() {
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public int getOrganizeID() {
		return organizeID;
	}

	public void setOrganizeID(int organizeID) {
		this.organizeID = organizeID;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public Timestamp getValidDate() {
		return validDate;
	}

	public void setValidDate(Timestamp validDate) {
		this.validDate = validDate;
	}

	public int getDistributeStatus() {
		return distributeStatus;
	}

	public void setDistributeStatus(int distributeStatus) {
		this.distributeStatus = distributeStatus;
	}

	public Timestamp getDistributeTime() {
		return distributeTime;
	}

	public void setDistributeTime(Timestamp distributeTime) {
		this.distributeTime = distributeTime;
	}

	public int getRecycleStatus() {
		return recycleStatus;
	}

	public void setRecycleStatus(int recycleStatus) {
		this.recycleStatus = recycleStatus;
	}

	public Timestamp getRecycleTime() {
		return recycleTime;
	}

	public void setRecycleTime(Timestamp recycleTime) {
		this.recycleTime = recycleTime;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getOperaterID() {
		return operaterID;
	}

	public void setOperaterID(int operaterID) {
		this.operaterID = operaterID;
	}

	public int getUsertypeID() {
		return usertypeID;
	}

	public void setUsertypeID(int usertypeID) {
		this.usertypeID = usertypeID;
	}

}
