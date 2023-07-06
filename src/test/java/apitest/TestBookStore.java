package apitest;

import entities.LoanEntity;
import org.testng.annotations.*;
import org.testng.ITestContext;

import static apitest.TestAccount.gson;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class TestBookStore {
    String uri = "https://bookstore.toolsqa.com/BookStore/v1/";
    String ct = "application/json";
    TestAccount account = new TestAccount();
    LoanEntity isbn = new LoanEntity();
    String token;

    @BeforeClass
   // @BeforeMethod
    public void setUp(ITestContext context) {
        TestAccount.testCreateUser(context);
        TestAccount.testGenerateToken(context);
        token = context.getAttribute("token").toString();
    }
    @AfterClass //depois da classe
    //@AfterMethod
    public void tearDown()
    {
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
    @Test(priority = 2)
    public void testLoanBooks(ITestContext context){ // Emprestar livros
        // Configura

        // userId virá pelo context
        isbn.userId = context.getAttribute("userID").toString();//cód user
        isbn.collectionOfIsbns = new LoanEntity.ISBN[]{
                new LoanEntity.ISBN("9781449325862")
        };
        // isbn.isbn = "9781449325862";//cod livro



        // Dados de saida
        // statusCode = 201
        // Retorne o isbn do livro emprestado (echo)

        // Executa
        given()
                .log().all()
                .contentType(ct)
                .header("Authorization", "Bearer " + context.getAttribute("token"))
                .body(gson.toJson(isbn))
        .when()
                .post(uri + "Books")
        // Valida
        .then()
                .log().all()
                .statusCode(201)
                .body("isbn", is(isbn.isbn))
        ;
    }
    @Test(priority = 3)
    public void testUpdateLoan(ITestContext context){ //Atualizar com quem está qual livro
        //Configura
        // Dados de entrada
        // userID é extraidop no BeforeMethod
        String isbnAntigo = "9781449325862"; // Livro a substituir
        String isbnNovo = "9781449331818";// novo livro a ser emprestado

        // alimentar a classe LoanEntity apenas com o código do user e o isbn
        isbn = new LoanEntity();// reiniciando o objeto da classe LoanEntity
        isbn.userId = context.getAttribute("userID").toString();//cód do user
        isbn.isbn = isbnNovo;//cód do livro que estava com o user

        //Executa
        given()
                .log().all()
                .contentType(ct)
                .header("Authorization", "Bearer " + context.getAttribute("token"))
                .body(gson.toJson(isbn))
        .when()
                .put(uri + "Books/" + isbnAntigo)
        //valida
        .then()
                .log().all()
                .statusCode(200)
                .body("books[0].isbn", is(isbnNovo))
        ;
    }
    @Test(priority = 4)
    public void testDeleteLoans(ITestContext context){
        //Configura
        // Dados de entrada
        // userID é extraido no BeforeClass
        //statusCode = 204

        //Executa
        given()
                .log().all()
                .contentType(ct)
                .header("Authorization", "Bearer " + context.getAttribute("token"))
        .when()
                .delete(uri + "Books?UserId=" + context.getAttribute("userID"))
        //valida
        .then()
                .log().all()
                .statusCode(204)
        ;
    }

}