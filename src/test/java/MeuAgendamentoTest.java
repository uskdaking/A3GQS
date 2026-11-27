import org.example.base.BaseTest;
import org.example.pages.MeuAgendamentoPage;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

/**
 * Cenário: Consulta e cancelamento em "Meu agendamento".
 * Casos CT10 a CT13. Usa o agendamento de exemplo que o próprio site indica
 * ("Para testar: código ..., ou telefone ... no dia ...").
 */
public class MeuAgendamentoTest extends BaseTest {

    private MeuAgendamentoPage abrirComExemplo() {
        MeuAgendamentoPage page = new MeuAgendamentoPage(getDriver()).abrir(getUrl());
        if (!page.temDadosDeExemplo()) {
            throw new SkipException("O site não exibiu dados de exemplo para teste");
        }
        return page;
    }

    @Test(priority = 1, description = "CT10 - Consultar com código inexistente")
    public void ct10CodigoInexistente() {
        MeuAgendamentoPage page = new MeuAgendamentoPage(getDriver()).abrir(getUrl());

        page.informarCodigo("NAF-ZZZZZZ").clicarConsultar();

        Assert.assertTrue(page.buscarErroCampo("codigo").contains("Não encontramos este código")
                        || page.buscarErros().toString().contains("Não encontramos este código"),
                "Mensagem de código inexistente não exibida");
    }

    @Test(priority = 2, description = "CT11 - Consultar agendamento pelo código")
    public void ct11ConsultarPorCodigo() {
        MeuAgendamentoPage page = abrirComExemplo();
        String codigo = page.codigoExemplo();

        page.informarCodigo(codigo).clicarConsultar().esperarResultado();

        Assert.assertTrue(getDriver().getPageSource().contains("Agendamento " + codigo));
        Assert.assertEquals(page.buscarSituacao(), "Confirmado");
        Assert.assertTrue(page.existeBotaoRemarcar(), "Botão Remarcar não exibido");
    }

    @Test(priority = 3, description = "CT12 - Localizar agendamento por telefone e data (perdi o código)")
    public void ct12BuscarPorTelefoneEData() {
        MeuAgendamentoPage page = abrirComExemplo();
        String codigo = page.codigoExemplo();

        page.informarTelefone(page.telefoneExemplo())
                .informarData(page.dataExemploIso())
                .clicarProcurar()
                .esperarResultado();

        Assert.assertTrue(getDriver().getPageSource().contains(codigo),
                "O agendamento " + codigo + " não foi localizado por telefone e data");
    }

    @Test(priority = 4, description = "CT13 - Cancelar agendamento e liberar a vaga")
    public void ct13CancelarAgendamento() {
        MeuAgendamentoPage page = abrirComExemplo();

        page.informarCodigo(page.codigoExemplo()).clicarConsultar().esperarResultado()
                .clicarCancelarAgendamento()
                .selecionarPrimeiroMotivo()
                .confirmarCancelamento();

        Assert.assertTrue(page.buscarMensagemSucesso().contains("Agendamento cancelado"),
                page.buscarMensagemSucesso());
        Assert.assertEquals(page.buscarSituacao(), "Cancelado");
    }
}
