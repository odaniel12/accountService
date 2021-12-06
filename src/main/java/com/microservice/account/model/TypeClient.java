package com.microservice.account.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TypeClient {
	
	private String typeclient;

	public String getTypeclient() {
		return typeclient;
	}

	public void setTypeclient(String typeclient) {
		this.typeclient = typeclient;
	}
	
}
