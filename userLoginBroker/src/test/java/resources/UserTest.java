package resources;

import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class UserTest {



        @BeforeAll
         static void setUp() {
            baseURI = "http://restapi.wcaquino.me";
            port = 80;
        }

        //FUNÇÃO GET

    @Test
    void deveObterUsuariosComSucesso() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/users")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }

    @Test
    void deveObterUsuariosSemSucessoESemAutorizacao(){

        given()
        .when()
                .get("/users")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    void deveObterUsuariosSemSucessoESemPermissão() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/users")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user."));
    }

    @Test
    void deveDetalharUmUsuarioComSucesso(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";


        given()
                .header("Authorization", token)
        .when()
                .get("/user/1")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }

    @Test
    void deveDetalharUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
            .get("/user/1")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));
    }

    @Test
    void deveDetalharUmUsuarioSemSucessoESemPermissão() {

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/user/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user."));
    }

    @Test
    void deveDetalharUmUsuarioSemSucessoENaoEncontrado(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .get("/user/1000")
        .then()
                .statusCode(is(404))
                .body("msg",is("User not found."));
    }



        // FUNÇÃO DELETE

    @Test
    void deveRemoverUmUsuarioComSucesso(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(204));
    }

    @Test
    void deveRemoverUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    void deveRemoverUmUsuarioSemSucessoESemPermissão(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .delete("/user/1")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user"));
    }

    @Test
    void deveRemoverUmUsuarioSemSucessoENaoEncontrado(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
                .delete("/user/1000")
        .then()
            .statusCode(is(404))
            .body("msg",is("User not found."));
    }


    // FUNÇÃO PATCH

    @Test
    void deveAtualizarUmUsuarioComSucesso(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(usuario)
                .pathParam("id", 1)
        .when()
                .patch("/user/{id}")
        .then()
                .statusCode(is(200));
    }


    @Test
    void deveAtualizarUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
            .patch("/user/1")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));
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
            .body("msg",is("Access forbidden for this user"));
    }

    @Test
     void deveAtualizarUmUsuarioSemSucessoENaoEncontrado(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .patch("/user/1000")
        .then()
            .statusCode(is(404))
            .body("msg",is("User not found."));
    }

    @Test
    void deveAtualizarUmUsuarioSemSucessoEComProblemaDeValidacao(){

        User usuario = new User();

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(usuario)
            .pathParam("id", 1)
        .when()
            .patch("/user/{id}")
        .then()
            .statusCode(is(422));
    }

    // FUNÇÃO POST

    @Test
    void deveRegistrarUmUsuarioComSucesso(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(usuario)
        .when()
            .post("/user")
        .then()
                .statusCode(is(201))
                .body("id", is(notNullValue()))
                .body("name", is("João da Silva"))
                .body("cpf",is("123.456.789-00"))
                .body("email",is("joao@example.com"))
                .body("date of birth",is(usuario.getDateOfBirth()))
                .header("Authorization",token);
    }

    @Test
    void deveRegistrarUmUsuarioSemSucessoESemAutorizacao(){

        given()
        .when()
                .post("/user")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));

    }

    @Test
    void deveRegistrarUmUsuarioSemSucessoESemPermissão(){

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
        .when()
            .post("/user")
        .then()
            .statusCode(is(403))
            .body("msg",is("Access forbidden for this user"));
    }

    @Test
    void deveRegistrarUmUsuarioSemSucessoComConflitoDeCPF(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
                .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body(usuario)
        .when()
                .post("/user")
        .then()
                .statusCode(201);

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(usuario)
        .when()
            .post("/user")
        .then()
            .statusCode(is(409))
            .body("msg",is("CPF already exists!"));
    }


    @Test
    void deveRegistrarUmUsuarioSemSucessoEComProblemaDeValidacao(){

        User usuario = new User();

        String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

        given()
            .header("Authorization", token)
            .contentType(ContentType.JSON)
            .body(usuario)
        .when()
            .post("/user")
        .then()
            .statusCode(is(422))
            .body("msg",is("Validation problem"));

}
