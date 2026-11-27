import org.example.base.BaseTest;
import org.example.pages.EquipePage;
import org.example.pages.InicioPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Cenário: Navegação pelas páginas públicas e acesso à área da equipe.
 * Casos CT14 a CT17.
 */
public class NavegacaoTest extends BaseTest {

    @DataProvider(name = "rotasPublicas")
    public Object[][] rotasPublicas() {
        return new Object[][]{
                {"#/", "Início"},
                {"#/servicos", "Serviços"},
                {"#/privacidade", "Aviso de privacidade"},
                {"#/meu-agendamento", "Meu agendamento"},
                {"#/equipe", "Área da equipe"}
        };
    }

    @Test(priority = 1, dataProvider = "rotasPublicas", description = "CT14 - Abrir páginas públicas")
    public void ct14PaginasPublicas(String rota, String tituloEsperado) {
        InicioPage page = new InicioPage(getDriver());

        page.abrirRota(getUrl(), rota).esperarTituloConter(tituloEsperado);

        Assert.assertTrue(page.buscarTitulo().startsWith(tituloEsperado), page.buscarTitulo());
        Assert.assertTrue(page.buscarTitulo().endsWith("NAF FADERGS"), page.buscarTitulo());
    }

    @Test(priority = 2, description = "CT15 - Rota inexistente mostra página não encontrada")
    public void ct15RotaInexistente() {
        InicioPage page = new InicioPage(getDriver());

        page.abrirRota(getUrl(), "#/rota-que-nao-existe").esperarTituloConter("Página não encontrada");

        Assert.assertEquals(page.buscarH1(), "Página não encontrada");
    }

    @Test(priority = 3, description = "CT16 - Links da página inicial")
    public void ct16LinksPaginaInicial() {
        InicioPage page = new InicioPage(getDriver());

        Assert.assertTrue(page.clicarAgendar().contains("#/agendar"));
        page.abrirRota(getUrl(), "#/").esperarTituloConter("Início");
        Assert.assertTrue(page.clicarVerServicos().startsWith("Serviços"));
    }

    @Test(priority = 4, description = "CT17 - Entrar e sair da área da equipe como coordenação")
    public void ct17AcessoCoordenacao() {
        EquipePage page = new EquipePage(getDriver());

        page.abrir(getUrl()).entrarComoCoordenacao();
        Assert.assertEquals(page.buscarTitulo(), "Agenda do dia · NAF FADERGS");
        Assert.assertEquals(page.buscarH1(), "Agenda do dia");

        page.sair();
        Assert.assertTrue(page.buscarTitulo().startsWith("Área da equipe"), page.buscarTitulo());
    }
}
