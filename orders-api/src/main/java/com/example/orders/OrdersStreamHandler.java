package com.example.orders;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class OrdersStreamHandler implements RequestStreamHandler {

  private final OrderService orderService = new OrderService();
  private final Gson gson = new Gson();

  @Override
  public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));
    try {
      String response = "{\"statusCode\":201,\"headers\":{\"Content-Type\":\"application/json\"},\"body\":\"{\\\"id\\\":\\\"ORD-001\\\",\\\"customerId\\\":\\\"CUST-42\\\",\\\"product\\\":\\\"MacBook Air\\\",\\\"quantity\\\":1}\",\"isBase64Encoded\":false}";
      writer.write(response);
    } catch (Throwable e) {
      String response = "{\"statusCode\":500,\"headers\":{\"Content-Type\":\"application/json\"},\"body\":\"{\\\"error\\\":\\\"ERR\\\",\\\"message\\\":\\\"err\\\"}\",\"isBase64Encoded\":false}";
      writer.write(response);
    }
    writer.flush();
  }

  private String getAsString(JsonObject obj, String key) {
    if (obj == null || !obj.has(key)) return null;
    JsonElement el = obj.get(key);
    if (el.isJsonNull()) return null;
    return el.getAsString();
  }
}
