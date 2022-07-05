import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.RequestBuilder;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat; //para hacer assertions

public class ReqResTests {

    @Before
    public void setUp(){
        RestAssured.baseURI = "https://reqres.in";
        RestAssured.basePath = "/api";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        //configuraciones comunes
        //en este ejemplo esta el ContentType se peude agregar varios

        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)

                .build();
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
                .get("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("data.id",equalTo(2));
    }

    //Test avanzado de borrado
    @Test
    public void deleteUserTest(){
        given()
                .delete("/users/2")
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }


    //Test avanzado de patch
    // sirve para actualizar una o mas propiedades
    @Test
    public void patchUserTest(){
       String nameUpdate = given()
                .when()
                .body("{\n" +
                        "    \"name\": \"morpheus\",\n" +
                        "    \"job\": \"zion resident\"\n" +
                        "}")
                .patch("/users/2")
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath().getString("name");
    // creo una assertion con la libreria de hamcrest
    assertThat(nameUpdate,equalTo("morpheus"));
    } // otro metodo extrar para crear assertions
}
