package com.kan.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Sort;

import com.kan.model.Order;

public interface OrderDao extends JpaRepository<Order, String>{

	public Order findByOrderNo(int orderNo);
	public List<Order> findByFarmID(String farmID, Sort sort);
}
