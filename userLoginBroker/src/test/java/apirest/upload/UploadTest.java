package apirest.upload;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class UploadTest {
	
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		RestAssured.port = 80;
	}
	
	@Test
	public void deveFalharAoNaoEnviarArquivo() {
		given()
		.when()
			.post("/upload")
		.then()
			//.log().all()
			.statusCode(404)
			.body("error", is("Arquivo não enviado"))
		;
	}
	
	@Test
	public void deveEnviarArquivo() {
		File file = new File("src/test/resources/upload.txt");
		
		given()
			.multiPart("arquivo", file)
			.log().all()
		.when()
			.post("/upload")
		.then()
			.log().all()
			.statusCode(200)
			.time(lessThan(500L))
			.body("size", lessThanOrEqualTo(1000))
			.body("name", is("upload.txt"))
		;
	}
	
}
