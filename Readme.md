# NAF Inteligente – Testes automatizados

Rotina de testes funcionais do site de agendamento do **NAF FADERGS** (https://nafinteligente.netlify.app/), feita na disciplina de **Garantia da Qualidade de Software (FADERGS 2026/2)**.

O projeto segue a mesma estrutura do projeto da aula ([adaltofadergs/2026_2_GarantiaQualiadeDeSoftware](https://github.com/adaltofadergs/2026_2_GarantiaQualiadeDeSoftware)):

- **Java + Maven + Selenium WebDriver + TestNG**;
- **Page Object** com `PageFactory` e `@FindBy` (por `id`, `name`, `css`, `xpath`, `linkText` e `partialLinkText`);
- `BaseTest` com pré-condição (`@BeforeClass`) que abre o navegador e entrega o `driver` para as páginas;
- métodos encadeados (cada ação devolve a própria página) e validações com `Assert`.

## Estrutura

```
src/main/java/org/example/base/BaseTest.java          abre/fecha o navegador e restaura os dados antes de cada teste
src/main/java/org/example/pages/BasePage.java         PageFactory, esperas e leitura das mensagens de erro
src/main/java/org/example/pages/AgendamentoPage.java   fluxo de agendamento em 4 etapas
src/main/java/org/example/pages/MeuAgendamentoPage.java consulta e cancelamento
src/main/java/org/example/pages/InicioPage.java        página inicial e rotas
src/main/java/org/example/pages/EquipePage.java        área da equipe
src/test/java/AgendamentoTest.java                     CT01 a CT09
src/test/java/MeuAgendamentoTest.java                  CT10 a CT13
src/test/java/NavegacaoTest.java                       CT14 a CT17
testng.xml                                             suíte com os 3 cenários
docs/PLANO_DE_TESTES.md                                plano de testes (cenários e casos)
docs/RESULTADOS.md                                     resultado da última execução
```

## Como executar

**IntelliJ (como na aula):** abrir a pasta como projeto Maven e rodar `testng.xml` ou qualquer classe de teste. Por padrão usa o **Microsoft Edge**, com janela.
Não é preciso baixar o `msedgedriver.exe`: o Selenium Manager baixa o driver correto sozinho.

**Linha de comando:**

```bash
mvn test                                          # Edge, com janela
mvn test -Dbrowser=chrome                         # Chrome
mvn test -Dbrowser=chrome -Dheadless=true         # sem janela (igual ao GitHub Actions)
mvn test -DbaseUrl=http://localhost:8080/         # outra versão do site
```

Os relatórios do TestNG ficam em `target/surefire-reports/` (abrir `index.html` ou `emailable-report.html`).

## Integração contínua

O workflow `.github/workflows/testes.yml` executa toda a suíte no **GitHub Actions** (Chrome headless) a cada push na `main`, toda segunda-feira e sob demanda (aba *Actions* → *Run workflow*). A tabela de resultados aparece no resumo da execução e os relatórios do TestNG ficam disponíveis como artefato.

## Observação

O site é um protótipo: os dados são fictícios e ficam no `localStorage` do navegador. Por isso cada teste começa limpando o `localStorage`, o que faz o site recriar os dados de exemplo e deixa os testes independentes entre si.
