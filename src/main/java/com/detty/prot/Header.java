package com.detty.prot;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Title:Header
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author daiyma
 * @date 2016年7月17日
 */
public final class Header {
	private int code = 0xabef001;
	private int length;
	private long sessionID;
	private byte type;
	private byte priority;
	private Map<String, Object> attachment = new HashMap<String, Object>();

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public long getSessionID() {
		return sessionID;
	}

	public void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}

	public byte getType() {
		return type;
	}

	public void setType(byte type) {
		this.type = type;
	}

	public byte getPriority() {
		return priority;
	}

	public void setPriority(byte priority) {
		this.priority = priority;
	}

	public Map<String, Object> getAttachment() {
		return attachment;
	}

	public void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}

	public String toString() {
		return "Header [crcCode=" + code + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type
				+ ", priority=" + priority + ", attachment=" + attachment + "]";
	}
}
