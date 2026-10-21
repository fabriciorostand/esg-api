# language: pt

Funcionalidade: Cadastro de um novo usuário
  Como usuário da API
  Quero cadastrar um novo usuário
  Para que o registro seja salvo corretamente no sistema

  Cenário: Cadastro bem-sucedido de usuário
    Dado que eu tenha os seguintes dados do usuário:
      | campo | valor           |
      | email | teste@email.com |
      | senha | Senha123        |
    Quando eu enviar a requisição para o endpoint "/api/autenticacao/registro" de cadastro de usuarios
    Então o status code da resposta deve ser 201
    E deve conter o identificador do usuário cadastrado

  Cenário: Não cadastrar usuário sem informar o email
    Dado que eu tenha os seguintes dados do usuário:
      | campo | valor    |
      | email |          |
      | senha | Senha123 |
    Quando eu enviar a requisição para o endpoint "/api/autenticacao/registro" de cadastro de usuarios
    Então o status code da resposta deve ser 400
    E a resposta deve conter um erro no campo "email" com a mensagem "Email é obrigatório"
