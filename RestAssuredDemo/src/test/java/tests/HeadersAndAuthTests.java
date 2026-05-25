package tests;

import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class HeadersAndAuthTests extends BaseTest {

    @Test
    public void request_withCustomHeader_shouldSucceed() {
        given()
            .spec(spec)
            .header("X-Custom-Header", "MyValue")
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200);
    }

    @Test
    public void request_withBearerToken_shouldSucceed() {
        // Shows the Bearer pattern — JSONPlaceholder ignores the token;
        // a real API would validate it and return 401 on failure.
        given()
            .spec(spec)
            .header("Authorization", "Bearer my-fake-jwt-token")
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200);
    }

    @Test
    public void response_shouldHaveJsonContentType() {
        given()
            .spec(spec)
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .contentType(containsString("application/json"));
    }

    @Test
    public void request_withQueryParams_shouldBeReflectedInResponse() {
        given()
            .spec(spec)
            .queryParam("userId", 1)
            .queryParam("_limit", 5)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0))
            .body("userId", everyItem(equalTo(1)));
    }

    @Test
    public void request_withPathParam_shouldReturnCorrectResource() {
        int postId = 5;

        given()
            .spec(spec)
            .pathParam("id", postId)
        .when()
            .get("/posts/{id}")
        .then()
            .statusCode(200)
            .body("id", equalTo(postId));
    }
}
