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
- **Oracle Database / Oracle XE**
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

O projeto está preparado para utilizar Oracle XE via Docker Compose. O serviço do banco está definido em `docker/docker-compose.yml` com as seguintes credenciais padrão:

| Configuração           | Valor       |
|------------------------|-------------|
| Host no Docker Compose | `oracle-db` |
| Host local             | `localhost` |
| Porta                  | `1521`      |
| Service name           | `xepdb1`    |
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
spring.datasource.url=jdbc:oracle:thin:@oracle-db:1521/xepdb1
spring.datasource.username=esg_user
spring.datasource.password=esg_pass
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=validate
spring.flyway.enabled=true

api.security.token.secret=12345678
```

Para executar a aplicação diretamente na máquina, use o profile `test`, que aponta para `localhost`:

```properties
spring.datasource.url=jdbc:oracle:thin:@localhost:1521/xepdb1
spring.datasource.username=esg_user
spring.datasource.password=esg_pass
```

Também é possível sobrescrever as configurações por variáveis de ambiente:

```bash
SPRING_DATASOURCE_URL=jdbc:oracle:thin:@localhost:1521/xepdb1
SPRING_DATASOURCE_USERNAME=esg_user
SPRING_DATASOURCE_PASSWORD=esg_pass
API_SECURITY_TOKEN_SECRET=sua-chave-secreta
```

> Em ambientes reais, não mantenha segredos no repositório. Use variáveis de ambiente, secret manager ou configuração externa.

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

Como o `Dockerfile` copia o artefato gerado em `target/*.jar`, primeiro gere o pacote da aplicação:

No Windows PowerShell:

```powershell
.\mvnw.cmd clean package
docker compose -f docker/docker-compose.yml up --build
```

No Linux/macOS:

```bash
./mvnw clean package
docker compose -f docker/docker-compose.yml up --build
```

## Variáveis de Ambiente

| Variável                     | Descrição                           | Valor padrão local                        |
|------------------------------|-------------------------------------|-------------------------------------------|
| `SPRING_DATASOURCE_URL`      | URL JDBC do Oracle                  | `jdbc:oracle:thin:@localhost:1521/xepdb1` |
| `SPRING_DATASOURCE_USERNAME` | Usuário do banco                    | `esg_user`                                |
| `SPRING_DATASOURCE_PASSWORD` | Senha do banco                      | `esg_pass`                                |
| `API_SECURITY_TOKEN_SECRET`  | Chave usada para assinar tokens JWT | `12345678`                                |

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