package com.kan;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;

import com.kan.dao.OrderDao;
import com.kan.exception.InvalidDataException;
import com.kan.model.Order;
import com.kan.model.Status;
import com.kan.service.OrderService;
import com.kan.util.GenUtils;

@SpringBootTest
class WaterOrderRestApplicationTests {
	
	@Autowired
	private OrderService orderService;
	
	@MockBean
	private OrderDao orderDao;
	
	@Test
	void queryOrdersTest() {
		String startDate = "18/03/2020 19:30"; 
		
		List<Order> orders = new ArrayList<Order>();
		orders.add( new Order("TEST_1", startDate, 3.0));
		orders.add( new Order("TEST_1", startDate, 4.0));
		orders.add( new Order("TEST_2", startDate, 4.0));
		
		when(orderDao.findAll(Sort.by("orderNo"))).thenReturn(orders);
		assertEquals(3, orderService.queryOrders().size());
	}
	
	@Test
	void queryOrdersByFarmIDTest() {
		String startDate = "18/03/2020 19:30"; 
		
		// with 2
		List<Order> orders = new ArrayList<Order>();
		orders.add( new Order("TEST_1", startDate, 3.0));
		orders.add( new Order("TEST_1", startDate, 4.0));
		
		when(orderDao.findByFarmID("TEST_1", Sort.by("orderNo"))).thenReturn(orders);
		assertEquals(2, orderService.queryOrders("TEST_1").size());
		
		// with 1
		List<Order> orders2 = new ArrayList<Order>();
		orders2.add( new Order("TEST_2", startDate, 4.0));
		
		when(orderDao.findByFarmID("TEST_2", Sort.by("orderNo"))).thenReturn(orders2);
		assertEquals(1, orderService.queryOrders("TEST_2").size());
		
		// with 0
		List<Order> orders3 = new ArrayList<Order>();
		
		when(orderDao.findByFarmID("TEST_3", Sort.by("orderNo"))).thenReturn(orders3);
		assertEquals(0, orderService.queryOrders("TEST_3").size());
	}
	
	@Test
	void queryOrdersByOrderNoTest() {
		String startDate = "18/03/2020 19:30"; 
		Order order = new Order("TEST_1", startDate, 3.0);
		
		when(orderDao.findByOrderNo(1)).thenReturn(order);
		assertEquals(order, orderService.queryOrder(1));
	}
	
	@Test
	void updateOrderStatusTest() {
		// null order
		String err = "";
		when(orderDao.findByOrderNo(1)).thenReturn(null);
	
		try {
			orderService.updateOrderStatus(1, Status.InProgress);
		} catch (InvalidDataException e) {
			err = e.getMessage();
		}
		
		assertFalse(err.isEmpty());
		
		// 1 order
		String startDate = "18/03/2020 19:30"; 
		Order order = new Order("TEST_1", startDate, 3.0);
		order.setStatus( Status.Delivered);
	
		err = "";
		when(orderDao.findByOrderNo(1)).thenReturn(order);
	
		try {
			orderService.updateOrderStatus(1, Status.Cancelled);
		} catch (InvalidDataException e) {
			err = e.getMessage();
		}
		
		assertFalse(err.isEmpty());
	}
	
	@Test
	void addHoursToDateTest() {
		Date startDate = GenUtils.toDate("10/03/2020 12:00"); 
		Date endDate = GenUtils.toDate("10/03/2020 15:30"); 
		
		assertEquals(endDate, GenUtils.addHoursToDate(startDate, 3.5));
		assertNotEquals(endDate, GenUtils.addHoursToDate(startDate, 3));
	}

	@Test
	void isDateBetweenTest() throws ParseException {
		Date startDate = GenUtils.toDate("10/03/2020 12:00");  
		Date endDate = GenUtils.toDate("10/03/2020 15:00");  
	
		// on the left border
		Date dt = (Date) startDate.clone();
		assertTrue(GenUtils.isDateBetween(dt, startDate, endDate));
		
		// on the right border
		dt = (Date) endDate.clone();
		assertTrue(GenUtils.isDateBetween(dt, startDate, endDate));
		
		// in the middle
		dt = GenUtils.toDate("10/03/2020 13:30");  
		assertTrue(GenUtils.isDateBetween(dt, startDate, endDate));
		
		// outside left
		dt = GenUtils.toDate("10/03/2020 11:30");  
		assertFalse(GenUtils.isDateBetween(dt, startDate, endDate));
		
		// outside right
		dt = GenUtils.toDate("10/03/2020 11:30");  
		assertFalse(GenUtils.isDateBetween(dt, startDate, endDate));
	}
	
	@Test
	void validateOrderTest() {
		Order order = new Order("TEST_1", "18/03/2020T19:20", 3.0);
		String err = "";
		
		try {
			orderService.validateOrder(order);
		} catch (InvalidDataException e) {
			err = e.getMessage();
		}
		
		assertFalse(err.isEmpty());
		
		//overlap test
		String startDate = "18/03/2020 19:30"; 
		Order order1 = new Order("TEST_1", startDate, 2.5);
		
		List<Order> orders = new ArrayList<Order>();
		orders.add( new Order("TEST_1", startDate, 3.0));
		orders.add( new Order("TEST_1", startDate, 4.0));
		orders.add( new Order("TEST_2", startDate, 4.0));
		
		when(orderDao.findByFarmID("TEST_1", Sort.by("orderNo"))).thenReturn(orders);
		
		try {
			orderService.validateOrder(order1);
		} catch (InvalidDataException e) {
			err = e.getMessage();
		}
		
		assertFalse(err.isEmpty(), err);
	}

}
