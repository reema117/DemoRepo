package tests;

import models.Post;
import org.testng.annotations.Test;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class CrudTests extends BaseTest {

    // ── POST ──────────────────────────────────────────────────────────────────

    @Test
    public void createPost_shouldReturn201AndNewId() {
        Post newPost = new Post(1, "My New Post", "This is the body of the post.");

        given()
            .spec(spec)
            .body(newPost)
        .when()
            .post("/posts")
        .then()
            .statusCode(201)
            .body("id",    greaterThan(0))
            .body("title", equalTo("My New Post"))
            .body("body",  equalTo("This is the body of the post."));
    }

    @Test
    public void createPost_missingTitle_shouldStillReturn201() {
        // JSONPlaceholder is lenient — real APIs would return 400
        Map<String, Object> payload = Map.of("body", "No title here", "userId", 2);

        given()
            .spec(spec)
            .body(payload)
        .when()
            .post("/posts")
        .then()
            .statusCode(201);
    }

    // ── PUT ───────────────────────────────────────────────────────────────────

    @Test
    public void updatePost_shouldReturn200WithUpdatedFields() {
        Map<String, Object> updated = Map.of(
            "id",     1,
            "title",  "Updated Title",
            "body",   "Updated body content.",
            "userId", 1
        );

        given()
            .spec(spec)
            .body(updated)
        .when()
            .put("/posts/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("Updated Title"))
            .body("body",  equalTo("Updated body content."));
    }

    // ── PATCH ─────────────────────────────────────────────────────────────────

    @Test
    public void patchPost_shouldReturn200() {
        Map<String, String> patch = Map.of("title", "Patched Title");

        given()
            .spec(spec)
            .body(patch)
        .when()
            .patch("/posts/1")
        .then()
            .statusCode(200)
            .body("title", equalTo("Patched Title"));
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Test
    public void deletePost_shouldReturn200() {
        given()
            .spec(spec)
        .when()
            .delete("/posts/1")
        .then()
            .statusCode(200);
    }

    @Test
    public void deletePost_nonExistent_shouldReturn404() {
        given()
            .spec(spec)
        .when()
            .delete("/posts/99999")
        .then()
            .statusCode(404);
    }
}
