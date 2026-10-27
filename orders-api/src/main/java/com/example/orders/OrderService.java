package com.example.orders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class OrderService {
  private final Map<String, Order> orders = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public Order createOrder(CreateOrderRequest request) {
    validate(request);
    String id = "ORD-" + String.format("%03d", idGenerator.getAndIncrement());
    Order order = new Order(id, request.getCustomerId(), request.getProduct(), request.getQuantity());
    orders.put(id, order);
    return order;
  }

  public Order getOrder(String id) {
    Order order = orders.get(id);
    if (order == null) {
      throw new OrderNotFoundException(id);
    }
    return order;
  }

  public List<Order> getOrdersByCustomer(String customerId) {
    List<Order> result = new ArrayList<>();
    for (Order order : orders.values()) {
      if (customerId.equals(order.getCustomerId())) {
        result.add(order);
      }
    }
    return result;
  }

  public boolean deleteOrder(String id) {
    if (!orders.containsKey(id)) {
      return false;
    }
    orders.remove(id);
    return true;
  }

  private void validate(CreateOrderRequest request) {
    if (request == null) {
      throw new IllegalArgumentException("Request body is required");
    }
    if (request.getCustomerId() == null || request.getCustomerId().isBlank()) {
      throw new IllegalArgumentException("customerId is required");
    }
    if (request.getProduct() == null || request.getProduct().isBlank()) {
      throw new IllegalArgumentException("product is required");
    }
    if (request.getQuantity() <= 0) {
      throw new IllegalArgumentException("quantity must be greater than 0");
    }
  }
}
