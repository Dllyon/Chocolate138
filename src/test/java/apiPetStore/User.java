package apiPetStore;

import com.google.gson.Gson;
import entities.UserEntity;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class User {
    //Atributos
    String jsonBody;
    String ct = "Application/json";

    String uri = "https://petstore.swagger.io/v2/";

    Gson gson = new Gson(); // instanciar

    @Test
    public  void testCreateUser(){
        //configura
        // Dados de entrada
        UserEntity user = new UserEntity();
        user.id = 9999;
        user. username = "Charlie";
        user. firstName= "Charlie";
        user. lastName = "Brown";
        user. email = "charlie@iterasys.com.br";
        user. password = "123456";
        user.phone = "1199999999";
        user.userStatusString = 0;

        jsonBody = gson.toJson(user);

        //Dados de saida / Resultado esperado

        int code  = 200;
        String  type = "unknown";
        String message = "9999";

        //Executa
        given()
                .contentType(ct)  // tipo do conteudo
                .log().all()
                .body(jsonBody +"user")
        .when() // quando
                .post(uri +"user")
        //valida
        .then()
                .log().all()
                .statusCode(200)
                .body("code", is(code))
                .body("type", is(type))
                .body("message", is(message))
        ;

    }
}
