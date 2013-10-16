package org.ciotc.middleware.adapter.positioning.pojo;

import java.io.Serializable;
/**
 * t_target 属性封装类
 * @author ZhangMin.name
 *
 */
public class TargetInfoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String targetID;
	private String targetBarCode;
	private int targetUseStatus;
	private int targetStatus;
	private int targetUseTime;
	private String remark;
	private String battery;
	
	public TargetInfoDto() {
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public String getTargetBarCode() {
		return targetBarCode;
	}

	public void setTargetBarCode(String targetBarCode) {
		this.targetBarCode = targetBarCode;
	}

	public int getTargetUseStatus() {
		return targetUseStatus;
	}

	public void setTargetUseStatus(int targetUseStatus) {
		this.targetUseStatus = targetUseStatus;
	}

	public int getTargetStatus() {
		return targetStatus;
	}

	public void setTargetStatus(int targetStatus) {
		this.targetStatus = targetStatus;
	}

	public int getTargetUseTime() {
		return targetUseTime;
	}

	public void setTargetUseTime(int targetUseTime) {
		this.targetUseTime = targetUseTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}

}
