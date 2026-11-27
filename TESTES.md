# Testes automatizados – NAF Inteligente

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
```

## Como executar

**IntelliJ (como na aula):** abrir a pasta como projeto Maven e rodar `testng.xml` ou qualquer classe de teste. Por padrão usa o **Microsoft Edge**, com janela.
Não é preciso baixar o `msedgedriver.exe`: o Selenium Manager baixa o driver correto sozinho.

**Linha de comando:**

```bash
mvn test                                          # Edge, com janela
mvn test -Dbrowser=chrome                         # Chrome
mvn test -Dbrowser=chrome -Dheadless=true         # sem janela
mvn test -DbaseUrl=http://localhost:8080/         # outra versão do site
```

Os relatórios do TestNG ficam em `target/surefire-reports/` (abrir `index.html` ou `emailable-report.html`).

## Observação

O site é um protótipo: os dados são fictícios e ficam no `localStorage` do navegador. Por isso cada teste começa limpando o `localStorage`, o que faz o site recriar os dados de exemplo e deixa os testes independentes entre si.

## Roteiro de testes e resultado

Última execução completa: 21 de 21 execuções aprovadas (17 casos; o CT14 roda para 5 páginas).

| ID | Cenário | Caso de teste | Resultado esperado |
|---|---|---|---|
| CT01 | Agendamento | Agendar com dados válidos | Código NAF-XXXXXX e situação "Confirmado" |
| CT02 | Agendamento | Avançar sem escolher serviço | Bloqueia e pede "Escolha um serviço" |
| CT03 | Agendamento | "Não sei o serviço" sem assunto | Pede o assunto; com ele, avança |
| CT04 | Agendamento | Avançar sem confirmar documentos | Bloqueia na etapa Documentos |
| CT05 | Agendamento | Avançar sem dia e sem horário | Exige dia e, depois, horário |
| CT06 | Agendamento | Horários esgotados | Esgotados desabilitados; com vaga, habilitados |
| CT07 | Agendamento | Confirmar com dados vazios | Erros em nome, telefone e aceite; e-mail opcional |
| CT08 | Agendamento | Telefone sem DDD e e-mail incompleto | Erros nos dois campos; nada é agendado |
| CT09 | Agendamento | Máscara do telefone | 51988887777 → (51) 98888-7777 ao sair do campo |
| CT10 | Meu agendamento | Consultar código inexistente | "Não encontramos este código" |
| CT11 | Meu agendamento | Consultar pelo código | Resumo com situação "Confirmado" |
| CT12 | Meu agendamento | Localizar por telefone e data | Encontra o agendamento sem o código |
| CT13 | Meu agendamento | Cancelar agendamento | Situação "Cancelado" e vaga liberada |
| CT14 | Navegação e equipe | Abrir as 5 páginas públicas | Título correto em cada página |
| CT15 | Navegação e equipe | Acessar rota inexistente | "Página não encontrada" |
| CT16 | Navegação e equipe | Links da página inicial | Levam ao agendamento e aos serviços |
| CT17 | Navegação e equipe | Entrar e sair como coordenação | "Agenda do dia" e volta à área da equipe |
