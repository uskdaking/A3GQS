package org.example.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

/** Page Object da "Área da equipe" (no protótipo, entrada por perfil, sem senha). */
public class EquipePage extends BasePage {

    @FindBy(css = "[data-perfil='coordenacao']")
    private WebElement btnCoordenacao;

    @FindBy(id = "qual-aluno")
    private WebElement selAluno;

    @FindBy(css = "[data-perfil='aluno']")
    private WebElement btnAluno;

    @FindBy(css = "a[href='#/equipe/sair']")
    private WebElement linkSair;

    public EquipePage(WebDriver driver) {
        super(driver);
    }

    public EquipePage abrir(String urlBase) {
        driver.get(urlBase + "#/equipe");
        esperarTitulo("Área da equipe");
        return this;
    }

    public EquipePage entrarComoCoordenacao() {
        clicar(btnCoordenacao);
        esperarRota("#/equipe/agenda");
        return this;
    }

    public EquipePage selecionarAluno(String nome) {
        new Select(selAluno).selectByVisibleText(nome);
        return this;
    }

    public EquipePage entrarComoAluno() {
        clicar(btnAluno);
        esperarRota("#/equipe/");
        return this;
    }

    public EquipePage sair() {
        clicar(linkSair);
        esperarTitulo("Área da equipe");
        return this;
    }
}
