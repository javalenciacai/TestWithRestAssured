import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ReqResTests {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }
    //primer test basico
    @Test
    public void loginTest(){
        String response = given()
                .log().all()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("https://reqres.in/api/login")
                .then()
                .log().all()
                .extract()
                .asString();

        System.out.println(response);
    }

    //Test mas abreviado
    @Test
    public void loginTest2(){

                given()
                .contentType(ContentType.JSON)
                .body("{\n" +
                        "    \"email\": \"eve.holt@reqres.in\",\n" +
                        "    \"password\": \"cityslicka\"\n" +
                        "}")
                .post("/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());

    }
    //Test avanzado
    @Test
    public void singleUsers(){
        given()
                 .contentType(ContentType.JSON)
                .get("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id",equalTo(2));
    }
}
