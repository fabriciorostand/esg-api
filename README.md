<p align="center">
  <img src="https://img.shields.io/badge/version-0.0.1--SNAPSHOT-blue" alt="Versão">
  <img src="https://img.shields.io/badge/build-not%20verified-yellow" alt="Build">
  <img src="https://img.shields.io/badge/license-A%20definir-lightgrey" alt="Licença">
  <img src="https://img.shields.io/badge/Java-21-orange" alt="Java 21">
  <img src="https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F" alt="Spring Boot">
</p>

# ESG API

API REST desenvolvida em Spring Boot para gerenciar eficiência energética e sustentabilidade em unidades consumidoras. O sistema permite registrar unidades, dispositivos, sensores, consumos energéticos, metas de consumo e alertas, com autenticação JWT e observabilidade via Spring Actuator.

## Tecnologias

- **Java 21**
- **Spring Boot 4.0.6**
- **Spring Web**
- **Spring Data JPA**
- **Spring Security**
- **Spring Validation**
- **Spring Actuator**
- **Springdoc OpenAPI**
- **Flyway**
- **Oracle Database / Oracle Free**
- **JWT com Auth0 Java JWT**
- **MapStruct**
- **Lombok**
- **Maven**
- **Docker e Docker Compose**
- **Postman**

## Pré-requisitos

Para executar a API localmente, instale:

- **JDK 21** ou superior
- **Maven** ou use o Maven Wrapper do projeto (`mvnw` / `mvnw.cmd`)
- **Docker** e **Docker Compose**
- **Git**
- Cliente HTTP opcional, como **Postman**, **Insomnia** ou **curl**

## Instalação e Configuração

### 1. Clonar o repositório

```bash
git clone https://github.com/fabriciorostand/esg-api.git
cd esg-api
```

### 2. Configurar o banco de dados

O projeto está preparado para utilizar Oracle Database Free via Docker Compose. O serviço do banco está definido em `compose.yml`, com as seguintes credenciais padrão:

| Configuração           | Valor       |
|------------------------|-------------|
| Host no Docker Compose | `oracle-db` |
| Host local             | `localhost` |
| Porta                  | `1521`      |
| Service name           | `freepdb1`  |
| Usuário                | `esg_user`  |
| Senha                  | `esg_pass`  |

Antes de executar os comandos do Compose, crie o arquivo `.env` a partir do exemplo versionado. A chave JWT precisa estar definida mesmo ao subir apenas o banco, pois o Compose resolve as variáveis de todos os serviços.

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

No Linux/macOS:

```bash
cp .env.example .env
```

Abra o arquivo `.env` e ajuste a chave JWT, respeitando o mínimo de 8 caracteres:

```env
API_SECURITY_TOKEN_SECRET=troque-por-uma-chave-com-8-caracteres
```

Para subir apenas o banco de dados, execute na raiz do repositório:

```bash
docker compose --env-file .env -f compose.yml up -d oracle-db
```

### 3. Configurar o `application.properties`

O arquivo principal está em:

```text
src/main/resources/application.properties
```

Configuração padrão para execução dentro da rede Docker:

```properties
spring.datasource.url=jdbc:oracle:thin:@oracle-db:1521/freepdb1
spring.datasource.username=esg_user
spring.datasource.password=esg_pass
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

api.security.token.secret=${API_SECURITY_TOKEN_SECRET}
```

Para executar a aplicação diretamente na máquina, use o profile `test`, que aponta para `localhost`. A chave JWT continua obrigatória e deve ser informada por variável de ambiente:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/freepdb1
spring.datasource.username=esg_user
spring.datasource.password=esg_pass
api.security.token.secret=${API_SECURITY_TOKEN_SECRET}
```

### 4. Executar a API com Maven

O Maven não carrega automaticamente o arquivo `.env` usado pelo Compose. Antes de iniciar a aplicação, defina a variável abaixo com o mesmo valor configurado nesse arquivo.

No Windows PowerShell:

```powershell
$env:API_SECURITY_TOKEN_SECRET = 'sua-chave-com-8-ou-mais-caracteres'
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=test"
```

No Linux/macOS:

```bash
export API_SECURITY_TOKEN_SECRET='sua-chave-com-8-ou-mais-caracteres'
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

Por padrão, a aplicação ficará disponível em:

```text
http://localhost:8080
```

### 5. Executar com Docker Compose

O `Dockerfile` utiliza build multi-stage: a primeira etapa compila a aplicação com Maven e a segunda executa apenas o `.jar` gerado. Por isso, não é necessário gerar o pacote manualmente antes de subir os contêineres.

Antes de gerar a imagem Docker, recomenda-se executar a suíte de testes localmente. O `Dockerfile` empacota a aplicação com `-DskipTests` porque os testes de integração usam Testcontainers e Oracle Database Free, ficando mais adequados para execução fora do processo de build da imagem.

Com o arquivo `.env` configurado, execute os comandos abaixo na raiz do repositório.

No Windows PowerShell:

```powershell
.\mvnw.cmd test
docker compose --env-file .env -f compose.yml up --build
```

No Linux/macOS:

```bash
./mvnw test
docker compose --env-file .env -f compose.yml up --build
```

## Variáveis de Ambiente

| Variável                     | Descrição                           | Valor padrão local                        |
|------------------------------|-------------------------------------|-------------------------------------------|
| `SPRING_DATASOURCE_URL`      | URL JDBC do Oracle                  | `jdbc:oracle:thin:@localhost:1521/freepdb1` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco                    | `esg_user`                                |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco                      | `esg_pass`                                |
| `API_SECURITY_TOKEN_SECRET`  | Chave usada para assinar tokens JWT, com no mínimo 8 caracteres | Obrigatória em todos os perfis; no Docker Compose pode ser definida pelo arquivo `.env` |

## Documentação da API com Springdoc OpenAPI

Os endpoints, contratos de requisição, respostas e códigos HTTP são documentados automaticamente com **Springdoc OpenAPI**.

Com a aplicação em execução, acesse:

- Swagger UI: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
- OpenAPI JSON: [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

A documentação interativa permite visualizar os recursos disponíveis e testar chamadas diretamente pelo navegador. Rotas protegidas exigem autenticação via JWT.

## Monitoramento com Spring Actuator

A API possui endpoints de observabilidade expostos pelo **Spring Actuator** para acompanhar saúde, informações da aplicação e métricas.

Link base local:

- Actuator: [http://localhost:8080/actuator](http://localhost:8080/actuator)

Endpoints úteis:

| Endpoint                   | Descrição                       |
|----------------------------|---------------------------------|
| `/actuator/health`         | Verifica a saúde da aplicação   |
| `/actuator/info`           | Exibe informações da aplicação  |
| `/actuator/metrics`        | Lista métricas disponíveis      |
| `/actuator/metrics/{nome}` | Consulta uma métrica específica |

Exemplo:

```text
http://localhost:8080/actuator/health
```

## Coleção do Postman

O projeto inclui uma coleção do Postman para facilitar testes manuais das rotas.

[![Run in Postman](https://run.pstmn.io/button.svg)](postman/postman_collection.json)

Arquivo local:

```text
postman/postman_collection.json
```

Para usar:

1. Abra o Postman.
2. Clique em **Import**.
3. Selecione o arquivo `postman/postman_collection.json`.
4. Ajuste a variável `baseUrl` para `http://localhost:8080`, se necessário.
5. Execute `Registrar` e `Login` para gerar o token usado nas rotas protegidas.

## Testes

### Preparar o ambiente

Execute os comandos na raiz do repositório, onde está o `pom.xml`.

1. Instale o **JDK 21** e configure `JAVA_HOME` para apontar para essa instalação.
2. Inicie o **Docker Desktop** com contêineres Linux no Windows/macOS, ou o serviço Docker no Linux.
3. Confirme que o Maven Wrapper usa o Java esperado e que o Docker está acessível:

No Windows PowerShell:

```powershell
.\mvnw.cmd -version
docker info
```

No Linux/macOS:

```bash
chmod +x mvnw
./mvnw -version
docker info
```

O Wrapper baixa a versão do Maven configurada no projeto. Na primeira execução, também é necessário acesso à internet para baixar as dependências e a imagem `gvenzl/oracle-free:23-slim-faststart`.

**Não é necessário subir a API ou o banco pelo Compose, criar um `.env` ou definir a chave JWT para executar os testes.** As configurações de integração e BDD fornecem uma chave exclusiva de teste e substituem a conexão do banco pelos dados do Oracle temporário criado pelo Testcontainers. As migrações Flyway são aplicadas automaticamente, e os contêineres temporários são removidos ao encerrar a execução.

### Tipos de testes

| Grupo | O que valida | Precisa de Docker? |
|-------|--------------|--------------------|
| Controllers (`controller/*Test`) | Rotas, validações e respostas com MockMvc e serviços simulados | Não |
| Validador e schemas (`bdd/contract/*Test`) | Aceitação e rejeição de exemplos JSON pelos contratos | Não |
| Integração (`integration/EsgApiIntegrationTest`) | Aplicação, persistência e migrações reais, com requisições via MockMvc | Sim |
| BDD (`bdd/runner/TestRunner`) | Cenários Gherkin de cadastro e de contrato, usando REST Assured com HTTP real e Oracle temporário | Sim |

No BDD, o Spring Boot inicia a API em uma porta aleatória. Os steps preparam os cadastros necessários e fazem a autenticação pelas rotas da aplicação. O `Hook` limpa as tabelas do banco de teste antes de cada cenário, permitindo repetir os testes sem conflitos com dados anteriores.

### Executar a suíte completa

O comando abaixo executa todos os grupos, incluindo o `TestRunner` do Cucumber:

No Windows PowerShell:

```powershell
.\mvnw.cmd clean test
```

No Linux/macOS:

```bash
./mvnw clean test
```

Mantenha o Docker disponível até o término. O download e a inicialização do Oracle podem tornar a primeira execução mais demorada.

### Executar grupos específicos

No Windows PowerShell:

```powershell
# Controllers, sem Docker
.\mvnw.cmd test "-Dtest=*ControllerTest"

# Validador e schemas, sem Docker
.\mvnw.cmd test "-Dtest=ContratoStepsTest,ContratosCadastroTest"

# Integração com Oracle temporário
.\mvnw.cmd test "-Dtest=EsgApiIntegrationTest"

# Todos os cenários BDD, incluindo contratos via HTTP
.\mvnw.cmd test "-Dtest=br.com.fiap.esg.bdd.runner.TestRunner"
```

No Linux/macOS:

```bash
# Controllers, sem Docker
./mvnw test '-Dtest=*ControllerTest'

# Validador e schemas, sem Docker
./mvnw test '-Dtest=ContratoStepsTest,ContratosCadastroTest'

# Integração com Oracle temporário
./mvnw test '-Dtest=EsgApiIntegrationTest'

# Todos os cenários BDD, incluindo contratos via HTTP
./mvnw test '-Dtest=br.com.fiap.esg.bdd.runner.TestRunner'
```

Para executar apenas uma classe de teste, substitua o valor de `-Dtest` pelo nome dela, como `DispositivoControllerTest`.

### Features, contratos e relatórios

Os arquivos usados pelo BDD ficam em:

| Caminho | Conteúdo |
|---------|----------|
| `src/test/resources/features/` | Features Gherkin organizadas por domínio, incluindo os arquivos `*Contrato.feature` |
| `src/test/resources/schemas/` | Schemas JSON das respostas de cadastro bem-sucedido |
| `src/test/java/br/com/fiap/esg/bdd/steps/` | Implementação dos passos e das validações HTTP e de contrato |
| `src/test/java/br/com/fiap/esg/bdd/infra/` | Integração do Cucumber com Spring Boot e configuração do Oracle temporário |
| `src/test/java/br/com/fiap/esg/bdd/hook/` | Preparação e limpeza dos cenários |
| `src/test/java/br/com/fiap/esg/bdd/runner/` | Runner do Cucumber na JUnit Platform |

Após a execução, consulte:

- **Resultado no terminal:** o Maven apresenta o resumo dos testes e indica `BUILD SUCCESS` quando a execução passa.
- **Relatório BDD:** abra `target/cucumber-reports.html` no navegador após executar o `TestRunner`, isoladamente ou na suíte completa.
- **Relatórios Maven Surefire:** os arquivos `.txt` e `.xml` em `target/surefire-reports/` detalham os resultados e as falhas dos testes.

Os relatórios ficam em `target/` e são apagados pelo comando `clean` antes de uma nova execução.

Execute a suíte completa antes de gerar a imagem Docker. O `Dockerfile` usa `-DskipTests` no empacotamento; a construção da imagem não substitui a execução dos testes.

## Migrações de Banco de Dados

As migrações são gerenciadas pelo **Flyway** e ficam em:

```text
src/main/resources/db/migration
```

Ao iniciar a aplicação com `spring.flyway.enabled=true`, o Flyway aplica automaticamente os scripts pendentes no banco configurado.

## Estrutura do Projeto

```text
|-- docker
|   `-- Dockerfile
|-- postman                 # Coleção Postman
|-- src
|   |-- main
|   |   |-- java/br/com/fiap/esg
|   |   |   |-- controller  # Controllers REST
|   |   |   |-- domain      # Entidades, serviços, repositórios e DTOs
|   |   |   |-- infra       # Segurança, exceções e configuração OpenAPI
|   |   |   `-- mapper      # Mapeadores MapStruct
|   |   `-- resources
|   |       |-- application.properties
|   |       |-- application-test.properties
|   |       `-- db/migration # Scripts Flyway
|   `-- test                # Testes automatizados
|-- compose.yml             # Docker Compose
```
