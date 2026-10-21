# language: pt

Funcionalidade: Validar o contrato do cadastro bem-sucedido de dispositivo
  Como usuário da API
  Quero validar o contrato da resposta de cadastro
  Para garantir que a resposta mantenha a estrutura esperada

  Contexto: Usuário autenticado e unidade consumidora existente
    Dado que estou autenticado
    E existe uma unidade consumidora cadastrada

  Cenário: Validar contrato do cadastro bem-sucedido de dispositivo
    Dado que eu tenha os seguintes dados do dispositivo:
      | campo                 | valor           |
      | nome                  | Ar condicionado |
      | potenciaNominal       | 1200.0          |
      | status                | LIGADO          |
      | consumoMinimoAtivo    | 0.5             |
      | tempoOciosidadeLimite | 30              |
    E que o arquivo de contrato esperado é o "cadastro-bem-sucedido-de-dispositivo.json"
    Quando eu enviar a requisição para o endpoint "/api/dispositivos" de cadastro de dispositivos
    Então o status code da resposta deve ser 201
    E a resposta da requisição deve estar em conformidade com o contrato selecionado
