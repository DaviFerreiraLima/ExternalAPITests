package resources;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import resources.utils.BrokerUtils;

import java.awt.print.Pageable;
import java.util.Date;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BrokerTest {


    @BeforeAll
    static void setUp() {
        baseURI = "/iot";
        port = 80;
    }


    //Função Get
    @Test

    void deveObterOsClientesDoBrokerComSucesso(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/broker_client")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }


    @Test

    void deveObterOsClientesDoBrokerSemSucessoESemAutorização(){

        given()
        .when()
            .get("/broker_client")
        .then()
            .statusCode(is(401))
            .body("msg",is("Broker Client is not logged in!"));
    }

    @Test

    void deveObterOsClientesDoBrokerSemSucessoESemPermissão(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .get("/broker_client")
        .then()
            .statusCode(is(403))
            .body("msg",is("Broker Client do not have permission!"));
    }
    
    @Test
    void deveObterClienteBrokerComSucesso() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }
    
    @Test
    void deveObterClienteBrokerSemSucessoESemAutorizacao() {

        given()
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("Broker Client is not logged in!"));
    }
    
    @Test
    void deveObterClienteBrokerSemSucessoESemPermissão() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Broker Client do not have permission!"));
    }
    
    //Função POST

    @Test
    void deveResgistrarUmNovoClienteBrokerComSucesso(){
        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        JsonObject broker = BrokerUtils.criarPostBroker();

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker_client")
        .then()
                .statusCode(is(201))
                .header("Location",is(notNullValue()))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }
    
    @Test
    void deveResgistrarUmNovoClienteBrokerComProblemaValidacao(){
        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        JsonObject broker = BrokerUtils.criarPostBrokerInvalido();

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker_client")
        .then()
                .statusCode(is(422))
                .body("msg",is("Syntax Error!"));
    }
    
    @Test
    void deveResgistrarUmNovoClienteBrokerComConflito(){
        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        JsonObject broker = BrokerUtils.criarPostBroker();
        
        given()
	        .header("Authorization", token)
	        .contentType(ContentType.JSON)
	        .body(broker)
        .when()
        	.post("/broker_client")
        .then()
        	.statusCode(201);

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker_client")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client already exist!"));
    }
    
  //Função DELETE
    
    @Test
    void deveRemoverUmClienteBrokerComSucesso(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .delete("/broker_client/1")
        .then()
                .statusCode(is(204));
    }
    
    @Test
    void deveRemoverUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
            .delete("/broker_client/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("Broker Client is not logged in!"));
    }
    
    @Test
    void deveRemoverUmUsuarioSemSucessoESemPermissão(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .delete("/broker_client/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Do not have permission to remove!"));
    }
    
 // FUNÇÃO PATCH

    @Test
    void deveAtualizarClientBrokerComSucesso(){

    	JsonObject broker = BrokerUtils.criarPostBroker();

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(broker)
                .pathParam("id", 1)
        .when()
                .patch("/user/{id}")
        .then()
                .statusCode(is(204));
    }


    @Test
    void deveAtualizarClientBrokerSemSucessoESemAutorizacao(){

        given()
        .when()
            .patch("/user/1")
        .then()
            .statusCode(is(401))
            .body("msg",is("Broker Client is not logged in!"));
    }
    
    @Test
    void deveAtualizarUmUsuarioSemSucessoESemPermissão(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .patch("/user/1")
        .then()
            .statusCode(is(403))
            .body("msg",is("Broker Client does not have permission!"));
    }
    
}