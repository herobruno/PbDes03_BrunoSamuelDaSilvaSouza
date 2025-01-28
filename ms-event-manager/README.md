# ms-event-manager

## Descrição
Este projeto é um microsserviço desenvolvido em **Spring Boot** para gerenciamento de  eventos , ele inclui suporte para banco de dados MongoDB Compass.

## Tecnologias Utilizadas
- **Mensageria RabbitMQ**
- **Spring Versão 3.3.x (LTS)**
- **Maven**
- **Banco de Dados MongoDB**
- **Java Versão 17 (LTS)**
- **API ViaCEP**




## Dependências
### Principais Dependências
- `spring-boot-starter-data-mongodb`: Suporte para integração com o MongoDB.
- `spring-boot-starter-validation`: Suporte para validação de dados com JSR-303/JSR-380.
- `spring-boot-starter-data-jpa`: Suporte para JPA (Java Persistence API) e bancos de dados relacionais.
- `spring-boot-starter-web`: Suporte para desenvolvimento de aplicações web com Spring MVC e Tomcat.
- `spring-boot-starter-test`: Suporte para testes com JUnit, Mockito e Spring Test.
- `spring-boot-devtools`: Ferramentas para desenvolvimento, como reinicialização automática e depuração.
- `spring-cloud-starter-openfeign`: Suporte para comunicação com APIs REST via Feign.
- `spring-cloud-dependencies`: Gerenciamento de versões para dependências do Spring Cloud.
- `lombok`: Geração automática de código como getters, setters e toString.
- `mockito-core`: Biblioteca para criação de mocks em testes unitários.
- `assertj-core`: Biblioteca para asserções fluentes e legíveis em testes.





## Configuração do Banco de Dados
### MongoDB 
```
spring.application.name=ms-event-manager
spring.data.mongodb.uri=mongodb://localhost:27017/db_event
spring.data.mongodb.database=db_event

```

## Configurações da Porta 
```
server.port=8081

```

## Executando o Projeto
1. Clone o repositório:
   ```bash
   git clone <URL_DO_REPOSITORIO>
   ```
2. Acesse o diretório do projeto:
   ```bash
   cd ms-event-manager
   ```
3. Execute o comando para compilar e rodar:
   ```bash
   mvn spring-boot:run
   ```
4. O projeto estará disponível em:
   ```
   http://localhost:8081
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


# API Endpoints -Event

Este documento descreve as rotas disponíveis nos controladores de Evento  `ms-event-manager`. Ele inclui exemplos de uso para cada endpoint.

---

## Evento (Event)

### 1. Criar Evento
**POST /api/create-event**

**Requisição:**
```json
{
  "eventName": "Show da Xux",
  "dateTime": "2024-12-30T21:00:00",
  "cep": "01020-000" Sera buscado os dados pela API ViaCEP
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

**Requisição:**

id: `"1"`

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

**Requisição:**

id: `"1"`

**Resposta:**
> 204 No Content
> 
> Evento excluído com sucesso





