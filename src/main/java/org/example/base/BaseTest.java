package org.example.base;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

/**
 * Mesma ideia do BaseTest da aula: abre o navegador antes da classe de teste
 * e entrega o driver para as páginas (Page Objects).
 *
 * Diferenças em relação à aula:
 *  - o alvo é o site publicado (NAF Inteligente), e não um HTML local;
 *  - o navegador é escolhido por parâmetro (-Dbrowser=edge|chrome, padrão edge, como na aula);
 *  - -Dheadless=true permite rodar sem janela (útil em integração contínua);
 *  - antes de cada teste os dados de exemplo são restaurados (localStorage limpo),
 *    para que um teste não dependa do que o outro fez;
 *  - o navegador é fechado ao final da classe.
 *
 * Não é preciso baixar msedgedriver.exe: o Selenium Manager baixa o driver certo sozinho.
 */
public class BaseTest {
    public String url;
    public WebDriver driver;

    @BeforeClass
    public void preCond() {
        url = System.getProperty("baseUrl", "https://nafinteligente.netlify.app/");
        String browser = System.getProperty("browser", "edge").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "false"));

        if (browser.equals("chrome")) {
            ChromeOptions options = new ChromeOptions();
            if (headless) options.addArguments("--headless=new", "--window-size=1366,900");
            options.addArguments("--lang=pt-BR");
            driver = new ChromeDriver(options);
        } else {
            EdgeOptions options = new EdgeOptions();
            if (headless) options.addArguments("--headless=new", "--window-size=1366,900");
            options.addArguments("--lang=pt-BR");
            driver = new EdgeDriver(options);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        if (!headless) driver.manage().window().maximize();
        driver.get(url);
    }

    /** Volta para o início com os dados de exemplo restaurados. */
    @BeforeMethod
    public void restaurarDados() {
        driver.get(url + "#/");
        ((JavascriptExecutor) driver).executeScript("localStorage.clear(); sessionStorage.clear();");
        driver.navigate().refresh();
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.titleContains("NAF FADERGS"));
    }

    @AfterClass(alwaysRun = true)
    public void posCond() {
        if (driver != null) driver.quit();
    }

    protected WebDriver getDriver() {
        return driver;
    }

    protected String getUrl() {
        return url;
    }
}
