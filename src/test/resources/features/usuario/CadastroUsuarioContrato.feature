# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de usuário
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Cenário: Validar contrato do cadastro bem-sucedido de usuário
    Dado que eu tenha os seguintes dados do usuário:
      | campo | valor           |
      | email | teste@email.com |
      | senha | Senha123        |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-usuario.json"
    Quando eu enviar a requisição para o endpoint "/api/autenticacao/registro" de cadastro de usuarios
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
