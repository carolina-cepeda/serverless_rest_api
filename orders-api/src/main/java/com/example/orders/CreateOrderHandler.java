package com.example.orders;

import com.amazonaws.services.lambda.runtime.RequestHandler;

public class CreateOrderHandler implements RequestHandler<CreateOrderRequest, Order> {

  private final OrderService orderService = new OrderService();

  @Override
  public Order handleRequest(CreateOrderRequest request, com.amazonaws.services.lambda.runtime.Context context) {
    return orderService.createOrder(request);
  }
}
