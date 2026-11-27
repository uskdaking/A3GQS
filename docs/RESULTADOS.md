# Resultados da execução – NAF Inteligente

**Sistema sob teste:** https://nafinteligente.netlify.app/
**Ambiente:** GitHub Actions (ubuntu-latest), Java 21 (Temurin), Maven, Selenium 4.49.0, TestNG 7.12.0, Google Chrome em modo headless
**Data:** 12/11/2026
**Execução de referência:** workflow #6 (disparo manual), commit `5d31607`, duração total de 1min27s

## Resumo

| Total | Aprovados | Falhas | Ignorados |
|---|---|---|---|
| 21 | 21 | 0 | 0 |

São 17 casos de teste. O CT14 roda 5 vezes, uma por rota (DataProvider), por isso o total é 21.

## Detalhe por caso

| Caso | Classe | Status | Tempo (ms) |
|---|---|---|---|
| CT01 – Efetuar agendamento com sucesso | AgendamentoTest | ✅ Aprovado | 8746 |
| CT02 – Impedir avanço sem escolher serviço | AgendamentoTest | ✅ Aprovado | 1179 |
| CT03 – Exigir assunto quando a pessoa não sabe o serviço | AgendamentoTest | ✅ Aprovado | 3408 |
| CT04 – Exigir ciência dos documentos necessários | AgendamentoTest | ✅ Aprovado | 2311 |
| CT05 – Exigir dia e horário antes de avançar | AgendamentoTest | ✅ Aprovado | 14676 |
| CT06 – Horários esgotados não podem ser escolhidos | AgendamentoTest | ✅ Aprovado | 530 |
| CT07 – Validar campos obrigatórios dos dados pessoais | AgendamentoTest | ✅ Aprovado | 1606 |
| CT08 – Rejeitar telefone sem DDD e e-mail incompleto | AgendamentoTest | ✅ Aprovado | 4005 |
| CT09 – Formatar telefone ao sair do campo | AgendamentoTest | ✅ Aprovado | 5672 |
| CT10 – Consultar com código inexistente | MeuAgendamentoTest | ✅ Aprovado | 200 |
| CT11 – Consultar agendamento pelo código | MeuAgendamentoTest | ✅ Aprovado | 200 |
| CT12 – Localizar agendamento por telefone e data | MeuAgendamentoTest | ✅ Aprovado | 204 |
| CT13 – Cancelar agendamento e liberar a vaga | MeuAgendamentoTest | ✅ Aprovado | 3432 |
| CT14 – Abrir páginas públicas [#/] | NavegacaoTest | ✅ Aprovado | 29 |
| CT14 – Abrir páginas públicas [#/servicos] | NavegacaoTest | ✅ Aprovado | 35 |
| CT14 – Abrir páginas públicas [#/privacidade] | NavegacaoTest | ✅ Aprovado | 33 |
| CT14 – Abrir páginas públicas [#/meu-agendamento] | NavegacaoTest | ✅ Aprovado | 42 |
| CT14 – Abrir páginas públicas [#/equipe] | NavegacaoTest | ✅ Aprovado | 42 |
| CT15 – Rota inexistente mostra página não encontrada | NavegacaoTest | ✅ Aprovado | 34 |
| CT16 – Links da página inicial | NavegacaoTest | ✅ Aprovado | 168 |
| CT17 – Entrar e sair da área da equipe como coordenação | NavegacaoTest | ✅ Aprovado | 215 |

## Histórico das execuções

| Execução | Resultado | O que aconteceu |
|---|---|---|
| #1 | 20 de 21 | O CT09 falhou: o teste esperava a máscara do telefone durante a digitação, mas o site aplica a máscara quando o campo perde o foco. O comportamento foi confirmado manualmente. O erro estava no teste, não no sistema, e o caso foi ajustado para sair do campo com TAB. |
| #2 | 20 de 21 | Commit intermediário: o Page Object já estava ajustado, mas o teste ainda não. |
| #3 | 20 de 21 | O CT08 falhou de forma intermitente: a etapa "Documentos" ainda estava sendo redesenhada quando o checkbox foi clicado, e o clique se perdeu. Era um problema de sincronização do teste. |
| #4 | 21 de 21 | Atualização das actions do workflow. |
| #5 | 21 de 21 | Correção da intermitência. Agora o teste espera o título de cada etapa e confirma que o radio ou checkbox ficou marcado (`BasePage.marcar`). |
| #6 | 21 de 21 | Reexecução manual para confirmar a estabilidade. |

## Observações para o sistema (não são falhas dos casos automatizados)

1. **Validação do nome:** a mensagem pede "nome completo", mas o campo aceita um único nome, como "Maria". Recomenda-se exigir nome e sobrenome ou trocar o texto da mensagem.
2. **Máscara do telefone:** a máscara só é aplicada ao sair do campo. Aplicar a formatação durante a digitação daria um retorno imediato a quem preenche.

## Observações sobre a automação

- O CT05 leva cerca de 15 s porque a espera implícita de 5 s age sobre mensagens que não existem na tela. Isso pode ser reduzido com esperas explícitas.
- Os dados do protótipo ficam no `localStorage`. Cada teste limpa o armazenamento antes de rodar e, assim, recomeça com os dados de exemplo, o que torna os casos independentes.
- Os relatórios completos do TestNG (`index.html`, `emailable-report.html` e `testng-results.xml`) ficam disponíveis como artefato `relatorios-testng` em cada execução do workflow.
