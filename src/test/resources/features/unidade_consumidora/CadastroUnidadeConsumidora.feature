# language: pt

Funcionalidade: Cadastro de uma nova unidade consumidora
  Como usuário da API
  Quero cadastrar uma nova unidade consumidora
  Para que o registro seja salvo corretamente no sistema

  Contexto: Usuário autenticado e endereço válido
    Dado que estou autenticado
    E que eu tenha os seguintes dados de endereço:
      | campo  | valor          |
      | bairro | Centro         |
      | rua    | Rua das Flores |
      | numero | 100            |
      | cep    | 01001000       |
      | cidade | Sao Paulo      |
      | uf     | SP             |

  Cenário: Cadastro bem-sucedido da unidade consumidora com endereço completo
    Dado que eu tenha os seguintes dados da unidade consumidora:
      | campo     | valor          |
      | nome      | Unidade Matriz |
      | tipo      | COMERCIAL      |
      | areaTotal | 250.75         |
    Quando eu enviar a requisição para o endpoint "/api/unidadesconsumidoras" de cadastro de unidades consumidoras
    Então o status code da resposta deve ser 201
    E deve conter o identificador da unidade consumidora
    E o endereço retornado deve corresponder aos dados informados

  Cenário: Não cadastrar unidade consumidora sem informar o tipo
    Dado que eu tenha os seguintes dados da unidade consumidora:
      | campo     | valor          |
      | nome      | Unidade Matriz |
      | tipo      |                |
      | areaTotal | 250.75         |
    Quando eu enviar a requisição para o endpoint "/api/unidadesconsumidoras" de cadastro de unidades consumidoras
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "tipo" com a mensagem "O tipo da unidade consumidora é obrigatório"

  Cenário: Não cadastrar unidade consumidora com tipo desconhecido
    Dado que eu tenha os seguintes dados da unidade consumidora:
      | campo     | valor          |
      | nome      | Unidade Matriz |
      | tipo      | ABC            |
      | areaTotal | 250.75         |
    Quando eu enviar a requisição para o endpoint "/api/unidadesconsumidoras" de cadastro de unidades consumidoras
    Então o status code da resposta deve ser 400
    E o corpo de resposta de erro da api deve retornar a mensagem "Corpo da requisição inválido ou malformado"
