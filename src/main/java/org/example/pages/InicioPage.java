package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/** Página inicial e navegação geral (cabeçalho, links e rotas). */
public class InicioPage extends BasePage {

    @FindBy(linkText = "Agendar atendimento")
    private WebElement linkAgendar;

    @FindBy(partialLinkText = "Ver serviços")
    private WebElement linkServicos;

    public InicioPage(WebDriver driver) {
        super(driver);
    }

    public InicioPage abrirRota(String urlBase, String rota) {
        driver.get(urlBase + rota);
        return this;
    }

    public InicioPage esperarTituloConter(String trecho) {
        esperarTitulo(trecho);
        return this;
    }

    public String clicarAgendar() {
        clicar(linkAgendar);
        esperarRota("#/agendar");
        return driver.getCurrentUrl();
    }

    public String clicarVerServicos() {
        clicar(linkServicos);
        esperarTitulo("Serviços");
        return driver.getTitle();
    }
}
