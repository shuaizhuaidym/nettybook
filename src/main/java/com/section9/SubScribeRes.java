package com.section9;

import java.io.Serializable;

public class SubScribeRes implements Serializable {

	private static final long serialVersionUID = -3619590117268217675L;

	private int subReqID;

	private int respCode;

	private String desc;

	public SubScribeRes() {
		super();
	}

	public SubScribeRes(int subReqID, int respCode, String desc) {
		super();
		this.subReqID = subReqID;
		this.respCode = respCode;
		this.desc = desc;
	}

	public int getSubReqID() {
		return subReqID;
	}

	public void setSubReqID(int subReqID) {
		this.subReqID = subReqID;
	}

	public int getRespCode() {
		return respCode;
	}

	public void setRespCode(int respCode) {
		this.respCode = respCode;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return new StringBuilder(super.toString()).append("[subReqID:").append(subReqID).append(",respCode:")
				.append(respCode).append(",desc:").append(desc).append("]").toString();
	}
}
