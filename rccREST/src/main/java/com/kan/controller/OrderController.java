package com.kan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kan.exception.InvalidDataException;
import com.kan.model.APICalls;
import com.kan.model.Order;
import com.kan.service.OrderService;

@RestController
public class OrderController {
	
	@Autowired
	private OrderService orderService; 
	
	@GetMapping(path="/")
	public String home() {
		return new APICalls().toString();
	}
	
	@GetMapping(path="/orders")
	public List<Order> queryOrders() {
		return orderService.queryOrders();
	}
	
	@GetMapping(path="/orders", params = "farmID")
	public List<Order> queryOrdersByFarmId(@RequestParam String farmID) {
		return orderService.queryOrders(farmID);
	}
	
	@GetMapping(path="/orders", params = "orderNo")
	public Order queryOrdersByOrderNo(@RequestParam int orderNo) {
		return orderService.queryOrder(orderNo);
	}
	
	@PostMapping("/orders")
	public Order placeOrder(@RequestBody Order order) throws InvalidDataException {
		return orderService.placeOrder(order);
	}
	
	@PutMapping("/orders/{orderNo}")
	public Order cancelOrder(@PathVariable int orderNo) throws InvalidDataException{
		return orderService.cancelOrder(orderNo);
	}
}
