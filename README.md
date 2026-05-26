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

O projeto está preparado para utilizar Oracle Database Free via Docker Compose. O serviço do banco está definido em `docker/docker-compose.yml` com as seguintes credenciais padrão:

| Configuração           | Valor       |
|------------------------|-------------|
| Host no Docker Compose | `oracle-db` |
| Host local             | `localhost` |
| Porta                  | `1521`      |
| Service name           | `freepdb1`  |
| Usuário                | `esg_user`  |
| Senha                  | `esg_pass`  |

Para subir apenas o banco de dados:

```bash
docker compose -f docker/docker-compose.yml up -d oracle-db
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

No Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=test"
```

No Linux/macOS:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=test
```

Por padrão, a aplicação ficará disponível em:

```text
http://localhost:8080
```

### 5. Executar com Docker Compose

O `Dockerfile` utiliza build multi-stage: a primeira etapa compila a aplicação com Maven e a segunda executa apenas o `.jar` gerado. Por isso, não é necessário gerar o pacote manualmente antes de subir os contêineres.

Antes de gerar a imagem Docker, recomenda-se executar a suíte de testes localmente. O `Dockerfile` empacota a aplicação com `-DskipTests` porque os testes de integração usam Testcontainers e Oracle Database Free, ficando mais adequados para execução fora do processo de build da imagem.

Antes de subir os contêineres, crie o arquivo `.env` a partir do exemplo versionado:

No Windows PowerShell:

```powershell
Copy-Item .env.example .env
```

No Linux/macOS:

```bash
cp .env.example .env
```

Abra o arquivo `.env` e ajuste a chave JWT, se desejar:

```env
API_SECURITY_TOKEN_SECRET=troque-por-uma-chave-com-8-caracteres
```

No Windows PowerShell:

```powershell
.\mvnw.cmd test
docker compose --env-file .env -f docker/docker-compose.yml up --build
```

No Linux/macOS:

```bash
./mvnw test
docker compose --env-file .env -f docker/docker-compose.yml up --build
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

Execute a suíte de testes com:

No Windows PowerShell:

```powershell
.\mvnw.cmd test
```

No Linux/macOS:

```bash
./mvnw test
```

Além dos testes de controller com mocks, o projeto possui testes de integração com **Testcontainers** e **Oracle Database Free**. Esses testes sobem automaticamente um banco Oracle temporário, aplicam as migrações Flyway reais e removem o container ao final da execução.

Os testes devem ser executados antes da build Docker. No `Dockerfile`, o empacotamento usa `-DskipTests` apenas para manter a construção da imagem mais simples e previsível; a validação da qualidade fica no comando `mvnw test`.

Para rodar os testes de integração:

- Deixe o **Docker Desktop** aberto;
- Não é necessário executar `docker compose up`;
- Execute `.\mvnw.cmd test` no Windows ou `./mvnw test` no Linux/macOS;
- A primeira execução pode demorar mais porque o Docker precisará baixar a imagem Oracle usada pelos testes.

## Migrações de Banco de Dados

As migrações são gerenciadas pelo **Flyway** e ficam em:

```text
src/main/resources/db/migration
```

Ao iniciar a aplicação com `spring.flyway.enabled=true`, o Flyway aplica automaticamente os scripts pendentes no banco configurado.

## Estrutura do Projeto

```text
src
|-- main
|   |-- java/br/com/fiap/esg
|   |   |-- controller      # Controllers REST
|   |   |-- domain          # Entidades, serviços, repositórios e DTOs
|   |   |-- infra           # Segurança, exceções e configuração OpenAPI
|   |   `-- mapper          # Mapeadores MapStruct
|   `-- resources
|       |-- application.properties
|       |-- application-test.properties
|       `-- db/migration    # Scripts Flyway
|-- test                    # Testes automatizados
|-- docker                  # Dockerfile e Docker Compose
`-- postman                 # Coleção Postman
```
