# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de alerta de meta
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Contexto: Dispositivo monitorado com meta cadastrada
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora
    E existe um sensor ativo cadastrado nesse dispositivo
    E existe uma meta de consumo cadastrada para esse dispositivo:
      | campo      | valor      |
      | tipo       | MES        |
      | metaKwh    | 100.00     |
      | dataInicio | 2026-10-01 |
      | dataFim    | 2026-10-31 |
    E que eu tenha os seguintes dados do alerta de meta:
      | campo           | valor      |
      | valorAlertaMeta | 100.00     |
      | dataAlertaMeta  | 2026-10-20 |

  Cenário: Validar contrato do cadastro bem-sucedido de alerta de meta
    Dado que existem os seguintes consumos registrados para esse sensor:
      | dataMedicao | kwhConsumido |
      | 2026-10-10  | 60.00        |
      | 2026-10-20  | 40.00        |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-alerta-meta.json"
    Quando eu enviar a requisição para o endpoint "/api/alertas" de cadastro de alertas de meta
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
