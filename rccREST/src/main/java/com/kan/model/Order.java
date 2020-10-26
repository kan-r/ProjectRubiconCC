package com.kan.model;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="ORDERS") /* prefer ORDER, but it is Key Word in SQL */
public class Order {
	
	@Id
	@GeneratedValue()
	private int orderNo;
	
	private String farmID;
	private String startDate;
	private Double duration;
	
	@Enumerated(EnumType.STRING)
	private Status status;
	
	@Transient
	private String statusDesc;

	public Order() {
		
	}
	
	public Order(String farmID, String startDate, Double duration) {
		super();
		
		this.farmID = farmID;
		this.startDate = startDate;
		this.duration = duration;
	}
	
	public int getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(int orderNo) {
		this.orderNo = orderNo;
	}
	public String getFarmID() {
		return farmID;
	}
	public void setFarmID(String farmID) {
		this.farmID = farmID;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public Double getDuration() {
		return duration;
	}
	public void setDuration(Double duration) {
		this.duration = duration;
	}
	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public String getStatusDesc() {
		return status.getStatusDesc();
	}
	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	@Override
	public String toString() {
		return "Order [orderNo=" + orderNo + ", farmID=" + farmID + ", startDate=" + startDate + ", duration="
				+ duration + ", status=" + status + "]";
	}
}
