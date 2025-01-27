# ms-ticket-manager

## Descrição
Este projeto é um microsserviço desenvolvido em **Spring Boot** para gerenciamento de  tickets(Ingresso) . Ele inclui suporte para banco de dados MongoDB Compass.

## Tecnologias Utilizadas
- **Mensageria: RabbitMQ**
- **Spring Versão 3.3.x (LTS)**
- **Maven**
- **Banco de Dados MongoDB Compass**
- **Java Versão 17 (LTS)**
- **OpenFeign**



## Dependências
### Principais Dependências
- `spring-boot-starter-data-mongodb`: Suporte para integração com o MongoDB.
- `spring-boot-starter-mail`: Suporte para envio de e-mails.
- `spring-boot-starter-web`: Suporte para criação de aplicativos web e APIs RESTful.
- `spring-boot-starter-amqp`: Suporte para integração com sistemas de mensageria como RabbitMQ.
- `mapstruct`: Ferramenta para mapeamento entre objetos Java (DTOs e entidades).
- `spring-rabbit`: Integração com RabbitMQ para comunicação com filas de mensagens.
- `jackson-databind`: Serialização e desserialização de objetos Java para JSON.
- `lombok`: Geração automática de código repetitivo (getters, setters, etc.) com anotações.
- `javax.persistence-api`: API para uso de JPA (Java Persistence API) em bancos de dados relacionais.
- `hibernate-core`: Implementação de JPA para persistência de dados em bancos de dados.
- `spring-cloud-starter-openfeign`: Suporte para clientes HTTP declarativos usando OpenFeign.
- `spring-boot-starter-test`: Dependências para testes de unidade e integração em Spring Boot.
- `mockito-core`: Framework para criação de mocks em testes de unidade.
- `assertj-core`: Biblioteca para escrever assertivas fluídas em testes.
- `mockito-junit-jupiter`: Extensão do Mockito para integração com JUnit 5.
- `spring-test`: Ferramentas e utilitários para testes de integração em Spring.






## Configuração do Banco de Dados
### MongoDB 
```
spring.data.mongodb.uri=mongodb://localhost:27017/db_ticket
spring.data.mongodb.database=db_ticket

```

## Configurações da Porta 
```
server.port=8080

```
## Configurações do RabbitMQ
```
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

```
## Configurações do RabbitMQ
```
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

```
## Configurações do Gmail SMTP para envio de e-mails
```
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=(seu email)
spring.mail.password=(sua senha)
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.starttls.required=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.debug=true
spring.mail.properties.mail.smtp.ssl.trust=smtp.gmail.com

```

## Executando o Projeto
1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
2. Acesse o diretório do projeto:
   ```bash
   cd ms-ticket-manager
   ```
3. Execute o comando para compilar e rodar:
   ```bash
   mvn spring-boot:run
   ```
4. O projeto estará disponível em:
   ```
   http://localhost:8080
   ```
   ## Relatório de cobertura de Test
   (img)


## Testes
Para rodar os testes, faça o exemplo:
```
Para rodar os testes com cobertura de código

Clique com o botão direito no diretório ou arquivo de teste.
Selecione Run 'Test Name' with Coverage.
```


# API Endpoints -Ticket

Este documento descreve as rotas disponíveis nos controladores de Evento  `ms-ticket-manager`. Ele inclui exemplos de uso para cada endpoint.

---

## Ingresso (Ticket)

### 1. Criar Evento
**POST /api/create-event**

**Requisição:**
```json
{
  "eventName": "Show da Xux",
  "dateTime": "2024-12-30T21:00:00",
  "cep": "01020-000"
}
```

**Resposta:**
```json
{
    "id": "1",
    "eventName": "Show da Xux",
    "dateTime": "2024-12-30T21:00:00",
    "cep": "01020-000",
    "logradouro": "Rua Tabatinguera",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP"
}
```

---

### 2. Buscar Evento por ID
**GET /api/get-event/{id}**

**Resposta:**
```json
{
    "id": "1",
    "eventName": "Show da Xux",
    "dateTime": "2024-12-30T21:00:00",
    "cep": "01020-000",
    "logradouro": "Rua Tabatinguera",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP"
}
```

---

### 3. Buscar todos os Eventos
**GET /api/get-all-events**

**Resposta:**
```json
{
    "id": "1",
    "eventName": "Show do notion",
    "dateTime": "2024-12-30T21:00:00",
    "cep": "01020-000",
    "logradouro": "Rua Tabatinguera",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP"
}
```
### 4. Buscar todos os Eventos ordenados
**GET /api/get-all-events/sorted**

**Resposta:**
```json
{
}
```

### 5. Atualizar Evento
**PUT /api/update-event/{id}**

**Requisição:**
```json
{
  "id": "1",
  "eventName": "Show do Igor",
  "dateTime": "2024-12-30T21:00:00",
  "cep": "01020-000",
  "logradouro": "Rua Tabatinguera",
  "bairro": "Sé",
  "localidade": "São Paulo das",
  "uf": "SP"
}
```

**Resposta:**
```json
{
    "id": "1",
    "eventName": "Show do Igor",
    "dateTime": "2024-12-30T21:00:00",
    "cep": "01020-000",
    "logradouro": "Rua Tabatinguera",
    "bairro": "Sé",
    "localidade": "São Paulo",
    "uf": "SP"
}
```
### 6. Deletar Evento
**DELETE /api/delete-event/{id}**

**Resposta:**
- 204 No Content (Evento excluído com sucesso.)
