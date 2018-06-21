package com.xml.bean;

public class Address {
	private String street1, street2, state, city, country, postCode;

	public Address() {
		super();
	}

	public Address(String street1, String street2, String state, String city, String country,
			String postCode) {
		super();
		this.street1 = street1;
		this.street2 = street2;
		this.state = state;
		this.city = city;
		this.country = country;
		this.postCode = postCode;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPost_code() {
		return postCode;
	}

	public void setPost_code(String post_code) {
		this.postCode = post_code;
	}

}
