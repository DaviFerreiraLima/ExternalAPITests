package resources;

import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import resources.utils.JWTGenerator;

import java.util.Date;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

class UserTest {

        @BeforeAll
         static void setUp() {
            baseURI = "http://localhost:3003";
        }

        //FUNÇÃO GET

    @Test
    void deveObterUsuariosComSucesso() {

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);
        given()
                .header("Authorization", token)
        .when()
                .get("/user")
        .then()
                .statusCode(is(200))
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"));
    }

    @Test
    void deveObterUsuariosSemSucessoESemAutorizacao(){

        String cpf = "777-777-777-77";
        String token = JWTGenerator.tokenGenerator("admin",cpf);
        given()
                .header("Authorization", token)
        .when()
                .get("/user")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    void deveObterUsuariosSemSucessoESemPermissão() {

        String cpf = "703-555-738-22";

        String token = JWTGenerator.tokenGenerator("owner",cpf);

        given()
                .header("Authorization", token)
        .when()
                .get("/user")
        .then()
                .statusCode(is(403))
                .body("msg",is("Access forbidden for this user."));
    }

    @Test
    void deveDetalharUmUsuarioComSucesso(){

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

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

        String cpf = "777-777-777";
        String token = JWTGenerator.tokenGenerator("noRole",cpf);
        given()
                .header("Authorization", token)
        .when()
            .get("/user/1")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));
    }

    @Test
    void deveDetalharUmUsuarioSemSucessoESemPermissão() {

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("NoRole",cpf);

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

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

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

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

        given()
                .header("Authorization", token)
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(204));
    }

    @Test
    void deveRemoverUmUsuarioSemSucessoESemAutorizacao(){

        String cpf = "777-777-777";
        String token = JWTGenerator.tokenGenerator("noRole",cpf);

        given()
                .header("Authorization", token)
        .when()
            .delete("/user/1")
        .then()
                .statusCode(is(401))
                .body("msg",is("User not logged in!"));
    }

    @Test
    void deveRemoverUmUsuarioSemSucessoESemPermissão(){

        String cpf = "703-555-738-22";

        String token = JWTGenerator.tokenGenerator("owner",cpf);

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

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

        given()
                .header("Authorization", token)
        .when()
                .delete("/user/10000")
        .then()
            .statusCode(is(404))
            .body("msg",is("User not found."));
    }


    // FUNÇÃO PATCH

    @Test
    void deveAtualizarUmUsuarioComSucesso(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

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

        String cpf = "703-555-738-11";
        String token = JWTGenerator.tokenGenerator("noRole",cpf);
        given()
                .header("Authorization",token)
        .when()
            .patch("/user/1")
        .then()
            .statusCode(is(401))
            .body("msg",is("User not logged in!"));
    }

    @Test
    void deveAtualizarUmUsuarioSemSucessoESemPermissão(){

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("owner",cpf);

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

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

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

        String cpf = "703-555-738-22";
        String token = JWTGenerator.tokenGenerator("admin",cpf);

        given()
            .header("Authorization", token)
                .contentType(ContentType.JSON)
                .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail()
                        + "\",\"password\":\"12345\",\"date_of_birth\":\"" + usuario.getDateOfBirth() + "\"}")
            .pathParam("id", 1)
        .when()
            .patch("/user/{id}")
        .then()
            .statusCode(is(422));
    }

    // FUNÇÃO POST

    @Test
    void deveRegistrarUmUsuarioComSucesso() {
        User usuario = new User("João da Silva", "123.456.789-01", "joao@exampleee.com", new Date());

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + usuario.getName() +
                    "\",\"cpf\":\"" + usuario.getCpf() +
                    "\",\"email\":\"" + usuario.getEmail()
                    + "\",\"password\":\"12345\",\"date_of_birth\":\"" + usuario.getDateOfBirth() + "\"}")
        .when()
            .post("/user")
        .then()
            .statusCode(is(201))
            .body("id", is(notNullValue()))
            .body("name", is(usuario.getName()))
            .body("cpf", is(usuario.getCpf()))
            .body("email", is(usuario.getEmail()))
            .body("date_of_birth", is(usuario.getDateOfBirth()))
            .header("Authorization", is(notNullValue()));
    }

    @Test
    void deveRegistrarUmUsuarioSemSucessoComConflitoDeCPF(){

        User usuario = new User("João da Silva", "123.456.789-00", "joao@example.com", new Date());

        given()
                .contentType(ContentType.JSON)
                .body(usuario)
        .when()
                .post("/user")
        .then()
                .statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + usuario.getName() +
                        "\",\"cpf\":\"" + usuario.getCpf() +
                        "\",\"email\":\"" + usuario.getEmail()
                        + "\",\"password\":\"12345\",\"date_of_birth\":\"" + usuario.getDateOfBirth() + "\"}")
        .when()
            .post("/user")
        .then()
            .statusCode(is(409))
            .body("msg",is("CPF already exists!"));
    }


    @Test
    void deveRegistrarUmUsuarioSemSucessoEComProblemaDeValidacao() {

        User usuario = new User();

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\":\"" + usuario.getName() +
                "\",\"cpf\":\"" + usuario.getCpf() +
                "\",\"email\":\"" + usuario.getEmail()
                + "\",\"password\":\"12345\",\"date_of_birth\":\"" + usuario.getDateOfBirth() + "\"}")
        .when()
            .post("/user")
        .then()
            .statusCode(is(422))
            .body("msg", is("Validation problem"));
    }
}
