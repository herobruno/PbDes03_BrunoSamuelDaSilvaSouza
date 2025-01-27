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
   ## Porcentagem de cobertura de Test
   ![image](https://github.com/user-attachments/assets/91e3d5f7-7062-43f8-99e6-e89cd53fb429)




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

### 1. Criar Ticket
**POST /api/create-ticket**

**Requisição:**
```json
{
 "customerName": "carlos",
  "cpf": "04957391043",
  "customerMail": "carlos@gmail.com",
  "eventId": "1", Buscado do ms-event-manager
  "eventName": "Show da fe",
  "BRLamount": "R$ 50,00",
  "USDamount": "$ 10,00"
}
```

**Resposta:**
```json
{
    "ticketId": "18",
    "customerName": "carlos",
    "cpf": "04957391043",
    "customerMail": "carlos@gmail.com",
    "eventId": "1",
    "eventName": "Show da fe",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "status": "concluído",
    "dateTime": "2024-12-30T21:00:00",
    "BRLtotalAmount": "R$ 50,00",
    "USDtotalAmount": "$ 10,00"
}
```

---

### 2. Buscar ticket por ID
**GET /api/get-ticket/{id}**

**Resposta:**
```json
{
    "ticketId": "1",
    "customerName": "carlos",
    "cpf": "04957391043",
    "customerMail": "carlos@gmail.com",
    "eventId": "6791e16ff033044b038159dc",
    "eventName": "Show da fe",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "status": "concluído",
    "dateTime": "2024-12-30T21:00:00",
    "BRLtotalAmount": "R$ 50,00",
    "USDtotalAmount": "$ 10,00"
}
```

---

### 3. Buscar todos os Ticket por cpf
**GET /api/get-ticket-by-cpf/**

**Requisição:**

cpf: "04957391043"

**Resposta:**
```json
{
    "ticketId": "17",
    "customerName": "string",
    "cpf": "04957391043",
    "customerMail": "carlos@gmail.com",
    "eventId": "6791e16ff033044b038159dc",
    "eventName": "Show da fe",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "status": "cancelado",
    "dateTime": "2024-12-30T21:00:00",
    "BRLtotalAmount": "R$ 50,00",
    "USDtotalAmount": "$ 10,00"
}
```

### 4. Atualizar Ticket
**PUT /api/update-ticket/{id}**

**Requisição:**
```json
{
    "ticketId": "1",
    "customerName": "carlos",
    "cpf": "04957391043",
    "customerMail": "carlos@gmail.com",
    "eventId": "6791e16ff033044b038159dc",
    "eventName": "Show da fe",
    "logradouro": "Avenida Paulista",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "status": "concluído",
    "dateTime": "2024-12-30T21:00:00",
    "BRLtotalAmount": "R$ 50,00",
    "USDtotalAmount": "$ 10,00"
}
```

**Resposta:**
```json
{
    "ticketId": "1",
    "customerName": "henrique",
    "cpf": "04957391043",
    "customerMail": "henrique@gmail.com",
    "eventId": "6791e16ff033044b038159dc",
    "eventName": "Show da milhao",
    "logradouro": "Avenida ",
    "bairro": "Bela Vista",
    "localidade": "São Paulo",
    "uf": "SP",
    "status": "concluído",
    "dateTime": "2024-12-30T21:00:00",
    "BRLtotalAmount": "R$ 50,00",
    "USDtotalAmount": "$ 10,00"
}
```
### 5. Verificar Tickets Vinculados a um Evento  
**`GET /api/check-tickets-by-event/{eventId}`**

**Requisição:**  
eventId: `"1"`

**Resposta:**  
`True` ou `False`  
> Essa rota é utilizada pelo **ms-event-manager** para verificar se há ingressos vendidos no **ms-ticket-manager** antes de excluir o evento.  

---



### 6. Cancelar Ticket por id
**DELETE /api/cancel-ticket/{id}**

**Requisição:**

ticketId: `"1"`

**Resposta:**

`Ingresso cancelado com sucesso.`

`"status": "cancelado"`
>Utilizamos a técnica de soft delete, onde o status de um pedido é alterado de "concluído" para "pendente" em vez de excluí-lo fisicamente do banco de dados. 

---
### 7. Cancelar Ticket por cpf
**DELETE /api/cancel-ticket/cpf/{cpf}**

**Requisição:**

cpf: `"04957391043"`

**Resposta:**

`Todos os ingressos associados ao CPF 04957391043 foram cancelados com sucesso.`

`"status": "cancelado"`
>Utilizamos a técnica de soft delete, onde o status de um pedido é alterado de "concluído" para "pendente" em vez de excluí-lo fisicamente do banco de dados. 
---
