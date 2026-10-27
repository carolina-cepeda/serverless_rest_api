# API Gateway Setup (Reproducible) - Lambda Proxy Integration

## 1) Create REST API
1. API Gateway → Create API → REST API → Build
2. Name: `orders-rest-api`
3. Endpoint Type: Regional

## 2) Create Resources
- Create resource `/orders`
- Create resource `/orders/{id}` under `/orders`

## 3) Create Methods (Lambda Proxy Integration)
For each method, use **Lambda Function** with **Use Lambda Proxy integration** checked.

| Resource | Method | Integration |
|---|---|---|
| `/orders` | POST | Lambda Proxy → `orders-api-handler` |
| `/orders` | GET | Lambda Proxy → `orders-api-handler` |
| `/orders/{id}` | GET | Lambda Proxy → `orders-api-handler` |
| `/orders/{id}` | DELETE | Lambda Proxy → `orders-api-handler` |

## 4) Deploy
1. Actions → Deploy API
2. Stage: `dev`
3. Deploy
4. Copy Invoke URL: `https://<api-id>.execute-api.<region>.amazonaws.com/dev`

## 5) Lambda Configuration
- Runtime: Java 17 (Corretto)
- Handler: `com.example.orders.OrdersLambdaHandler::handleRequest`
- Memory: 512 MB
- Timeout: 30s
- Upload fat JAR: `target/orders-api-1.0.0.jar`
