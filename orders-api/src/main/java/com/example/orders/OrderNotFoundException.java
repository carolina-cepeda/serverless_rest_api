package com.example.orders;

public class OrderNotFoundException extends RuntimeException {
  public OrderNotFoundException(String orderId) {
    super("Order " + orderId + " does not exist");
  }
}
