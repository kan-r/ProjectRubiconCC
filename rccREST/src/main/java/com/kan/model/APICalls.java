package com.kan.model;

public class APICalls {
	
	private String Load_orders = "GET /orders";
	private String Load_orders_for_a_farm = "GET /orders?farmID={farmID}";
	private String Load_an_order = "GET /orders?orderNo={orderNo}";
	private String Create_an_order = "POST /orders";
	private String Cancel_an_order = "PUT /orders/{orderNo}";
	

	public String getLoad_orders() {
		return Load_orders;
	}

	public void setLoad_orders(String load_orders) {
		Load_orders = load_orders;
	}

	public String getLoad_orders_for_a_farm() {
		return Load_orders_for_a_farm;
	}

	public void setLoad_orders_for_a_farm(String load_orders_for_a_farm) {
		Load_orders_for_a_farm = load_orders_for_a_farm;
	}

	public String getLoad_an_order() {
		return Load_an_order;
	}

	public void setLoad_an_order(String load_an_order) {
		Load_an_order = load_an_order;
	}

	public String getCreate_an_order() {
		return Create_an_order;
	}

	public void setCreate_an_order(String create_an_order) {
		Create_an_order = create_an_order;
	}

	public String getCancel_an_order() {
		return Cancel_an_order;
	}

	public void setCancel_an_order(String cancel_an_order) {
		Cancel_an_order = cancel_an_order;
	}

	@Override
	public String toString() {
		return "{\"APICalls\":"
					+ "{\"Load_orders\":\"" + Load_orders 
						+ "\", \"Load_orders_for_a_farm\":\"" + Load_orders_for_a_farm
						+ "\", \"Load_an_order\":\"" + Load_an_order 
						+ "\", \"Create_an_order\":\"" + Create_an_order 
						+ "\", \"Cancel_an_order\":\"" + Cancel_an_order 
					+ "\"}"
				+ "}";
	}
}
