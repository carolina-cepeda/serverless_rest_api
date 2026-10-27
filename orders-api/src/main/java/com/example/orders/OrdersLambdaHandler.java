package com.example.orders;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class OrdersLambdaHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

  private final OrderService orderService = new OrderService();
  private final Gson gson = new Gson();

  @Override
  public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
    String httpMethod = input.getHttpMethod();
    String path = input.getPath();

    APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
    Map<String, String> headers = new HashMap<>();
    headers.put("Content-Type", "application/json");
    response.setHeaders(headers);

    try {
      if ("POST".equals(httpMethod) && "/orders".equals(path)) {
        CreateOrderRequest request = gson.fromJson(input.getBody(), CreateOrderRequest.class);
        Order order = orderService.createOrder(request);
        response.setStatusCode(201);
        response.setBody(gson.toJson(order));
        response.getHeaders().put("Location", "/orders/" + order.getId());
        return response;
      }

      if ("GET".equals(httpMethod) && "/orders".equals(path)) {
        String customerId = null;
        if (input.getQueryStringParameters() != null) {
          customerId = input.getQueryStringParameters().get("customerId");
        }
        if (customerId == null || customerId.isBlank()) {
          response.setStatusCode(400);
          response.setBody(gson.toJson(new ErrorResponse("INVALID_REQUEST", "customerId query parameter is required")));
          return response;
        }
        response.setStatusCode(200);
        response.setBody(gson.toJson(orderService.getOrdersByCustomer(customerId)));
        return response;
      }

      if ("GET".equals(httpMethod) && path.startsWith("/orders/")) {
        String id = path.substring("/orders/".length());
        Order order = orderService.getOrder(id);
        response.setStatusCode(200);
        response.setBody(gson.toJson(order));
        return response;
      }

      if ("DELETE".equals(httpMethod) && path.startsWith("/orders/")) {
        String id = path.substring("/orders/".length());
        boolean deleted = orderService.deleteOrder(id);
        if (deleted) {
          response.setStatusCode(204);
          response.setBody("");
        } else {
          response.setStatusCode(404);
          response.setBody(gson.toJson(new ErrorResponse("ORDER_NOT_FOUND", "Order " + id + " does not exist")));
        }
        return response;
      }

      response.setStatusCode(400);
      response.setBody(gson.toJson(new ErrorResponse("INVALID_REQUEST", "Unsupported operation")));
      return response;

    } catch (OrderNotFoundException e) {
      response.setStatusCode(404);
      response.setBody(gson.toJson(new ErrorResponse("ORDER_NOT_FOUND", e.getMessage())));
      return response;
    } catch (IllegalArgumentException e) {
      response.setStatusCode(400);
      response.setBody(gson.toJson(new ErrorResponse("INVALID_REQUEST", e.getMessage())));
      return response;
    } catch (Exception e) {
      response.setStatusCode(500);
      response.setBody(gson.toJson(new ErrorResponse("INTERNAL_ERROR", "Unexpected internal error")));
      return response;
    }
  }
}
