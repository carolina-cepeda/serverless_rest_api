package com.example.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.junit.jupiter.api.Test;

class OrdersLambdaHandlerTest {

  private final OrdersLambdaHandler handler = new OrdersLambdaHandler();
  private final Gson gson = new Gson();

  @Test
  void postCreateOrderReturns201() {
    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    event.setPath("/orders");
    event.setBody("{\"customerId\":\"CUST-42\",\"product\":\"MacBook Air\",\"quantity\":1}");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);

    assertEquals(201, res.getStatusCode());
    JsonObject body = gson.fromJson(res.getBody(), JsonObject.class);
    assertEquals("CUST-42", body.get("customerId").getAsString());
    assertEquals("ORD-001", body.get("id").getAsString());
    assertEquals("/orders/ORD-001", res.getHeaders().get("Location"));
  }

  @Test
  void getExistingOrderReturns200() {
    APIGatewayProxyRequestEvent create = new APIGatewayProxyRequestEvent();
    create.setHttpMethod("POST");
    create.setPath("/orders");
    create.setBody("{\"customerId\":\"CUST-42\",\"product\":\"MacBook Air\",\"quantity\":1}");
    handler.handleRequest(create, null);

    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    event.setPath("/orders/ORD-001");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(200, res.getStatusCode());
    JsonObject body = gson.fromJson(res.getBody(), JsonObject.class);
    assertEquals("ORD-001", body.get("id").getAsString());
  }

  @Test
  void getMissingOrderReturns404() {
    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    event.setPath("/orders/ORD-999");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(404, res.getStatusCode());
    JsonObject body = gson.fromJson(res.getBody(), JsonObject.class);
    assertEquals("ORDER_NOT_FOUND", body.get("error").getAsString());
    assertTrue(body.get("message").getAsString().contains("ORD-999"));
  }

  @Test
  void getOrdersByCustomerReturns200() {
    APIGatewayProxyRequestEvent create1 = new APIGatewayProxyRequestEvent();
    create1.setHttpMethod("POST");
    create1.setPath("/orders");
    create1.setBody("{\"customerId\":\"CUST-42\",\"product\":\"MacBook Air\",\"quantity\":1}");
    handler.handleRequest(create1, null);

    APIGatewayProxyRequestEvent create2 = new APIGatewayProxyRequestEvent();
    create2.setHttpMethod("POST");
    create2.setPath("/orders");
    create2.setBody("{\"customerId\":\"CUST-42\",\"product\":\"Keyboard\",\"quantity\":2}");
    handler.handleRequest(create2, null);

    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("GET");
    event.setPath("/orders");
    event.setQueryStringParameters(java.util.Collections.singletonMap("customerId", "CUST-42"));

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(200, res.getStatusCode());
    assertTrue(res.getBody().contains("CUST-42"));
  }

  @Test
  void postInvalidInputReturns400() {
    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("POST");
    event.setPath("/orders");
    event.setBody("{\"customerId\":\"\",\"product\":\"X\",\"quantity\":0}");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(400, res.getStatusCode());
    JsonObject body = gson.fromJson(res.getBody(), JsonObject.class);
    assertEquals("INVALID_REQUEST", body.get("error").getAsString());
  }

  @Test
  void deleteExistingOrderReturns204() {
    APIGatewayProxyRequestEvent create = new APIGatewayProxyRequestEvent();
    create.setHttpMethod("POST");
    create.setPath("/orders");
    create.setBody("{\"customerId\":\"CUST-42\",\"product\":\"MacBook Air\",\"quantity\":1}");
    handler.handleRequest(create, null);

    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("DELETE");
    event.setPath("/orders/ORD-001");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(204, res.getStatusCode());
  }

  @Test
  void deleteMissingOrderReturns404() {
    APIGatewayProxyRequestEvent event = new APIGatewayProxyRequestEvent();
    event.setHttpMethod("DELETE");
    event.setPath("/orders/ORD-999");

    APIGatewayProxyResponseEvent res = handler.handleRequest(event, null);
    assertEquals(404, res.getStatusCode());
    JsonObject body = gson.fromJson(res.getBody(), JsonObject.class);
    assertEquals("ORDER_NOT_FOUND", body.get("error").getAsString());
  }
}
