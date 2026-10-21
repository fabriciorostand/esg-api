# language: pt

Funcionalidade: Cadastro de um novo sensor
  Como usuário da API
  Quero cadastrar um sensor em um dispositivo
  Para monitorar seu consumo energético

  Contexto: Usuário autenticado e dispositivo disponível
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora

  Cenário: Cadastro bem-sucedido do sensor ativo em um dispositivo existente
    Dado que eu tenha os seguintes dados do sensor:
      | campo | valor |
      | ativo | S     |
    Quando eu enviar a requisição para o endpoint "/api/sensores" de cadastro de sensores
    Então o status code da resposta deve ser 201
    E deve conter o identificador do sensor cadastrado
    E o sensor deve estar vinculado ao dispositivo informado

  Cenário: Não cadastrar sensor sem informar se está ativo
    Dado que eu tenha os seguintes dados do sensor:
      | campo | valor |
      | ativo |       |
    Quando eu enviar a requisição para o endpoint "/api/sensores" de cadastro de sensores
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "ativo" com a mensagem "O status ativo é obrigatório"
