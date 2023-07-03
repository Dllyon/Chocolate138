package apitest;

import com.google.gson.Gson;
import entities.AccountEntity;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TestAccount {
    static String userId;
    static String ct = "application/json";
    static String jsonBody;
    static String uri = "https://bookstore.toolsqa.com/Account/v1/";
    static Response resposta;
    static String token;

    static Gson gson = new Gson();
    static AccountEntity account = new AccountEntity();

    // Método para criar usuário
    @Test(priority = 1)
    public static void testCreateUser() {
        account.userName = "charlie503";
        account.password = "P@ss0rd1";
        jsonBody = gson.toJson(account);

        resposta = (Response) given()
                .contentType(ct)
                .log().all()
                .body(jsonBody)
                .when()
                .post(uri + "User")
                .then()
                .log().all()
                .statusCode(201)
                .body("username", is(account.userName))
                .extract();

        userId = resposta.jsonPath().getString("userID");
        System.out.println("UserID extraido: " + userId);
    }

    // Método para gerar token
    @Test(priority = 2)
    public static void testGenerateToken(ITestContext context) {
        resposta = (Response) given()
                .contentType(ct)
                .log().all()
                .body(jsonBody)
                .when()
                .post(uri + "GenerateToken")
                .then()
                .log().all()
                .statusCode(200)
                .body("status", is("Success"))
                .body("result", is("User authorized successfully."))
                .extract();

        token = resposta.jsonPath().getString("token");
        context.setAttribute("token", token);
        System.out.println("Token: " + token);

        Assert.assertTrue(token.length() != 0);
    }

    @Test(priority = 3)
    public void testAuthorized() {
        given()
                .contentType(ct)
                .log().all()
                .body(jsonBody)
                .when()
                .post(uri + "Authorized")
                .then()
                .log().all()
                .statusCode(200);
    }

    @Test(priority = 4)
    public void testResearchUserNotAuthorized() {
        given()
                .contentType(ct)
                .log().all()
                .when()
                .get(uri + "User/" + userId)
                .then()
                .log().all()
                .statusCode(401)
                .body("code", is("1200"))
                .body("message", is("User not authorized!"));
    }

    @Test(priority = 5)
    public void testResearchUser() {
        given()
                .contentType(ct)
                .log().all()
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri + "User/" + userId)
                .then()
                .log().all()
                .statusCode(200)
                .body("userId", is(userId))
                .body("username", is(account.userName));
    }

    @Test(priority = 20)
    public static void testDeleteUser() {
        given()
                .log().all()
                .contentType(ct)
                .header("Authorization", "Bearer " + token)
                .when()
                .delete(uri + "User/" + userId)
                .then()
                .log().all()
                .statusCode(204);
    }
}