package com.detty;

public enum MessageType {
	LOGIN_REQ, LOGIN_RESP,HEART_BEAT_REQ,HEART_BEAT_RESP;
	public byte value() {
		return Byte.valueOf("121");
	}
}
