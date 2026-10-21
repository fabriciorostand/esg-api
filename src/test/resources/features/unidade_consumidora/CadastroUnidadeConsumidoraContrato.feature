# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de unidade consumidora
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

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

  Cenário: Validar contrato do cadastro bem-sucedido de unidade consumidora
    Dado que eu tenha os seguintes dados da unidade consumidora:
      | campo     | valor          |
      | nome      | Unidade Matriz |
      | tipo      | COMERCIAL      |
      | areaTotal | 250.75         |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-unidade-consumidora.json"
    Quando eu enviar a requisição para o endpoint "/api/unidadesconsumidoras" de cadastro de unidades consumidoras
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
