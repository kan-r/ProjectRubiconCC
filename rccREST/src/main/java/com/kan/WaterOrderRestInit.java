package com.kan;

import java.util.Timer;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kan.service.OrderService;
import com.kan.task.OrderDeliveryTask;

@Component
public class WaterOrderRestInit {

	Logger logger = LogManager.getLogger(WaterOrderRestInit.class);
	
	static final int delayMilliSec = 1000;
	static final int periodMilliSec = 10000;
	
	@Autowired
	private OrderService orderService;
	
	@PostConstruct
    public void init() {
		logger.debug("Initialising the app...");
		
		// in case anything goes wrong
		try {
			//Starting the Order Delivery Task
			new Timer().schedule(new OrderDeliveryTask(orderService), delayMilliSec, periodMilliSec);
			
		}catch(Exception e) {
			logger.error(e.getMessage());
		}
    }
}
