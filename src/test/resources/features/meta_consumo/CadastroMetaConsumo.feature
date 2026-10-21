# language: pt

Funcionalidade: Cadastro de uma nova meta de consumo energético
  Como usuário da API
  Quero cadastrar uma nova meta de consumo energético para um dispositivo
  Para acompanhar seu consumo durante um período

  Contexto: Usuário autenticado e dispositivo existente
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada
    E existe um dispositivo cadastrado nessa unidade consumidora

  Cenário: Cadastro bem-sucedido da meta de consumo para um dispositivo existente
    Dado que eu tenha os seguintes dados da meta de consumo:
      | campo      | valor      |
      | tipo       | MES        |
      | metaKwh    | 100.50     |
      | dataInicio | 2026-10-01 |
      | dataFim    | 2026-10-31 |
    Quando eu enviar a requisição para o endpoint "/api/metas" de cadastro de metas de consumo
    Então o status code da resposta deve ser 201
    E deve conter o identificador da meta de consumo cadastrada
    E a meta de consumo deve estar vinculada ao dispositivo informado

  Cenário: Não cadastrar meta de consumo sem informar o tipo
    Dado que eu tenha os seguintes dados da meta de consumo:
      | campo      | valor      |
      | tipo       |            |
      | metaKwh    | 100.50     |
      | dataInicio | 2026-10-01 |
      | dataFim    | 2026-10-31 |
    Quando eu enviar a requisição para o endpoint "/api/metas" de cadastro de metas de consumo
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "tipo" com a mensagem "O tipo da meta é obrigatório"

  Cenário: Não cadastrar meta de consumo com tipo desconhecido
    Dado que eu tenha os seguintes dados da meta de consumo:
      | campo      | valor      |
      | tipo       | ABC        |
      | metaKwh    | 100.50     |
      | dataInicio | 2026-10-01 |
      | dataFim    | 2026-10-31 |
    Quando eu enviar a requisição para o endpoint "/api/metas" de cadastro de metas de consumo
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro da api deve retornar a mensagem "Corpo da requisição inválido ou malformado"
