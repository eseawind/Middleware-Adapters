/**
 *
 * EnterLeaveInfoDto.java
 * ZhangMin.name - zhangmin@zhangmin.name
 * org.ciotc.middleware.adapter.positioning
 *
 */
package org.ciotc.middleware.adapter.positioning.pojo;

import java.sql.Timestamp;

/**
 * @author ZhangMin.name
 *
 */
public class EnterLeaveInfoDto {
	private int user_id;
	private int organize_id;
	private String target_id;
	private String target_code;
	private Timestamp validdate;
	private int distributestatue;
	private Timestamp distributetime;
	private int recyclestatue;
	private Timestamp recycletime;
	private int eltype;
	private Timestamp eltime;
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}
	public int getOrganize_id() {
		return organize_id;
	}
	public void setOrganize_id(int organize_id) {
		this.organize_id = organize_id;
	}
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}
	public String getTarget_code() {
		return target_code;
	}
	public void setTarget_code(String target_code) {
		this.target_code = target_code;
	}
	public Timestamp getValiddate() {
		return validdate;
	}
	public void setValiddate(Timestamp validdate) {
		this.validdate = validdate;
	}
	public int getDistributestatue() {
		return distributestatue;
	}
	public void setDistributestatue(int distributestatue) {
		this.distributestatue = distributestatue;
	}
	public Timestamp getDistributetime() {
		return distributetime;
	}
	public void setDistributetime(Timestamp distributetime) {
		this.distributetime = distributetime;
	}
	public int getRecyclestatue() {
		return recyclestatue;
	}
	public void setRecyclestatue(int recyclestatue) {
		this.recyclestatue = recyclestatue;
	}
	public Timestamp getRecycletime() {
		return recycletime;
	}
	public void setRecycletime(Timestamp recycletime) {
		this.recycletime = recycletime;
	}
	public int getEltype() {
		return eltype;
	}
	public void setEltype(int eltype) {
		this.eltype = eltype;
	}
	public Timestamp getEltime() {
		return eltime;
	}
	public void setEltime(Timestamp eltime) {
		this.eltime = eltime;
	}
	
}
