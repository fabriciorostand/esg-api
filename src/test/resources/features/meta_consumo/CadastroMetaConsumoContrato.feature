# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de meta de consumo
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Contexto: Usuário autenticado e dispositivo existente
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora

  Cenário: Validar contrato do cadastro bem-sucedido de meta de consumo
    Dado que eu tenha os seguintes dados da meta de consumo:
      | campo      | valor      |
      | tipo       | MES        |
      | metaKwh    | 100.50     |
      | dataInicio | 2026-10-01 |
      | dataFim    | 2026-10-31 |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-meta-consumo.json"
    Quando eu enviar a requisição para o endpoint "/api/metas" de cadastro de metas de consumo
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
