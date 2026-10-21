# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de consumo energético
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Contexto: Usuário autenticado e sensor cadastrado
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora
    E existe um sensor ativo cadastrado nesse dispositivo

  Cenário: Validar contrato do cadastro bem-sucedido de consumo energético
    Dado que eu tenha os seguintes dados do consumo energético:
      | campo        | valor      |
      | kwhConsumido | 15.50      |
      | dataMedicao  | 2026-10-20 |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-consumo-energetico.json"
    Quando eu enviar a requisição para o endpoint "/api/consumosenergeticos" de cadastro de consumos energéticos
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
