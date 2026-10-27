# Example Requests and Responses

Base URL: `https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev`

## POST /orders (Create)
**Request:**
```bash
curl -i -X POST https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"CUST-42","product":"MacBook Air","quantity":1}'
```

**Response:**
```http
HTTP/2 201
Content-Type: application/json
Location: /orders/ORD-001

{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

## GET /orders/{id} (Retrieve)
**Request:**
```bash
curl -i -X GET https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev/orders/ORD-001
```

**Response (200):**
```http
HTTP/2 200
Content-Type: application/json

{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1}
```

**Response (404):**
```http
HTTP/2 404
Content-Type: application/json

{"error":"ORDER_NOT_FOUND","message":"Order ORD-999 does not exist"}
```

## GET /orders?customerId={customerId} (Search)
**Request:**
```bash
curl -i -X GET "https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev/orders?customerId=CUST-42"
```

**Response (200):**
```http
HTTP/2 200
Content-Type: application/json

[{"id":"ORD-001","customerId":"CUST-42","product":"MacBook Air","quantity":1}]
```

## DELETE /orders/{id}
**Request:**
```bash
curl -i -X DELETE https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev/orders/ORD-001
```

**Response (204):**
```http
HTTP/2 204
```

**Response (404):**
```http
HTTP/2 404
Content-Type: application/json

{"error":"ORDER_NOT_FOUND","message":"Order ORD-999 does not exist"}
```

## Invalid input (400)
**Request:**
```bash
curl -i -X POST https://ztegg7jbm4.execute-api.us-east-1.amazonaws.com/dev/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":"","product":"X","quantity":0}'
```

**Response (400):**
```http
HTTP/2 400
Content-Type: application/json

{"error":"INVALID_REQUEST","message":"customerId is required"}
```
