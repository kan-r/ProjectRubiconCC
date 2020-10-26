package com.kan.model;

public enum Status {
	
	Requested("Order has been placed but not yet delivered"), 
	InProgress("Order is being delivered right now"), 
	Delivered("Order has been delivered"), 
	Cancelled("Order was cancelled before delivery");
	
	private String statusDesc;
	
	private Status(final String statusDesc) {
		this.statusDesc = statusDesc;
	}
	
	public String getStatusDesc() {
		return statusDesc;
	}
}
