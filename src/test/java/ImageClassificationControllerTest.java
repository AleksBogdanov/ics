import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.path.json.JsonPath.from;
import static org.junit.jupiter.api.Assertions.*;

public class ImageClassificationControllerTest {

    @BeforeEach
    public void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8079;
    }

//    @Test
//    public void testClassify() {
//        String imageUrl = "http://example.com/image.jpg";
//
//        given()
//                .contentType("application/json")
//                .body(imageUrl)
//                .when()
//                .post("/api/v2/classify/imagga")
//                .then()
//                .statusCode(200);
//
//    }




    @Test
    public void testGetImages() {
        Response response =
                given()
                        .when()
                        .get("/api/v2/images")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();

        String jsonResponse = response.asString();
        List<Map<String, ?>> images = from(jsonResponse).getList("");


        assertTrue(!images.isEmpty());


        for (Map<String, ?> image : images) {
            assertTrue(image.containsKey("id"));
        }


        int id = (int) images.get(0).get("id");
        assertEquals(1, id);


        for (Map<String, ?> image : images) {
            assertNotNull(image.get("name"));
        }
    }


    @Test
    public void testDeleteWithID() {
        Integer id = 1;

        given()
                .when()
                .delete("/api/v2/images/" + id)
                .then()
                .statusCode(200);

    }



}
