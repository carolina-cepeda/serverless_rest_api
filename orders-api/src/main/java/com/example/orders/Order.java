package com.example.orders;

public class Order {
  private String id;
  private String customerId;
  private String product;
  private int quantity;

  public Order() {
  }

  public Order(String id, String customerId, String product, int quantity) {
    this.id = id;
    this.customerId = customerId;
    this.product = product;
    this.quantity = quantity;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return "Order{id='" + id + "', customerId='" + customerId + "', product='" + product + "', quantity=" + quantity + "}";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return quantity == order.quantity && id.equals(order.id) && customerId.equals(order.customerId) && product.equals(order.product);
  }

  @Override
  public int hashCode() {
    int result = id.hashCode();
    result = 31 * result + customerId.hashCode();
    result = 31 * result + product.hashCode();
    result = 31 * result + quantity;
    return result;
  }
}
