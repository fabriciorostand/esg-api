# language: pt

Funcionalidade: Cadastro de um novo consumo energético
  Como usuário da API
  Quero registrar o consumo energético medido por um sensor
  Para acompanhar o consumo do dispositivo ao longo do tempo

  Contexto: Usuário autenticado e sensor cadastrado
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora
    E existe um sensor ativo cadastrado nesse dispositivo

  Cenário: Cadastro bem-sucedido de consumo energético
    Dado que eu tenha os seguintes dados do consumo energético:
      | campo        | valor      |
      | kwhConsumido | 15.50      |
      | dataMedicao  | 2026-10-20 |
    Quando eu enviar a requisição para o endpoint "/api/consumosenergeticos" de cadastro de consumos energéticos
    Então o status code da resposta deve ser 201
    E deve conter o identificador do consumo energético cadastrado
    E o consumo energético deve estar vinculado ao sensor informado

  Cenário: Não cadastrar consumo energético sem informar o KWH consumido
    Dado que eu tenha os seguintes dados do consumo energético:
      | campo        | valor      |
      | kwhConsumido |            |
      | dataMedicao  | 2026-10-20 |
    Quando eu enviar a requisição para o endpoint "/api/consumosenergeticos" de cadastro de consumos energéticos
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "kwhConsumido" com a mensagem "O consumo em kWh e obrigatorio"
