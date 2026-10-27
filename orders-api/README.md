# Serverless REST API — Order Management API

Java 17, Maven, AWS Lambda, Amazon API Gateway, JUnit.

## Phase 1 — API Contract Design

The API manages orders. An order has this JSON representation:

```json
{
  "id": "ORD-001",
  "customerId": "CUST-42",
  "product": "MacBook Air",
  "quantity": 1
}
```

### Resource table

| Operation | Method | URL | Path params | Query params | Request body | Success body | Success status | Errors | Error status |
|---|---|---|---|---|---|---|---|---|---|
| Create an order | `POST` | `/orders` | — | — | `{ customerId, product, quantity }` | Created `Order` | `201 Created` | invalid input | `400 Bad Request` |
| Retrieve an order | `GET` | `/orders/{id}` | `id` | — | — | `Order` | `200 OK` | not found | `404 Not Found` |
| Find orders by customer | `GET` | `/orders` | — | `customerId` | — | `List<Order>` | `200 OK` | none (empty list) | `200 OK` |
| Delete an order | `DELETE` | `/orders/{id}` | `id` | — | — | none | `204 No Content` | not found | `404 Not Found` |

### Error JSON contract

All errors return a JSON document with this shape:

```json
{
  "error": "ORDER_NOT_FOUND",
  "message": "Order ORD-999 does not exist"
}
```

### Design questions

**What are the resources in your API?**
Two resources: the orders collection (`/orders`) and a single order (`/orders/{id}`).

**Why did you choose `/orders/{id}` to retrieve a particular order?**
The `id` uniquely identifies a single order resource, so it belongs in the path as a path parameter. This follows REST conventions where the URL identifies the resource.

**Why is `customerId` represented as a query parameter in the search operation?**
`customerId` filters a collection rather than identifying one specific resource. Query parameters are the appropriate mechanism for collection filtering/search.

**What should `POST /orders` return when an order is successfully created?**
`201 Created` with the created `Order` object in the body. A `Location: /orders/{id}` header is appropriate to point to the new resource.

**What should the API return when an order does not exist?**
`404 Not Found` with the standard error JSON (`error` code + `message`).

**What information should an error response contain?**
An `error` code (stable, machine-readable) and a `message` (human-readable).

## Phase 2 — Plain Java Application (Part I)

The application logic lives in `src/main/java/com/example/orders/` and depends on nothing but plain Java:

- `Order` — domain entity
- `CreateOrderRequest` — input for creating an order
- `OrderNotFoundException` — domain exception
- `OrderService` — `createOrder`, `getOrder`, `getOrdersByCustomer`, `deleteOrder` backed by an in-memory `ConcurrentHashMap`
- `CreateOrderHandler` — thin AWS Lambda entry point delegating to `OrderService`

No HTTP, no API Gateway classes, no Spring Boot, no web server.

### Build and test

```bash
mvn test
mvn package
```

### Checkpoint 1

| Question | Answer |
|---|---|
| Can the application functionality be tested without AWS? | Yes, with plain JUnit tests. |
| Does your application require a web server? | No. |
| Does it require Spring Boot? | No. |
| What does the generated JAR contain? | Compiled domain classes (`Order`, `CreateOrderRequest`, `OrderService`, `OrderNotFoundException`, `CreateOrderHandler`) and dependencies. |
| Which parts of your application currently know about HTTP? | None yet; only the Lambda handler will later receive HTTP-shaped events. |
