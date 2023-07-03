package apitest;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.ITestContext;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;

public class BookStore {
    String uri = "https://bookstore.toolsqa.com/BookStore/v1/";
    String ct = "application/json";
    String token;

    @BeforeMethod
    public void setUp(ITestContext context) {
        TestAccount.testCreateUser();
        TestAccount.testGenerateToken(context);
        token = context.getAttribute("token").toString();
    }

    @AfterMethod
    public void tearDown() {
        TestAccount.testDeleteUser();
    }

    @Test(priority = 1)
    public void testResearchBooks(ITestContext context) {
        given()
                .log().all()
                .contentType(ct)
                .header("Authorization", "Bearer " + token)
                .when()
                .get(uri + "Books")
                .then()
                .log().all()
                .statusCode(200);
    }
}