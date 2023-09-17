package apirest.users;

import static io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import resources.User;

class UsersTest {
	
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		RestAssured.port = 80;
	}
	
	@Test
	public void deveObterUsuarioComSucesso() {
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
		.when()
			.get("/users/1")
		.then()
			//.log().all()
			.statusCode(is(200))
			.body("id", is(1))
			.body("name", containsString("Silva"))
			.body("age", greaterThan(18))
			.body("salary", is(1234.5678F))
		;
	}
	
	@Test
	public void deveValidarObjetoAninhadoComSucesso() {
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
		.when()
			.get("/users/2")
		.then()
			//.log().all()
			.statusCode(is(200))
			.body("id", is(2))
			.body("endereco.rua", is("Rua dos bobos"))
			.body("endereco.numero", is(0))
		;
	}
	
	@Test
	public void deveValidarArrayComSucesso() {
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
		.when()
			.get("/users/3")
		.then()
			//.log().all()
			.statusCode(is(200))
			.body("id", is(3))
			.body("filhos", hasSize(2))
			.body("filhos[0].name", is("Zezinho"))
			.body("filhos.name", hasItem("Luizinho"))
			.body("filhos.name", hasItems("Luizinho","Zezinho"))
			.body("filhos.name.toArray()", arrayContaining("Zezinho", "Luizinho"))
		;
	}
	
	@Test
	public void deveObterUsuariosComSucesso() {
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
		.when()
			.get("/users")
		.then()
			//.log().all()
			.statusCode(is(200))
			.body("", hasSize(3))
			.body("age.findAll{it <= 25}", hasSize(2))
			.body("age.findAll{it > 25 && it <= 30}", hasSize(1))
			.body("findAll{it.age <= 25}.name", hasItem("Ana Júlia"))
			.body("name.collect{it.toUpperCase()}", hasItem("ANA JÚLIA"))
			.body("salary.max()", is(2500))
			.body("salary.max()", allOf(greaterThan(2000), lessThan(3000)))
		;
	}
	
	@Test
	public void deveExtrairUsuariosComSucesso() {
		ArrayList<String> nomes = given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
		.when()
			.get("/users")
		.then()
			//.log().all()
			.statusCode(is(200))
			//.extract().path("name.findAll{it.startsWith('Maria')}")
			.extract().path("name")
		;
		
		//System.out.println(nomes);
		//assertThat(nomes.get(0), is("Maria Joaquina"));
		assertThat(nomes.toArray(), arrayContaining("João da Silva", "Maria Joaquina", "Ana Júlia"));
	}
	
	@Test
	public void deveSalvarUsuario() {
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
			.body("{\r\n"
					+ "	\"name\": \"Tiago\",\r\n"
					+ "	\"age\": 38\r\n"
					+ "}")
			.contentType(ContentType.JSON)
		.when()
			.post("/users")
		.then()
			//.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Tiago"))
		;
	}
	
	@Test
	public void deveSerializarUsuarioaoSalvar() {
		User user = new User("Tiago", 38);
		
		given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
			.body(user)
			.contentType(ContentType.JSON)
			//.log().all()
		.when()
			.post("/users")
		.then()
			//.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Tiago"))
		;
	}
	
	@Test
	public void deveDeserializarUsuario() {
		User user = new User("Tiago", 38);
		
		User resUser = given()
			.baseUri("http://restapi.wcaquino.me")
			.port(80)
			.body(user)
			.contentType(ContentType.JSON)
			//.log().all()
		.when()
			.post("/users")
		.then()
			//.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Tiago"))
			.extract().body().as(User.class)
		;
		
		System.out.println(resUser.getId());
	}
	
	@Test
	public void deveUtilizarURLParametrizavel() {
		User user = new User("Tiago", 38);
		
		User resUser = given()
			.body(user)
			.contentType(ContentType.JSON)
			//.log().all()
		.when()
			.post("/users")
		.then()
			//.log().all()
			.statusCode(201)
			.body("id", is(notNullValue()))
			.body("name", is("Tiago"))
			.extract().body().as(User.class)
		;
		
		given()
			.pathParam("id", resUser.getId())
			//.log().all()
		.when()
			.get("/users/{id}")
		.then()
			//.log().all()
			.statusCode(200)
			.body("name", is("Tiago"))
		;
	}
	
	@Test
	public void deveRemoverUsuario() {
		given()
			.pathParam("id", 1)
			//.log().all()
		.when()
			.delete("/users/{id}")
		.then()
			//.log().all()
			.statusCode(204)
		;
	}
	
	@Test
	public void deveAtualizarUsuario() {
		User user = new User("Tiago Eduardo", 38);
		
		given()
			.contentType(ContentType.JSON)
			.body(user)
			.pathParam("id", 1)
			//.log().all()
		.when()
			.put("/users/{id}")
		.then()
			//.log().all()
			.statusCode(200)
			.body("name", is("Tiago Eduardo"))
			.body("age", is(38))
		;
	}
	
	@Test
	public void deveAtualizarApenasNomeUsuario() {
		User user = new User("Tiago Eduardo");
		
		given()
			.contentType(ContentType.JSON)
			.body(user)
			.pathParam("id", 1)
			//.log().all()
		.when()
			.put("/users/{id}")
		.then()
			//.log().all()
			.statusCode(200)
			.body("name", is("Tiago Eduardo"))
		;
	}
	
	@Test
	public void deveEnviarTokenNoHead() {
		String token = "laaslkjqweoiru1312390iowjdflkj329u0089";

		String contentType = given()
			.header("Authorization", token)
			//.log().all()
		.when()
			.get("/users/1")
		.then()
			//.log().all()
			.statusCode(200)
			.extract().header("Content-Type");
		;
		
		System.out.println(contentType);
	}
	
	@Test
	public void deveEnviarValorViaQuery() {
		// http://restapi.wcaquino.me/v2/users?format=xml
		given()
			//.log().all()
			.basePath("/v2")
			.queryParam("format", "xml")
		.when()
			.get("/users")
		.then()
			//.log().all()
			.statusCode(200)
		;
	}
	
	@Test
	public void deveValidarJsonSchema() {
		given()
		.when()
			.get("/users")
		.then()
			.log().all()
			.statusCode(200)
			.body(JsonSchemaValidator.matchesJsonSchemaInClasspath("userJsonSchema.json"))
		;

	}
}

