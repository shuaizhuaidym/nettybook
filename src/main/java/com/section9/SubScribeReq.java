package com.section9;

import java.io.Serializable;

public class SubScribeReq implements Serializable {

	private static final long serialVersionUID = 4902822056656621400L;
	private int subReqID;
	private String userName;
	private String productName;
	private String phoneNumber;
	private String address;

	public SubScribeReq() {
		super();
	}

	public SubScribeReq(int subReqID, String userName, String productName, String phoneNumber, String address) {
		super();
		this.subReqID = subReqID;
		this.userName = userName;
		this.productName = productName;
		this.phoneNumber = phoneNumber;
		this.address = address;
	}

	public int getSubReqID() {
		return subReqID;
	}

	public void setSubReqID(int subReqID) {
		this.subReqID = subReqID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return new StringBuilder(super.toString()).append("[subReqID:").append(subReqID).append(",userName:")
				.append(userName).append(",productName:").append(productName).append(",phoneNumber:")
				.append(phoneNumber).append(",address:").append(address).append("]").toString();
	}
}
