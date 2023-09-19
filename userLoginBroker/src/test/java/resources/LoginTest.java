package resources;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @BeforeAll
    static void setUp() {
        baseURI = "http://restapi.wcaquino.me";
        port = 80;
    }

    @Test

    void deveLogarComSucesso() {

        JsonObject login = new JsonObject();

        login.addProperty("cpf", "123.456.789-00");
        login.addProperty("password", "12345678");

        given()
                .contentType(ContentType.JSON)
                .body(login)
        .when()
                .post("/login")
        .then()
                .statusCode(is(201))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"))
                .header("Authorization", is(notNullValue()));
    }

    @Test
    void deveLogarSemSucessoCamposIncorretos(){

        JsonObject login = new JsonObject();

        login.addProperty("cpf", "123");
        login.addProperty("password", "123");

        given()
                .contentType(ContentType.JSON)
                .body(login)
        .when()
                .post("/login")
        .then()
                .statusCode(is(401))
                .body("msg",is("Incorrect CPF or Password!"))
                .header("Authorization", is(notNullValue()));
    }


    @Test
    void deveLogarSemSucessoCamposInvalidados(){

        JsonObject login = new JsonObject();

        login.addProperty("cpf", "0273440510");
        login.addProperty("password", "111111111111111111111111111111111111111111111111111111");

        given()
                .contentType(ContentType.JSON)
                .body(login)
        .when()
                .post("/login")
        .then()
                .statusCode(is(422))
                .body("msg",is("Validation Problem."));
    }

    @Test

    void deveLogarSemSucessoESemAutorizacao(){

        JsonObject login = new JsonObject();

        login.addProperty("cpf", "0273440510");
        login.addProperty("password", "111111111111111111111111111111111111111111111111111111");

        given()
                .contentType(ContentType.JSON)
                .body(login)
        .when()
            .post("/login")
        .then()
            .statusCode(is(422))
            .body("msg",is("Validation Problem."));
    }

    @Test

    void deveProibirOAcessoASolicitacaoDeInformacoesDeLogin(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .get("/login")
        .then()
            .statusCode(is(403))
            .body("msg",is("Access forbidden for this user."));
    }


    @Test

    void deveNaoAutorizarOAcessoAsolicitacaoDeInformacoesDeLogin(){

        given()
        .when()
            .get("/login")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));

    }
}