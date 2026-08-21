# Customer Rewards REST API

## Assignment

A retailer awards customer reward points for each recorded purchase:

- 2 points for every dollar spent over $100.
- 1 point for every dollar spent between $50 and $100.
- No points for the first $50.
- Example: a $120 purchase earns 90 points.

The API calculates reward points for each customer by calendar month and provides the total for the requested period.

## Technology

- Java 21
- Spring Boot 3.5.4
- Spring Web
- Spring JDBC
- H2 in-memory database
- JUnit 5
- Mockito
- MockMvc
- Maven

## Project structure

```text
src/
├── main/
│   ├── java/com/charter/rewards/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   └── resources/
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
└── test/
    └── java/com/charter/rewards/
        ├── controller/
        └── service/
```

## Database

The assignment asks not to hard-code data in Java. The schema and sample transactions are therefore supplied through:

- `schema.sql`
- `data.sql`

The sample dataset contains multiple customers and multiple transactions across May, June and July 2026.

## API

### Get rewards

```http
GET /api/rewards?from=2026-05-01&to=2026-07-31
```

Example response:

```json
[
  {
    "customerId": 1,
    "customerName": "Alice Johnson",
    "monthlyRewards": [
      {"month": "2026-05", "points": 65},
      {"month": "2026-06", "points": 220},
      {"month": "2026-07", "points": 170}
    ],
    "totalPoints": 455
  }
]
```

Months are calculated dynamically from transaction dates. They are not hard-coded.

## Reward calculation

For a transaction amount `x`:

- `x <= 50`: 0 points
- `50 < x <= 100`: `floor(x - 50)` points
- `x > 100`: `50 + 2 * floor(x - 100)` points

For example:

- `$49.99` -> 0
- `$50.00` -> 0
- `$75.00` -> 25
- `$100.00` -> 50
- `$120.00` -> 90

## Testing

Run:

```bash
mvn clean test
```

The test suite contains:

- Unit tests for reward boundaries.
- Unit tests for negative and null amounts.
- Unit tests for multiple customers and multiple months.
- Unit tests for invalid date ranges.
- Integration tests using Spring Boot, H2 and MockMvc.
- Negative API tests for invalid and missing request parameters.

## Run locally

```bash
mvn spring-boot:run
```

Then call:

```bash
curl "http://localhost:8080/api/rewards?from=2026-05-01&to=2026-07-31"
```

## Git submission checklist

Before committing:

```bash
mvn clean test
git status
git add .
git commit -m "Implement customer rewards API"
git push origin main
```

Do not commit:

- `target/`
- `bin/`
- IDE-specific files
- ZIP files
- credentials, passwords or OAuth keys

The repository should be created and pushed using the personal GitHub account and personal laptop as required by the assignment.
