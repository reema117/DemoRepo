package tests;

import io.restassured.common.mapper.TypeRef;
import models.Post;
import models.User;
import org.testng.annotations.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class GetTests extends BaseTest {

    @Test
    public void getAllPosts_shouldReturn200AndNonEmptyList() {
        given()
            .spec(spec)
        .when()
            .get("/posts")
        .then()
            .statusCode(200)
            .body("size()", greaterThan(0));
    }

    @Test
    public void getPostById_shouldReturnCorrectPost() {
        given()
            .spec(spec)
        .when()
            .get("/posts/1")
        .then()
            .statusCode(200)
            .body("id",     equalTo(1))
            .body("title",  not(emptyString()))
            .body("userId", greaterThan(0));
    }

    @Test
    public void getPostById_invalidId_shouldReturn404() {
        given()
            .spec(spec)
        .when()
            .get("/posts/99999")
        .then()
            .statusCode(404);
    }

    @Test
    public void getPostsByUser_shouldReturnFilteredPosts() {
        List<Post> posts =
            given()
                .spec(spec)
                .queryParam("userId", 1)
            .when()
                .get("/posts")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .extract()
                .as(new TypeRef<List<Post>>() {});

        posts.forEach(p ->
            org.testng.Assert.assertEquals(p.getUserId(), 1,
                "Expected userId=1 but got " + p.getUserId())
        );
    }

    @Test
    public void getAllUsers_shouldReturn10Users() {
        given()
            .spec(spec)
        .when()
            .get("/users")
        .then()
            .statusCode(200)
            .body("size()", equalTo(10));
    }

    @Test
    public void getUserById_shouldHaveValidEmailFormat() {
        User user =
            given()
                .spec(spec)
            .when()
                .get("/users/1")
            .then()
                .statusCode(200)
                .body("name", not(emptyString()))
                .extract()
                .as(User.class);

        org.testng.Assert.assertTrue(
            user.getEmail().contains("@"),
            "Email should contain @: " + user.getEmail()
        );
    }
}
