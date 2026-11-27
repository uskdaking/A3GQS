package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Page Object da tela "Meu agendamento": consultar, cancelar e remarcar. */
public class MeuAgendamentoPage extends BasePage {

    @FindBy(id = "codigo")
    private WebElement txtCodigo;

    @FindBy(xpath = "//main//button[normalize-space()='Consultar']")
    private WebElement btnConsultar;

    @FindBy(id = "busca-telefone")
    private WebElement txtTelefone;

    @FindBy(id = "busca-data")
    private WebElement txtData;

    @FindBy(xpath = "//main//button[normalize-space()='Procurar']")
    private WebElement btnProcurar;

    @FindBy(css = "main p.nota")
    private List<WebElement> notas;

    @FindBy(css = "main [data-acao='cancelar']")
    private WebElement btnCancelar;

    @FindBy(css = "main [data-acao='remarcar']")
    private WebElement btnRemarcar;

    @FindBy(css = "main input[name='motivo']")
    private List<WebElement> rbMotivos;

    @FindBy(xpath = "//main//button[normalize-space()='Sim, cancelar']")
    private WebElement btnSimCancelar;

    @FindBy(css = "#resultado dl.resumo")
    private WebElement dlResumo;

    public MeuAgendamentoPage(WebDriver driver) {
        super(driver);
    }

    public MeuAgendamentoPage abrir(String urlBase) {
        driver.get(urlBase + "#/meu-agendamento");
        esperarTitulo("Meu agendamento");
        return this;
    }

    public MeuAgendamentoPage informarCodigo(String codigo) {
        escrever(txtCodigo, codigo);
        return this;
    }

    public MeuAgendamentoPage clicarConsultar() {
        clicar(btnConsultar);
        return this;
    }

    public MeuAgendamentoPage informarTelefone(String telefone) {
        escrever(txtTelefone, telefone);
        return this;
    }

    /** Campo type=date: o formato digitado muda com o idioma do navegador, então o valor ISO é definido via JS. */
    public MeuAgendamentoPage informarData(String dataIso) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = arguments[1];" +
                "arguments[0].dispatchEvent(new Event('input', {bubbles:true}));" +
                "arguments[0].dispatchEvent(new Event('change', {bubbles:true}));", txtData, dataIso);
        return this;
    }

    public MeuAgendamentoPage clicarProcurar() {
        clicar(btnProcurar);
        return this;
    }

    public MeuAgendamentoPage esperarResultado() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("#resultado dl.resumo")));
        return this;
    }

    public String buscarResumo() {
        return dlResumo.getText();
    }

    /** Valor de "Situação" (último item do resumo). */
    public String buscarSituacao() {
        List<WebElement> dds = driver.findElements(By.cssSelector("#resultado dl.resumo dd"));
        return dds.isEmpty() ? "" : dds.get(dds.size() - 1).getText().trim();
    }

    public boolean existeBotaoRemarcar() {
        return !driver.findElements(By.cssSelector("main [data-acao='remarcar']")).isEmpty();
    }

    public MeuAgendamentoPage clicarCancelarAgendamento() {
        clicar(btnCancelar);
        return this;
    }

    public MeuAgendamentoPage selecionarPrimeiroMotivo() {
        marcar(rbMotivos.get(0));
        return this;
    }

    public MeuAgendamentoPage confirmarCancelamento() {
        clicar(btnSimCancelar);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main .alerta-sucesso")));
        return this;
    }

    public String buscarMensagemSucesso() {
        return driver.findElement(By.cssSelector("main .alerta-sucesso")).getText();
    }

    // ===== Dados de exemplo que o próprio site mostra ("Para testar: código ..., ou telefone ... no dia dd/mm/aaaa") =====
    private Matcher dica() {
        String texto = notas.stream().map(WebElement::getText)
                .filter(t -> t.contains("Para testar")).findFirst().orElse("");
        Matcher m = Pattern.compile("código (NAF-[A-Z0-9]+), ou telefone (.+?) no dia (\\d{2})/(\\d{2})/(\\d{4})")
                .matcher(texto);
        return m.find() ? m : null;
    }

    public boolean temDadosDeExemplo() {
        return dica() != null;
    }

    public String codigoExemplo() {
        return dica().group(1);
    }

    public String telefoneExemplo() {
        return dica().group(2).trim();
    }

    public String dataExemploIso() {
        Matcher m = dica();
        return m.group(5) + "-" + m.group(4) + "-" + m.group(3);
    }
}
