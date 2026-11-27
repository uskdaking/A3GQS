package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    /** O site é uma SPA com rotas por hash (#/...). Espera a rota trocar. */
    protected void esperarRota(String hash) {
        wait.until(ExpectedConditions.urlContains(hash));
    }

    protected void esperarTitulo(String trecho) {
        wait.until(ExpectedConditions.titleContains(trecho));
    }

    /** Clique via JavaScript para elementos que podem estar cobertos (cabeçalho fixo etc.). */
    protected void clicar(WebElement elemento) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block:'center'});", elemento);
        try {
            elemento.click();
        } catch (org.openqa.selenium.ElementNotInteractableException e) {
            // radios/checkboxes estilizados podem ficar escondidos atrás do rótulo
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", elemento);
        }
    }

    /**
     * Marca um checkbox/radio e espera ele ficar selecionado.
     * Na SPA a tela é redesenhada logo após a troca de rota; se o clique cair no
     * DOM antigo, a espera tenta de novo (evita teste instável/"flaky").
     */
    protected void marcar(WebElement elemento) {
        wait.ignoring(StaleElementReferenceException.class).until(d -> {
            if (!elemento.isSelected()) clicar(elemento);
            return elemento.isSelected();
        });
    }

    /** Preenche um campo apagando o conteúdo anterior. */
    protected void escrever(WebElement campo, String texto) {
        campo.clear();
        campo.sendKeys(texto);
    }

    /** Lista de mensagens do quadro "Corrija N itens para continuar". */
    public List<String> buscarErros() {
        return driver.findElements(By.cssSelector(".alerta-erro li")).stream()
                .map(WebElement::getText).toList();
    }

    /** Texto da mensagem de erro exibida embaixo de um campo (p.erro-campo#erro-xxx). */
    public String buscarErroCampo(String campo) {
        List<WebElement> el = driver.findElements(By.id("erro-" + campo));
        return el.isEmpty() ? "" : el.get(0).getText().trim();
    }

    public String buscarTitulo() {
        return driver.getTitle();
    }

    public String buscarUrlAtual() {
        return driver.getCurrentUrl();
    }

    public String buscarH1() {
        return driver.findElement(By.cssSelector("main h1")).getText();
    }
}
