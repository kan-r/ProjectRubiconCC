package com.kan.task;

import java.util.TimerTask;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.kan.service.OrderService;


public class OrderDeliveryTask extends TimerTask{
	
	Logger logger = LogManager.getLogger(OrderDeliveryTask.class);
	
	private OrderService orderService;
	
	public OrderDeliveryTask() {
		
	}
	
	public OrderDeliveryTask(OrderService orderService) {
		this.orderService = orderService;
	}

	@Override
	public void run() {
		logger.debug("Running Order Delivery Task...");
		
		try {
			orderService.startAndStopOrderDelivery();
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
}
