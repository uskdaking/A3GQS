# Plano de Testes – NAF Inteligente (Agendamento do NAF FADERGS)

**Sistema sob teste:** https://nafinteligente.netlify.app/ (protótipo navegável, dados fictícios guardados no navegador)
**Tipo de teste:** funcional, caixa-preta, automatizado com Selenium WebDriver + TestNG (padrão Page Object)
**Autor:** Pedro Marshall Dell'Aglio – Garantia da Qualidade de Software – FADERGS 2026/2

Os casos abaixo seguem o mesmo modelo do plano de testes da disciplina (ID, descrição, nome, condição de aceite, pré-condições e passos com resultado esperado). A coluna **Automação** indica o método de teste que executa o caso.

---

## Cenário 1 – Agendamento de atendimento

**Objetivo:** validar todos os casos de teste relacionados ao agendamento de atendimento gratuito no NAF FADERGS, nas quatro etapas do fluxo (Serviço, Documentos, Dia e horário, Seus dados).
**Pré-requisitos:** acesso à internet, navegador Microsoft Edge ou Google Chrome atualizado, site disponível e dados de exemplo restaurados (o teste limpa o `localStorage` antes de cada caso).

### Caso de Teste 01
- **ID:** 01
- **Descrição:** valida a realização de um agendamento com todos os dados válidos.
- **Nome:** Efetuar agendamento com sucesso
- **Condição de aceite:** o sistema deve concluir o agendamento e exibir a tela "Agendamento confirmado" com um código no formato `NAF-XXXXXX`, o serviço escolhido e a situação "Confirmado".
- **Pré-condições:** existir pelo menos um dia com vaga no calendário.
- **Automação:** `AgendamentoTest.ct01AgendarComSucesso`

| Ação | Resultado esperado |
|---|---|
| Acessar "Agendar atendimento" | A etapa 1 de 4 (Serviço) é exibida com a lista de serviços |
| Selecionar "CPF" e clicar em Continuar | A etapa 2 (Documentos) é exibida com a lista do que levar |
| Marcar "Entendi o que preciso levar" e clicar em Continuar | A etapa 3 (Dia e horário) é exibida com o calendário |
| Escolher o primeiro dia com vaga e o primeiro horário com vaga e clicar em Continuar | A etapa 4 (Seus dados) é exibida com o resumo do serviço, dia e horário |
| Informar nome, telefone com DDD e e-mail válidos, marcar o aceite do aviso de privacidade e clicar em Confirmar agendamento | A tela "Agendamento confirmado" é exibida com o código, o resumo e a situação "Confirmado" |

### Caso de Teste 02
- **ID:** 02
- **Descrição:** valida o bloqueio do avanço quando nenhum serviço é escolhido.
- **Nome:** Avançar sem escolher serviço
- **Condição de aceite:** o sistema não deve avançar e deve exibir a mensagem "Escolha um serviço ou "Não sei qual serviço escolher"".
- **Pré-condições:** estar na etapa 1 do agendamento.
- **Automação:** `AgendamentoTest.ct02BloquearSemServico`

| Ação | Resultado esperado |
|---|---|
| Acessar "Agendar atendimento" | A etapa 1 (Serviço) é exibida |
| Clicar em Continuar sem selecionar serviço | O quadro "Corrija 1 item para continuar" é exibido com a mensagem de serviço obrigatório e a tela permanece na etapa 1 |

### Caso de Teste 03
- **ID:** 03
- **Descrição:** valida a obrigatoriedade do assunto quando a pessoa escolhe "Não sei qual serviço escolher".
- **Nome:** Exigir assunto para "Não sei qual serviço"
- **Condição de aceite:** sem assunto, o sistema exibe "Escreva o assunto em poucas palavras."; com assunto, avança para Documentos.
- **Pré-condições:** estar na etapa 1 do agendamento.
- **Automação:** `AgendamentoTest.ct03ExigirAssuntoNaoSei`

| Ação | Resultado esperado |
|---|---|
| Selecionar "Não sei qual serviço escolher" e clicar em Continuar | É exibida a mensagem pedindo o assunto e a tela permanece na etapa 1 |
| Escrever o assunto e clicar em Continuar | A etapa 2 (Documentos) é exibida |

### Caso de Teste 04
- **ID:** 04
- **Descrição:** valida que a pessoa precisa confirmar que leu a lista de documentos.
- **Nome:** Exigir ciência dos documentos
- **Condição de aceite:** o sistema não avança e exibe "Marque que você entendeu o que precisa levar."
- **Pré-condições:** serviço escolhido na etapa 1.
- **Automação:** `AgendamentoTest.ct04ExigirCienciaDocumentos`

| Ação | Resultado esperado |
|---|---|
| Selecionar "Imposto de Renda" e clicar em Continuar | A etapa 2 é exibida com os documentos do serviço |
| Clicar em Continuar sem marcar a confirmação | A mensagem de confirmação obrigatória é exibida e a tela permanece na etapa 2 |

### Caso de Teste 05
- **ID:** 05
- **Descrição:** valida a obrigatoriedade de dia e horário.
- **Nome:** Exigir dia e horário
- **Condição de aceite:** sem dia, o sistema exibe "Escolha um dia no calendário."; com dia e sem horário, exibe "Escolha um horário."
- **Pré-condições:** etapas 1 e 2 concluídas.
- **Automação:** `AgendamentoTest.ct05ExigirDiaEHorario`

| Ação | Resultado esperado |
|---|---|
| Na etapa 3, clicar em Continuar sem escolher dia | A mensagem de dia obrigatório é exibida |
| Escolher um dia com vaga e clicar em Continuar sem escolher horário | A mensagem "Escolha um horário." é exibida e a tela permanece na etapa 3 |

### Caso de Teste 06
- **ID:** 06
- **Descrição:** valida que horários sem vaga não podem ser selecionados.
- **Nome:** Bloquear horários esgotados
- **Condição de aceite:** horários marcados como "Esgotado" ficam desabilitados e horários com vaga ficam habilitados.
- **Pré-condições:** etapas 1 e 2 concluídas.
- **Automação:** `AgendamentoTest.ct06HorariosEsgotadosDesabilitados`

| Ação | Resultado esperado |
|---|---|
| Na etapa 3, escolher o primeiro dia com vaga | Os horários do dia são exibidos com a quantidade de vagas ou "Esgotado" |
| Verificar os botões de horário | Todo horário "Esgotado" está desabilitado e todo horário com vaga está habilitado |

### Caso de Teste 07
- **ID:** 07
- **Descrição:** valida os campos obrigatórios da etapa de dados pessoais.
- **Nome:** Confirmar sem preencher os dados
- **Condição de aceite:** o sistema exibe erros para nome, telefone e aceite do aviso de privacidade, e não exibe erro para o e-mail (opcional).
- **Pré-condições:** etapas 1 a 3 concluídas.
- **Automação:** `AgendamentoTest.ct07CamposObrigatorios`

| Ação | Resultado esperado |
|---|---|
| Na etapa 4, clicar em Confirmar agendamento com os campos vazios | São exibidas as mensagens "Escreva seu nome completo.", "Escreva o telefone com DDD..." e "Marque que você leu o aviso de privacidade."; o agendamento não é criado |

### Caso de Teste 08
- **ID:** 08
- **Descrição:** valida a rejeição de telefone sem DDD e de e-mail incompleto.
- **Nome:** Informar telefone e e-mail inválidos
- **Condição de aceite:** o sistema exibe erro no telefone e "O e-mail parece incompleto. Confira ou deixe em branco." e não cria o agendamento.
- **Pré-condições:** etapas 1 a 3 concluídas.
- **Automação:** `AgendamentoTest.ct08TelefoneEEmailInvalidos`

| Ação | Resultado esperado |
|---|---|
| Informar nome válido, telefone "5199", e-mail "maria@", marcar o aceite e confirmar | Erros exibidos para telefone e e-mail; nenhum erro para o nome; a tela permanece na etapa 4 |

### Caso de Teste 09
- **ID:** 09
- **Descrição:** valida a máscara automática do telefone.
- **Nome:** Formatar telefone ao sair do campo
- **Condição de aceite:** ao digitar `51988887777` e sair do campo, o campo exibe `(51) 98888-7777`.
- **Pré-condições:** etapas 1 a 3 concluídas.
- **Automação:** `AgendamentoTest.ct09MascaraTelefone`

| Ação | Resultado esperado |
|---|---|
| Digitar 51988887777 no campo Telefone ou WhatsApp | O valor digitado é exibido no campo |
| Sair do campo (tecla TAB) | O valor é formatado como (51) 98888-7777 |

---

## Cenário 2 – Meu agendamento (consulta e cancelamento)

**Objetivo:** validar a consulta, a localização e o cancelamento de agendamentos pela própria pessoa.
**Pré-requisitos:** os mesmos do Cenário 1. Os casos usam o agendamento de exemplo informado pelo próprio site na nota "Para testar: código ..., ou telefone ... no dia ...".

### Caso de Teste 10
- **ID:** 10
- **Descrição:** valida a consulta com um código que não existe.
- **Nome:** Consultar código inexistente
- **Condição de aceite:** o sistema exibe "Não encontramos este código. Confira as letras ou use "Perdi o código"."
- **Pré-condições:** nenhuma.
- **Automação:** `MeuAgendamentoTest.ct10CodigoInexistente`

| Ação | Resultado esperado |
|---|---|
| Acessar "Meu agendamento", informar NAF-ZZZZZZ e clicar em Consultar | A mensagem de código não encontrado é exibida |

### Caso de Teste 11
- **ID:** 11
- **Descrição:** valida a consulta de um agendamento existente pelo código.
- **Nome:** Consultar agendamento pelo código
- **Condição de aceite:** o sistema exibe o resumo do agendamento com situação "Confirmado" e os botões Remarcar e Cancelar.
- **Pré-condições:** código de exemplo válido.
- **Automação:** `MeuAgendamentoTest.ct11ConsultarPorCodigo`

| Ação | Resultado esperado |
|---|---|
| Informar o código de exemplo e clicar em Consultar | O resumo "Agendamento NAF-..." é exibido com situação "Confirmado" e as opções Remarcar e Cancelar agendamento |

### Caso de Teste 12
- **ID:** 12
- **Descrição:** valida a busca do agendamento por telefone e data, para quem perdeu o código.
- **Nome:** Localizar agendamento sem o código
- **Condição de aceite:** o sistema localiza o agendamento correspondente ao telefone e à data informados.
- **Pré-condições:** telefone e data de exemplo válidos.
- **Automação:** `MeuAgendamentoTest.ct12BuscarPorTelefoneEData`

| Ação | Resultado esperado |
|---|---|
| Em "Perdi o código", informar telefone e dia do atendimento e clicar em Procurar | O agendamento de exemplo é exibido |

### Caso de Teste 13
- **ID:** 13
- **Descrição:** valida o cancelamento de um agendamento pela própria pessoa.
- **Nome:** Cancelar agendamento
- **Condição de aceite:** o sistema exibe "Agendamento cancelado. O horário foi liberado para outra pessoa." e a situação muda para "Cancelado".
- **Pré-condições:** agendamento confirmado.
- **Automação:** `MeuAgendamentoTest.ct13CancelarAgendamento`

| Ação | Resultado esperado |
|---|---|
| Consultar o agendamento pelo código | O resumo do agendamento é exibido |
| Clicar em Cancelar agendamento | São exibidos os motivos e os botões "Sim, cancelar" e "Não, manter" |
| Escolher um motivo e clicar em "Sim, cancelar" | A mensagem de sucesso é exibida e a situação passa a "Cancelado" |

---

## Cenário 3 – Navegação e área da equipe

**Objetivo:** validar a navegação entre as páginas públicas, o tratamento de rotas inválidas e o acesso da equipe.
**Pré-requisitos:** os mesmos do Cenário 1.

### Caso de Teste 14
- **ID:** 14
- **Descrição:** valida que as páginas públicas abrem com o título correto (teste orientado a dados, 5 rotas).
- **Nome:** Abrir páginas públicas
- **Condição de aceite:** cada rota exibe o título "<Página> · NAF FADERGS".
- **Pré-condições:** nenhuma.
- **Automação:** `NavegacaoTest.ct14PaginasPublicas` (DataProvider `rotasPublicas`)

| Ação | Resultado esperado |
|---|---|
| Acessar #/, #/servicos, #/privacidade, #/meu-agendamento e #/equipe | Os títulos Início, Serviços, Aviso de privacidade, Meu agendamento e Área da equipe são exibidos, seguidos de "NAF FADERGS" |

### Caso de Teste 15
- **ID:** 15
- **Descrição:** valida o tratamento de endereço inexistente.
- **Nome:** Acessar rota inexistente
- **Condição de aceite:** o sistema exibe a página "Página não encontrada".
- **Pré-condições:** nenhuma.
- **Automação:** `NavegacaoTest.ct15RotaInexistente`

| Ação | Resultado esperado |
|---|---|
| Acessar #/rota-que-nao-existe | A página "Página não encontrada" é exibida com opção de voltar ao início |

### Caso de Teste 16
- **ID:** 16
- **Descrição:** valida os links principais da página inicial.
- **Nome:** Navegar pelos links da página inicial
- **Condição de aceite:** "Agendar atendimento" leva ao fluxo de agendamento e "Ver serviços e documentos" leva à página Serviços.
- **Pré-condições:** nenhuma.
- **Automação:** `NavegacaoTest.ct16LinksPaginaInicial`

| Ação | Resultado esperado |
|---|---|
| Clicar em "Agendar atendimento" | A etapa 1 do agendamento é exibida |
| Voltar ao início e clicar em "Ver serviços e documentos" | A página Serviços é exibida |

### Caso de Teste 17
- **ID:** 17
- **Descrição:** valida a entrada e a saída da coordenação na área da equipe.
- **Nome:** Acessar área da equipe como coordenação
- **Condição de aceite:** ao entrar, é exibida a "Agenda do dia"; ao sair, volta à tela de entrada da equipe.
- **Pré-condições:** nenhuma (no protótipo a entrada é por perfil, sem senha).
- **Automação:** `NavegacaoTest.ct17AcessoCoordenacao`

| Ação | Resultado esperado |
|---|---|
| Acessar "Área da equipe" e clicar em "Entrar como coordenação" | A página "Agenda do dia" é exibida |
| Clicar em Sair | A tela "Área da equipe" é exibida novamente |

---

## Fora do escopo desta versão

- Testes de desempenho, carga e segurança: o protótipo não tem servidor; os dados ficam no navegador.
- Testes em dispositivos móveis e de acessibilidade (sugeridos para a próxima etapa).
- Remarcação e funções internas da coordenação (registro de chegada, falta e encaminhamento).
