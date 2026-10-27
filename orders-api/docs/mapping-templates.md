# Mapping Templates for API Gateway (Non-Proxy Integration) - Architecture A

This document shows how API Gateway can extract parameters and transform requests/responses before invoking Lambda (mapping templates / integration request/response mappings).

## GET /orders/{id}

**Method Request:** `GET /orders/{id}` with path parameter `id`

**Integration Request (VTL mapping template):**
```vtl
{
  "id": "$input.params('id')"
}
```

Lambda receives:
```json
{
  "id": "ORD-001"
}
```

**Integration Response:** Map Lambda response to HTTP 200/404. Return error JSON with appropriate status code.

## GET /orders?customerId={customerId}

**Integration Request (VTL mapping template):**
```vtl
{
  "customerId": "$input.params('customerId')"
}
```

Lambda receives:
```json
{
  "customerId": "CUST-42"
}
```

## POST /orders

**Integration Request:** Pass the request body through (or transform). In simple cases, can use `$input.json('$')` to forward the body.

**VTL example:**
```vtl
$input.json('$')
```

Lambda receives the JSON body as-is:
```json
{
  "customerId": "CUST-42",
  "product": "MacBook Air",
  "quantity": 1
}
```

## What API Gateway is responsible for (Architecture A)

- **Routing:** Match resources/methods
- **Extracting path parameters:** `$input.params('path')` / path mapping
- **Extracting query parameters:** `$input.params('querystring')`
- **Transforming requests:** Mapping templates (VTL) to build the exact JSON expected by Lambda
- **Transforming responses:** Integration Response mappings based on Lambda output/patterns
- **Choosing status codes:** Method Response + Integration Response mappings
- **Mapping errors:** Map exceptions to 400/404/500 with consistent error JSON

## Advantages / Disadvantages

**Advantages:**
- Lambda receives only the minimal payload it needs
- HTTP semantics can be enforced heavily in API Gateway
- Can reduce Lambda code for simple cases

**Disadvantages:**
- VTL is verbose and harder to test locally
- Configuration-heavy as API grows
- Logic spread across API Gateway and Lambda
- Harder to debug compared to explicit Java code
- Less portable
