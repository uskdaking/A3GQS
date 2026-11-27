import org.example.base.BaseTest;
import org.example.pages.AgendamentoPage;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Cenário: Agendamento de atendimento no NAF FADERGS.
 * Casos CT01 a CT09 do plano de testes (docs/PLANO_DE_TESTES.md).
 */
public class AgendamentoTest extends BaseTest {

    @Test(priority = 1, description = "CT01 - Efetuar agendamento com sucesso")
    public void ct01AgendarComSucesso() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaDados(getUrl(), "cpf")
                .informarNome("Maria da Silva Teste")
                .informarTelefone("51988887777")
                .informarEmail("maria.teste@exemplo.com")
                .marcarAceitePrivacidade()
                .clicarConfirmar()
                .esperarConfirmacao();

        Assert.assertEquals(page.buscarH1(), "Agendamento confirmado");
        Assert.assertTrue(page.buscarCodigo().matches("NAF-[A-Z0-9]{6}"),
                "Código fora do padrão: " + page.buscarCodigo());
        String resumo = page.buscarResumo();
        Assert.assertTrue(resumo.contains("CPF"), resumo);
        Assert.assertTrue(resumo.contains("Confirmado"), resumo);
    }

    @Test(priority = 2, description = "CT02 - Impedir avanço sem escolher serviço")
    public void ct02BloquearSemServico() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.abrir(getUrl()).clicarContinuar();

        List<String> erros = page.buscarErros();
        Assert.assertEquals(erros.size(), 1, erros.toString());
        Assert.assertTrue(erros.get(0).contains("Escolha um serviço"), erros.toString());
        Assert.assertTrue(page.buscarUrlAtual().endsWith("#/agendar"), page.buscarUrlAtual());
    }

    @Test(priority = 3, description = "CT03 - Exigir assunto quando a pessoa não sabe o serviço")
    public void ct03ExigirAssuntoNaoSei() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.abrir(getUrl()).selecionarServico("nao-sei").clicarContinuar();
        Assert.assertTrue(page.buscarErros().toString().contains("Escreva o assunto"), page.buscarErros().toString());

        page.informarAssunto("Recebi uma carta da Receita sobre meu CPF").avancarParaDocumentos();
        Assert.assertTrue(page.buscarUrlAtual().contains("#/agendar/documentos"));
    }

    @Test(priority = 4, description = "CT04 - Exigir ciência dos documentos necessários")
    public void ct04ExigirCienciaDocumentos() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.abrir(getUrl()).selecionarServico("irpf").avancarParaDocumentos().clicarContinuar();

        Assert.assertTrue(page.buscarErros().toString().contains("Marque que você entendeu"),
                page.buscarErros().toString());
        Assert.assertTrue(page.buscarUrlAtual().contains("#/agendar/documentos"));
    }

    @Test(priority = 5, description = "CT05 - Exigir dia e horário antes de avançar")
    public void ct05ExigirDiaEHorario() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaHorario(getUrl(), "mei").clicarContinuarHorario();
        Assert.assertTrue(page.buscarErroCampo("dia").contains("Escolha um dia")
                        || page.buscarErros().toString().contains("Escolha um dia")
                        || getDriver().getPageSource().contains("Escolha um dia no calendário"),
                "Mensagem de dia obrigatório não exibida");

        page.selecionarPrimeiroDiaLivre().clicarContinuarHorario();
        Assert.assertEquals(page.buscarErroCampo("horario"), "Escolha um horário.");
        Assert.assertTrue(page.buscarUrlAtual().contains("#/agendar/horario"));
    }

    @Test(priority = 6, description = "CT06 - Horários esgotados não podem ser escolhidos")
    public void ct06HorariosEsgotadosDesabilitados() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaHorario(getUrl(), "pendencias");
        Assert.assertTrue(page.contarDiasLivres() > 0, "Nenhum dia com vaga no calendário");
        page.selecionarPrimeiroDiaLivre();

        for (WebElement esgotado : page.buscarHorariosEsgotados()) {
            Assert.assertFalse(esgotado.isEnabled(), "Horário esgotado habilitado: " + esgotado.getText());
        }
        for (WebElement comVaga : page.buscarHorariosComVaga()) {
            Assert.assertTrue(comVaga.isEnabled(), "Horário com vaga desabilitado: " + comVaga.getText());
        }
        Assert.assertFalse(page.buscarHorariosComVaga().isEmpty(), "Dia livre sem nenhum horário com vaga");
    }

    @Test(priority = 7, description = "CT07 - Validar campos obrigatórios dos dados pessoais")
    public void ct07CamposObrigatorios() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaDados(getUrl(), "cpf").clicarConfirmar();

        Assert.assertEquals(page.buscarErroCampo("nome"), "Escreva seu nome completo.");
        Assert.assertTrue(page.buscarErroCampo("telefone").startsWith("Escreva o telefone com DDD"));
        Assert.assertTrue(page.buscarErroCampo("aceite").startsWith("Marque que você leu o aviso de privacidade"));
        Assert.assertEquals(page.buscarErroCampo("email"), "", "E-mail é opcional e não deveria gerar erro");
        Assert.assertTrue(page.buscarUrlAtual().contains("#/agendar/dados"));
    }

    @Test(priority = 8, description = "CT08 - Rejeitar telefone sem DDD e e-mail incompleto")
    public void ct08TelefoneEEmailInvalidos() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaDados(getUrl(), "cpf")
                .informarNome("Maria da Silva Teste")
                .informarTelefone("5199")
                .informarEmail("maria@")
                .marcarAceitePrivacidade()
                .clicarConfirmar();

        Assert.assertTrue(page.buscarErroCampo("telefone").startsWith("Escreva o telefone com DDD"));
        Assert.assertTrue(page.buscarErroCampo("email").contains("e-mail parece incompleto"),
                page.buscarErroCampo("email"));
        Assert.assertEquals(page.buscarErroCampo("nome"), "");
        Assert.assertTrue(page.buscarUrlAtual().contains("#/agendar/dados"));
    }

    @Test(priority = 9, description = "CT09 - Formatar telefone ao sair do campo")
    public void ct09MascaraTelefone() {
        AgendamentoPage page = new AgendamentoPage(getDriver());

        page.irAteEtapaDados(getUrl(), "cpf").informarTelefone("51988887777").sairDoCampoTelefone();

        Assert.assertEquals(page.buscarValorTelefone(), "(51) 98888-7777");
    }
}
