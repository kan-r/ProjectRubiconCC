package com.kan.service;

import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kan.dao.OrderDao;
import com.kan.exception.InvalidDataException;
import com.kan.model.Order;
import com.kan.util.GenUtils;
import com.kan.model.Status;

@Service
@Transactional
public class OrderService {
	
	private Logger logger = LogManager.getLogger(OrderService.class);
	
	@Autowired
	private OrderDao orderDao;
	
	public List<Order> queryOrders(){
		logger.debug("queryOrders()");
		
		return orderDao.findAll(Sort.by("orderNo"));
	}
	
	public List<Order> queryOrders(String farmID){
		logger.debug("queryOrders({})", farmID);
		
		if(farmID == null) {
			farmID = "";
		}
		
		return orderDao.findByFarmID(farmID.toUpperCase(), Sort.by("orderNo"));
	}
	
	public Order queryOrder(int orderNo) {
		logger.debug("queryOrder({})", orderNo);
		
		return orderDao.findByOrderNo(orderNo);
	}
	
	public Order placeOrder(Order order) throws InvalidDataException {
		logger.debug("placeOrder({})", order);
		
		validateOrder(order);
		
		//farmID is always upper case
		order.setFarmID(order.getFarmID().toUpperCase());
		order.setStatus(Status.Requested);
		
		Order ord = orderDao.save(order);
		
		logger.info("Order #{} - New water order for farm {} created", ord.getOrderNo(), ord.getFarmID());
		
		return ord;
	}
	
	public Order updateOrderStatus(int orderNo, Status status) throws InvalidDataException {
		logger.debug("updateOrderStatus({}, {})", orderNo, status);
		
		Order order = queryOrder(orderNo);
		if(order == null) {
			throw new InvalidDataException("Order (#" + orderNo + ") does not exist");
		}
		
		if(Status.Cancelled == status && Status.Delivered == order.getStatus()) {
			throw new InvalidDataException("Order (#" + orderNo + ") has been already delivered, cannot be cancelled");
		}
		
		order.setStatus(status);
		
		return orderDao.save(order);
	}
	
	public Order cancelOrder(int orderNo) throws InvalidDataException {
		logger.debug("cancelOrder({})", orderNo);
		
		Order order = updateOrderStatus(orderNo, Status.Cancelled);
		logger.info("Order #{} - Water order for farm {} cancelled", order.getOrderNo(), order.getFarmID());
		
		return order;
	}
	
	public void startOrderDelivery(Order order) {
		logger.debug("startOrderDelivery({})", order);
		
		try {
			updateOrderStatus(order.getOrderNo(), Status.InProgress);
			logger.info("Order #{} - Water delivery to farm {} started", order.getOrderNo(), order.getFarmID());
			
		} catch (InvalidDataException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void stopOrderDelivery(Order order) {
		logger.debug("stopOrderDelivery({})", order);
		
		try {
			updateOrderStatus(order.getOrderNo(), Status.Delivered);
			logger.info("Order #{} - Water delivery to farm {} stopped", order.getOrderNo(), order.getFarmID());
			
		} catch (InvalidDataException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void startAndStopOrderDelivery() {
		logger.debug("startAndStopOrderDelivery()");
		
		List<Order> orders = queryOrders();
		
		for(Order ord : orders) {
			if(ord.getStatus() == Status.Cancelled) {
				continue; 
			}
			
			if(ord.getStatus() == Status.Delivered) {
				continue;
			}
			
			Date startDate = GenUtils.toDate(ord.getStartDate());
			Date endDate = GenUtils.addHoursToDate(startDate, ord.getDuration());
	
			if(endDate.before(new Date())) {
				stopOrderDelivery(ord);
			}else {
				if(ord.getStatus() == Status.InProgress) {
					continue;
				}
		
				if(startDate.before(new Date())) {
					startOrderDelivery(ord);
				}
			}
		}
	}
	
	public void validateOrder(Order order) throws InvalidDataException {
		logger.debug("validateOrder({})", order);
		
		if(order.getFarmID() == null || order.getFarmID().trim().isEmpty()) {
			throw new InvalidDataException("Farm ID is required");
		}
		
		if(order.getStartDate() == null || order.getStartDate().trim().isEmpty()) {
			throw new InvalidDataException("Start Date is required");
		}
		
		if(GenUtils.toDate(order.getStartDate()) == null) {
			throw new InvalidDataException("Invalid date, date format is " + GenUtils.C_DATE_FORMAT);
		}
		
		if(GenUtils.toDate(order.getStartDate()).before(new Date())) {
			throw new InvalidDataException("Start Date must be later than presnt time");
		}
		
		if(order.getDuration() <= 0) {
			throw new InvalidDataException("Duration is required");
		}
		
		Date startDate = GenUtils.toDate(order.getStartDate());
		Date endDate = GenUtils.addHoursToDate(startDate, order.getDuration());
		
		//Check for overlap
		List<Order> orders = queryOrders(order.getFarmID());
		for(Order ord : orders) {
			// if different farm, no check
			if(!ord.getFarmID().equals(order.getFarmID())) {
				continue;
			}
			
			Date startDate2 = GenUtils.toDate(ord.getStartDate());
			Date endDate2 = GenUtils.addHoursToDate(startDate2, ord.getDuration());
			
			if(GenUtils.isDateBetween(startDate, startDate2, endDate2)) {
				throw new InvalidDataException("This order overlaps with the order #" + ord.getOrderNo());
			}
			
			if(GenUtils.isDateBetween(endDate, startDate2, endDate2)) {
				throw new InvalidDataException("This order overlaps with the order #" + ord.getOrderNo());
			}
		}
	}
}
