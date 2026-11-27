package org.example.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

/**
 * Page Object do fluxo "Agendar atendimento" (4 etapas):
 * 1) Serviço  2) Documentos  3) Dia e horário  4) Seus dados  -> Confirmação.
 * Os métodos retornam a própria página, no mesmo estilo encadeado da aula.
 */
public class AgendamentoPage extends BasePage {

    // ---------- Etapa 1: serviço ----------
    @FindBy(id = "srv-cpf")
    private WebElement rbCpf;

    @FindBy(id = "srv-irpf")
    private WebElement rbImpostoRenda;

    @FindBy(id = "srv-mei")
    private WebElement rbMei;

    @FindBy(id = "srv-pendencias")
    private WebElement rbPendencias;

    @FindBy(id = "srv-nao-sei")
    private WebElement rbNaoSei;

    @FindBy(id = "assunto")
    private WebElement txtAssunto;

    // botão "Continuar" das etapas 1, 2 e 4 é o submit do formulário
    @FindBy(css = "main button[type='submit']")
    private WebElement btnSubmit;

    // ---------- Etapa 2: documentos ----------
    @FindBy(id = "leu")
    private WebElement cbEntendiDocumentos;

    // ---------- Etapa 3: dia e horário ----------
    @FindBy(css = "main button.dia-livre")
    private List<WebElement> diasLivres;

    @FindBy(css = "main .horario")
    private List<WebElement> horarios;

    @FindBy(id = "continuar-horario")
    private WebElement btnContinuarHorario;

    // ---------- Etapa 4: dados ----------
    @FindBy(id = "nome")
    private WebElement txtNome;

    @FindBy(id = "telefone")
    private WebElement txtTelefone;

    @FindBy(id = "email")
    private WebElement txtEmail;

    @FindBy(id = "aceite")
    private WebElement cbAceite;

    @FindBy(xpath = "//main//button[normalize-space()='Confirmar agendamento']")
    private WebElement btnConfirmar;

    // ---------- Confirmação ----------
    @FindBy(css = "main p.codigo")
    private WebElement txtCodigo;

    @FindBy(css = "main dl.resumo")
    private WebElement dlResumo;

    public AgendamentoPage(WebDriver driver) {
        super(driver);
    }

    public AgendamentoPage abrir(String urlBase) {
        driver.get(urlBase + "#/agendar");
        esperarTitulo("Serviço");
        return this;
    }

    // ===== Etapa 1 =====
    public AgendamentoPage selecionarServico(String servico) {
        switch (servico) {
            case "cpf" -> marcar(rbCpf);
            case "irpf" -> marcar(rbImpostoRenda);
            case "mei" -> marcar(rbMei);
            case "pendencias" -> marcar(rbPendencias);
            case "nao-sei" -> marcar(rbNaoSei);
            default -> throw new IllegalArgumentException("Serviço desconhecido: " + servico);
        }
        return this;
    }

    public AgendamentoPage informarAssunto(String assunto) {
        escrever(txtAssunto, assunto);
        return this;
    }

    public AgendamentoPage clicarContinuar() {
        clicar(btnSubmit);
        return this;
    }

    public AgendamentoPage avancarParaDocumentos() {
        clicarContinuar();
        esperarRota("#/agendar/documentos");
        esperarTitulo("Documentos");
        return this;
    }

    // ===== Etapa 2 =====
    public AgendamentoPage marcarEntendiDocumentos() {
        marcar(cbEntendiDocumentos);
        return this;
    }

    public AgendamentoPage avancarParaHorario() {
        clicarContinuar();
        esperarRota("#/agendar/horario");
        esperarTitulo("Dia e horário");
        return this;
    }

    // ===== Etapa 3 =====
    public AgendamentoPage selecionarPrimeiroDiaLivre() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("main button.dia-livre")));
        clicar(diasLivres.get(0));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("main .horario")));
        return this;
    }

    /** Escolhe o primeiro horário que ainda tem vaga (botão habilitado). */
    public AgendamentoPage selecionarPrimeiroHorarioLivre() {
        WebElement livre = horarios.stream().filter(WebElement::isEnabled).findFirst()
                .orElseThrow(() -> new AssertionError("Nenhum horário com vaga no dia escolhido"));
        clicar(livre);
        return this;
    }

    /** Horários exibidos como "Esgotado". */
    public List<WebElement> buscarHorariosEsgotados() {
        return horarios.stream().filter(h -> h.getText().contains("Esgotado")).toList();
    }

    public List<WebElement> buscarHorariosComVaga() {
        return horarios.stream().filter(h -> h.getText().contains("vaga")).toList();
    }

    public int contarDiasLivres() {
        return diasLivres.size();
    }

    public AgendamentoPage clicarContinuarHorario() {
        clicar(btnContinuarHorario);
        return this;
    }

    public AgendamentoPage avancarParaDados() {
        clicarContinuarHorario();
        esperarRota("#/agendar/dados");
        esperarTitulo("Seus dados");
        return this;
    }

    // ===== Etapa 4 =====
    public AgendamentoPage informarNome(String nome) {
        escrever(txtNome, nome);
        return this;
    }

    public AgendamentoPage informarTelefone(String telefone) {
        escrever(txtTelefone, telefone);
        return this;
    }

    public AgendamentoPage informarEmail(String email) {
        escrever(txtEmail, email);
        return this;
    }

    public AgendamentoPage marcarAceitePrivacidade() {
        marcar(cbAceite);
        return this;
    }

    public AgendamentoPage clicarConfirmar() {
        clicar(btnConfirmar);
        return this;
    }

    /** Sai do campo telefone com TAB (a máscara é aplicada quando o campo perde o foco). */
    public AgendamentoPage sairDoCampoTelefone() {
        txtTelefone.sendKeys(Keys.TAB);
        return this;
    }

    public String buscarValorTelefone() {
        return txtTelefone.getAttribute("value");
    }

    // ===== Confirmação =====
    public AgendamentoPage esperarConfirmacao() {
        esperarRota("#/confirmacao");
        esperarTitulo("Agendamento confirmado");
        return this;
    }

    public String buscarCodigo() {
        return txtCodigo.getText().trim();
    }

    public String buscarResumo() {
        return dlResumo.getText();
    }

    // ===== Atalhos para chegar a uma etapa =====
    public AgendamentoPage irAteEtapaHorario(String urlBase, String servico) {
        return abrir(urlBase)
                .selecionarServico(servico)
                .avancarParaDocumentos()
                .marcarEntendiDocumentos()
                .avancarParaHorario();
    }

    public AgendamentoPage irAteEtapaDados(String urlBase, String servico) {
        return irAteEtapaHorario(urlBase, servico)
                .selecionarPrimeiroDiaLivre()
                .selecionarPrimeiroHorarioLivre()
                .avancarParaDados();
    }
}
