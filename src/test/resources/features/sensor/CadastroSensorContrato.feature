# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de sensor
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Contexto: Usuário autenticado e dispositivo disponível
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora

  Cenário: Validar contrato do cadastro bem-sucedido de sensor
    Dado que eu tenha os seguintes dados do sensor:
      | campo | valor |
      | ativo | S     |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-sensor.json"
    Quando eu enviar a requisição para o endpoint "/api/sensores" de cadastro de sensores
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
