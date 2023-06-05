package com.example.ics;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class IcsApplicationTestsа {
    private static String tagSaved;
    private static String imgIdSaved;
    private static List<String> tagList = new ArrayList<>();
    private static int numOfImages;
    private static String imgId;

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8079;

    }

    @Order(1)
    @Test
    public void testGetAllImages() {
        Response response = given()
                .get("/api/v2/images")
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        numOfImages = response.jsonPath().getList("").size();
    }

    @Order(2)
    @Test
    public void testClassifyClarifai() {
        String imageUrl = "{\"imageUrl\":\"https://th.bing.com/th/id/R.a4de62f27e25796493e633172f99afb3?rik=1Kfs9c3kFWIKnw&pid=ImgRaw&r=0\"}";


        Response response = given()
                .contentType("text/plain")
                .body(imageUrl)
                .post("/api/v2/classify/imagga")
                .then()
                .statusCode(200)
                .body(notNullValue())
                .extract().response();


        imgId = response.getBody().asString();
    }


    @Order(3)
    @Test
    public void testGetAllImagesAgain() throws InterruptedException {
        Thread.sleep(2000);
        Response responseAgain = given()
                .get("/api/v2/images")
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        int numOfImagesAgain = responseAgain.jsonPath().getList("").size();


        assertEquals(numOfImages + 1, numOfImagesAgain);
    }

    @Order(4)
    @Test
    public void testDeleteImage() throws InterruptedException {
        Thread.sleep(1000);
        given()
                .delete("/api/v2/images/{id}", imgId)
                .then()
                .statusCode(200);


        given()
                .get("/api/v2/images/{id}", imgId)
                .then()
                .statusCode(404);
    }

    @Order(5)
    @Test
    public void testGetAllUniqueTags() {

        Response response = given()
                .get("/api/v2/getUniqueTags")
                .then()
                .statusCode(200)
                .body(is(notNullValue()))
                .extract().response();


        tagSaved = (String) response.jsonPath().getList("").get(0);


        assertNotNull(tagSaved);
    }

    @Order(6)
    @Test
    public void testGetImagesByTag() {

        Response response = given()
                .get("/api/v2/getimage/{tag}", tagSaved)
                .then()
                .statusCode(200)
                .body("[0].id", is(notNullValue()))
                .body("[0].tags", is(notNullValue()))
                .body("[0].name", is(notNullValue()))
                .body("[0].url", is(notNullValue()))
                .body("[0].imgurlUrl", is(notNullValue()))
                .body("[0].analyzedAt", is(notNullValue()))
                .body("[0].hash", is(notNullValue()))
                .extract().response();


        imgIdSaved = response.jsonPath().getString("[0].id");


        assertNotNull(imgIdSaved);
    }

    @Order(7)
    @Test
    public void testGetTagsByImageId() {

        Response response = given()
                .get("api/v2/images/{id}/tags", imgIdSaved)
                .then()
                .statusCode(200)
                .extract().response();
        JsonPath jsonPath = response.jsonPath();
        List<Map<String, Object>> tags = jsonPath.getList("");
        for (Map<String, Object> tag : tags) {
            String tagName = (String) tag.get("tag");
            tagList.add(tagName);
        }
        assertNotNull(imgIdSaved);
    }

    @Order(8)
    @Test
    public void testGetImageById() {

        Response response = given()
                .get("/api/v2/images/{id}", imgIdSaved)
                .then()
                .statusCode(200)
                .body("id", is(notNullValue()))
                .body("tags", is(notNullValue()))
                .body("name", is(notNullValue()))
                .body("url", is(notNullValue()))
                .body("imgurlUrl", is(notNullValue()))
                .body("analyzedAt", is(notNullValue()))
                .body("hash", is(notNullValue()))
                .extract().response();


        List<String> tags = response.jsonPath().getList("tags");

        System.out.println(tagList);
        System.out.println(tags.toString());
        System.out.println(tagSaved);
        assertTrue(tagList.contains(tagSaved));
    }

    @Test
    public void testGetAllImagesPage() {
        given().
                contentType(ContentType.JSON).
                param("pageNo", 0).
                param("pageSize", 10).
                param("direction", "asc").
                when().
                get("api/v2/all").
                then().
                assertThat().
                statusCode(200).
                body("content", hasSize(10));
    }

    @Test
    public void testGetAllImagesPageWithTags() {
        given().
                contentType(ContentType.JSON).
                param("pageNo", 0).
                param("pageSize", 1).
                param("direction", "asc").
                param("tag", "a").
                when().
                get("api/v2/all").
                then().
                assertThat().
                statusCode(200).
                body("content", hasSize(1));
    }

    @Test
    public void testGetAllImagesPageWithTags_NotFound() {
        given().
                contentType(ContentType.JSON).
                param("pageNo", 0).
                param("pageSize", 1).
                param("direction", "asc").
                param("tag", "notagwillbenamedlikethis").
                when().
                get("api/v2/all").
                then().
                assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


}
