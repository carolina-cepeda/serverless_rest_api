package com.example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class OrderServiceTest {

  private final OrderService service = new OrderService();

  @Test
  void createValidOrder() {
    CreateOrderRequest request = new CreateOrderRequest("CUST-42", "MacBook Air", 1);
    Order order = service.createOrder(request);

    assertEquals("ORD-001", order.getId());
    assertEquals("CUST-42", order.getCustomerId());
    assertEquals("MacBook Air", order.getProduct());
    assertEquals(1, order.getQuantity());
  }

  @Test
  void retrieveExistingOrder() {
    service.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));
    Order order = service.getOrder("ORD-001");

    assertEquals("ORD-001", order.getId());
    assertEquals("CUST-42", order.getCustomerId());
  }

  @Test
  void orderDoesNotExist() {
    OrderNotFoundException ex = assertThrows(OrderNotFoundException.class, () -> service.getOrder("ORD-999"));
    assertTrue(ex.getMessage().contains("ORD-999"));
  }

  @Test
  void findOrdersByCustomer() {
    service.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 1));
    service.createOrder(new CreateOrderRequest("CUST-42", "Keyboard", 2));
    service.createOrder(new CreateOrderRequest("CUST-100", "Mouse", 1));

    List<Order> orders = service.getOrdersByCustomer("CUST-42");

    assertEquals(2, orders.size());
    assertTrue(orders.stream().allMatch(o -> "CUST-42".equals(o.getCustomerId())));
  }

  @Test
  void findOrdersByCustomerEmpty() {
    List<Order> orders = service.getOrdersByCustomer("CUST-NOPE");
    assertEquals(0, orders.size());
  }

  @Test
  void invalidCustomerId() {
    assertThrows(IllegalArgumentException.class,
        () -> service.createOrder(new CreateOrderRequest("", "MacBook Air", 1)));
  }

  @Test
  void invalidProduct() {
    assertThrows(IllegalArgumentException.class,
        () -> service.createOrder(new CreateOrderRequest("CUST-42", "", 1)));
  }

  @Test
  void invalidQuantity() {
    assertThrows(IllegalArgumentException.class,
        () -> service.createOrder(new CreateOrderRequest("CUST-42", "MacBook Air", 0)));
  }

  @Test
  void nullRequest() {
    assertThrows(IllegalArgumentException.class, () -> service.createOrder(null));
  }
}
