# REST Assured + TestNG Demo

Simple REST API test project using **REST Assured 5** and **TestNG 7**. No complex architecture — just POJOs, test classes, and a `pom.xml`.

## Project Structure

```
RestAssuredDemo/
├── src/test/java/
│   ├── models/
│   │   ├── Post.java
│   │   └── User.java
│   └── tests/
│       ├── BaseTest.java          ← RequestSpecification setup
│       ├── GetTests.java          ← GET request tests
│       ├── CrudTests.java         ← POST / PUT / PATCH / DELETE
│       └── HeadersAndAuthTests.java
├── src/test/resources/
│   └── testng.xml                 ← suite definition
└── pom.xml
```

## Run Tests

```bash
# Run all tests via testng.xml
mvn test

# Run a single test class
mvn test -Dtest=GetTests

# Run a single method
mvn test -Dtest=GetTests#getAllPosts_shouldReturn200AndNonEmptyList
```

## Key Patterns Covered

| File | Covers |
|------|--------|
| `BaseTest.java` | `RequestSpecBuilder`, logging filters |
| `GetTests.java` | `GET` list & single, query params, 404, deserialization |
| `CrudTests.java` | `POST` (201), `PUT`, `PATCH`, `DELETE` |
| `HeadersAndAuthTests.java` | Custom headers, Bearer JWT, content-type, path params |

## BDD Quick Reference

```java
given()
    .spec(spec)             // reuse base config
    .header("X-Key", "v")  // add header
    .queryParam("q", "1")  // query param
    .pathParam("id", 5)    // path param  →  /posts/{id}
    .body(myObject)        // auto-serialised to JSON
.when()
    .get("/posts/{id}")    // HTTP method + path
.then()
    .statusCode(200)
    .body("id",    equalTo(5))
    .body("title", not(emptyString()))
    .extract().as(Post.class);   // deserialise response
```
