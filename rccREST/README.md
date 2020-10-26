# ProjectWaterOrderAPI
	REST API's to manage water orders for farms

	This provides following API calls
		GET		/orders
				/orders?farmID={farmID}
				/orders?orderNo={orderNo}
		POST		/orders
		PUT		/orders/{orderNo}
		
	This is developed using Java and Spring Boot
	This project is set up to build as jar with embedded server (port: 8082)