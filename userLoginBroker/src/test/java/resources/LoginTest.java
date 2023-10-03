package resources;

import com.google.gson.JsonObject;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import resources.utils.JWTGenerator;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;

class LoginTest {

    @BeforeAll
    static void setUp() {
        baseURI = "http://localhost:3003";
    }

    @Test

    void deveLogarComSucesso() {

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-00" + "\",\"password\":\"12345\"}")
        .when()
                .post("/login/login")
        .then()
                .statusCode(is(201))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"))
                .header("Authorization", is(notNullValue()));
    }

    @Test
    void deveLogarSemSucessoCamposIncorretos(){


        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "123.456.789-00" + "\",\"password\":\"1234\"}")
        .when()
                .post("/login")
        .then()
                .statusCode(is(401))
                .body("msg",is("Incorrect CPF or Password!"));
    }


    @Test
    void deveLogarSemSucessoCamposInvalidados(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "" + "\",\"password\":\"\"}")
        .when()
                .post("/login/login")
        .then()
                .statusCode(is(422))
                .body("msg",is("Validation Problem."));
    }

    @Test

    void deveLogarSemSucessoComRequestMalFormado(){

        given()
                .contentType(ContentType.JSON)
                .body("{\"cpf\":\"" + "02734" + "\",\"password\":\"1\"}")
        .when()
            .post("/login")
        .then()
            .statusCode(is(400))
            .body("msg",is( "Malformed request. Check the sent data."));
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
        void deveAutorizarOAccessoSolicitadoDeInformaçõesDeLogin(){

             String cpf = "123.456.789-00";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

        given()
                .header("Authorization", token)
                .when()
                .get("/login")
                .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"));
    }
    @Test
    void deveNaoAutorizarOAcessoAsolicitacaoDeInformacoesDeLoginSemAutorizacao(){

        String cpf = "123.456.789-1";
        String token = JWTGenerator.tokenGenerator("noRole",cpf);

        given()
            .header("Authorization", token)
        .when()
            .get("/login")
        .then()
            .statusCode(is(401));

    }

    @Test
    void deveNaoAutorizarOAcessoAsolicitacaoDeInformacoesDeLoginSemPermissao(){

        String cpf = "123.456.789-11";
        String token = JWTGenerator.tokenGenerator("noRole",cpf);

        given()
            .header("Authorization", token)
        .when()
            .get("/login/login")
        .then()
            .statusCode(is(403))
            .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("loginJsonSchema.json"));

    }
}