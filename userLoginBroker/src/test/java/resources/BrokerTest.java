package resources;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import resources.utils.BrokerUtils;

import java.awt.print.Pageable;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class BrokerTest {


    @BeforeAll
    static void setUp() {
        baseURI = "http://restapi.wcaquino.me";
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
}