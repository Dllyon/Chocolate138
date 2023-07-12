// 1 - Pacote
package webtest;
// 2 - Bibliotecas

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

// 3 - Classe
public class seleniumSimples {
    //3.1 Atributos
    WebDriver driver;// objeto do Selenium WebDriver

    //3.2 Funções e Métodos de Apoio
    // Não vamos criar

    // 3.3 Antes do Teste
    @BeforeMethod
    public void setUp(){
       // Instalar e configurar o driver do navegador/browser
       WebDriverManager.chromedriver().setup();// Download e instalação do chrome Drive

       //Configurar as opções para o Driver do Navegador (a partir do selenium 4.8.0)
        ChromeOptions options = new ChromeOptions();//Objeto de configuração para o chrome Driver
        options.addArguments("--remote-allow-origins*");//permitor qualquer origem remota

       // Instanciar o Selenium como driver de um navegador especifico
       driver = new ChromeDriver(options);

       //configurar tempo
       driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));
       driver.manage().window().maximize();//maximiza a janela do navegador
    }

    // 3.4 Depois dp Teste
    @AfterMethod
    public void tearDown() {
        driver.close(); //destroi o objeto do Selenium WebDriver
    }

    // 3.5 Teste em si

}
