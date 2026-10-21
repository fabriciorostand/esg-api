# language: pt

Funcionalidade: Cadastro de um novo dispositivo
  Como usuário da API
  Quero cadastrar um novo dispositivo
  Para que o registro seja salvo corretamente no sistema

  Contexto: Usuário autenticado e unidade consumidora existente
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada

  Cenário: Cadastro bem-sucedido do dispositivo em uma unidade existente
    Dado que eu tenha os seguintes dados do dispositivo:
      | campo                 | valor           |
      | nome                  | Ar condicionado |
      | potenciaNominal       | 1200.0          |
      | status                | LIGADO          |
      | consumoMinimoAtivo    | 0.5             |
      | tempoOciosidadeLimite | 30              |
    Quando eu enviar a requisição para o endpoint "/api/dispositivos" de cadastro de dispositivos
    Então o status code da resposta deve ser 201
    E deve conter o identificador do dispositivo cadastrado

  Cenário: Não cadastrar dispositivo sem informar o nome
    Dado que eu tenha os seguintes dados do dispositivo:
      | campo                 | valor  |
      | nome                  |        |
      | potenciaNominal       | 1200.0 |
      | status                | LIGADO |
      | consumoMinimoAtivo    | 0.5    |
      | tempoOciosidadeLimite | 30     |
    Quando eu enviar a requisição para o endpoint "/api/dispositivos" de cadastro de dispositivos
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "nome" com a mensagem "O nome do dispositivo é obrigatório"
