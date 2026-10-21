# language: pt

Funcionalidade: Cadastro de alerta de meta de consumo
  Como usuário da API
  Quero registrar um alerta quando o consumo atingir a meta
  Para identificar dispositivos que atingiram o limite definido

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

  Cenário: Cadastrar alerta quando o consumo atingir a meta
    Dado que existem os seguintes consumos registrados para esse sensor:
      | dataMedicao | kwhConsumido |
      | 2026-10-10  | 60.00        |
      | 2026-10-20  | 40.00        |
    Quando eu enviar a requisição para o endpoint "/api/alertas" de cadastro de alertas de meta
    Então o status code da resposta deve ser 201
    E deve conter o identificador do alerta de meta cadastrado
    E o alerta deve estar vinculado à meta de consumo informada

  Cenário: Não cadastrar alerta de meta sem informar o valor
    Dado que eu tenha os seguintes dados do alerta de meta:
      | campo           | valor      |
      | valorAlertaMeta |            |
      | dataAlertaMeta  | 2026-10-20 |
    Quando eu enviar a requisição para o endpoint "/api/alertas" de cadastro de alertas de meta
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "valorAlertaMeta" com a mensagem "O valor do alerta é obrigatório"
