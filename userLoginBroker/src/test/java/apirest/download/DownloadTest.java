package apirest.download;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;

public class DownloadTest {
	
	@BeforeAll
	public static void setup() {
		RestAssured.baseURI = "http://restapi.wcaquino.me";
		RestAssured.port = 80;
	}
	
	@Test
	public void deveBaixarArquivo() throws IOException {
		byte[] imageBinary = given()
			.log().all()
		.when()
			.get("/download")
		.then()
			//.log().all()
			.statusCode(200)
			.contentType("image/jpeg")
			.extract().asByteArray()
		;
		
		//criar arquivo vazio
		File imageFile = new File("src/test/resources/image.jpg");
		
		//criar streaming para escrever no arquivo
		OutputStream out = new FileOutputStream(imageFile);
		
		try {
			//escrever no arquivo
			out.write(imageBinary);
		}finally {
			out.close();
		}
		
		System.out.println(imageFile.length());
		
	}


}
