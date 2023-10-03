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
        baseURI = "http://localhost:3003";
    }


    //Função Get
    @Test

    void deveObterOsClientesDoBrokerComSucesso(){

        given()
        .when()
                .get("/broker-client")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }


    /*@Test

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
    }*/
    
    @Test
    void deveObterClienteBrokerComSucesso() {

        given()
        .when()
                .get("/broker-client/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }
    
   /* @Test
    void deveObterClienteBrokerSemSucessoESemAutorizacao() {

        given()
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("Broker Client is not logged in!"));
    }*/
    
    /*@Test
    void deveObterClienteBrokerSemSucessoESemPermissão() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/broker_client/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Broker Client do not have permission!"));
    }*/
    
    //Função POST

    @Test
    void deveResgistrarUmNovoClienteBrokerComSucesso(){

        String broker = BrokerUtils.criarPostBroker().toString();

        System.out.println(broker);
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker-client")
        .then()
                .statusCode(is(201))
                .header("Location",is(notNullValue()))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("brokerJsonSchema.json"));
    }
    
    @Test
    void deveNaoResgistrarUmNovoClienteBrokerComProblemaValidacao(){

        String broker = BrokerUtils.criarPostBrokerInvalido().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker-client")
        .then()
                .statusCode(is(422))
                .body("msg",is("Syntax Error!"));
    }
    
    @Test
    void deveNaoResgistrarUmNovoClienteBrokerComConflito(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .post("/broker-client")
        .then()
                .statusCode(is(409))
                .body("msg",is("Broker Client already exist!"));
    }
    
  //Função DELETE
    
    @Test
    void deveRemoverUmClienteBrokerComSucesso(){

        given()
        .when()
            .delete("/broker-client/1")
        .then()
                .statusCode(is(204));
    }
    
    @Test
    void deveRemoverUmClienteBrokerSemSucessoENotFound(){

        given()
        .when()
            .delete("/broker-client/1")
        .then()
                .statusCode(is(404))
                .body("msg",is("Broker Client does not have permission!"));
    }

    
 // FUNÇÃO PATCH

    @Test
    void deveAtualizarClientBrokerComSucesso(){

    	String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
                .patch("/broker-client/651c91f975137e73aaa0227e")
        .then()
                .statusCode(is(204));
    }


    @Test
    void deveAtualizarClientBrokerSemSucessoEComDadosRepetidos(){

        String broker = BrokerUtils.criarPostBroker().toString();
        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .patch("/broker-client/1")
        .then()
            .statusCode(is(409))
            .body("msg",is("CPF already exists!"));
    }
    
    @Test
    void deveAtualizarUmUsuarioSemSucessoENotFound(){

        String broker = BrokerUtils.criarPostBroker().toString();

        given()
                .contentType(ContentType.JSON)
                .body(broker)
        .when()
            .patch("/broker-client/1")
        .then()
            .statusCode(is(404))
            .body("msg",is("Broker Client does not have permission!"));
    }
    
}