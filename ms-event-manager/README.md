# ms-event-manager

## Descrição
Este projeto é um microsserviço desenvolvido em **Spring Boot** para gerenciamento de  eventos . Ele inclui suporte para banco de dados MongoDB Compass.

## Tecnologias Utilizadas
- **Mensageria: RabbitMQ**
- **Spring Versão 3.3.x (LTS)**
- **Maven**
- **Banco de Dados MongoDB Compass**
- **Java Versão 17 (LTS)**
- **API ViaCEP**
- **OpenFeign**



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
Configurações do MongoDB
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

## Testes
Para rodar os testes, utilize o comando:
```bash
mvn test
```


# API Endpoints -Event

Este documento descreve as rotas disponíveis nos controladores de Evento  `ms-event-manager`. Ele inclui exemplos de uso para cada endpoint.

---

## Produtos (Products)

### 1. Criar Produto
**POST /api/v1/products**

**Requisição:**
```json
{
    "name": "Produto1",
    "description": "Descrição",
    "price": 9.99,
    "imgUrl": "http://exemplo.com/imagem.jpg",
    "date": "2023-12-27T10:30:00",
    "categories": [
        {
            "id": 1,
            "name": "Categoria A"
        }
    ]
}
```

**Resposta:**
```json
{
    "name": "Produto1",
    "description": "Descrição",
    "price": 9.99,
    "imgUrl": "http://exemplo.com/imagem.jpg",
    "date": "2023-12-27T10:30:00",
    "categories": [
        {
            "id": 1,
        }
    ]
}
```

---

### 2. Buscar Produto por ID
**GET /api/v1/products/{id}**

**Resposta:**
```json
{
   "id": 1,
    "name": "Produto1",
    "description": "Descrição",
    "price": 9.99,
    "imgUrl": "http://exemplo.com/imagem.jpg",
    "date": "2023-12-27T10:30:00",
    "categories": [
        {
            "id": 1,
        }
    ]
}
```

---

### 3. Deletar Produto
**DELETE /api/v1/products/{id}**

**Resposta:**
- 204 No Content (se removido com sucesso)
- 404 Not Found (se o produto não for encontrado)

---

### 4. Listar Produtos com Paginação
**GET /api/v1/products**

**Parâmetros:**
- `page` (opcional): Página atual (padrão: 0)
- `linesPerPage` (opcional): Linhas por página (padrão: 5)
- `direction` (opcional): Ordenação ASC/DESC (padrão: ASC)
- `orderBy` (opcional): Campo de ordenação (padrão: name)

**Resposta:**
```json
{
  "content": [
{
    "name": "Produto1",
    "description": "Descrição",
    "price": 9.99,
    "imgUrl": "http://exemplo.com/imagem.jpg",
    "date": "2023-12-27T10:30:00",
    "categories": [
        {
            "id": 1,
        }
    ]
}
{
    "name": "Produto2",
    "description": "Descrição",
    "price": 1.99,
    "imgUrl": "http://exemplo.com/imagem.jpg",
    "date": "2023-12-27T10:30:00",
    "categories": [
        {
            "id": 1,
        }
    ]
}
  ],
  "totalPages": 1,
  "totalElements": 2,
  "size": 5,
  "number": 0
}
```

---

## Categorias (Categories)

### 1. Criar Categoria
**POST /api/v1/categories**

**Requisição:**
```json
{
  "name": "Categoria A"
}
```

**Resposta:**
```json
   "categories": [
        {
            "id": 1,
        }
    ]
```

---

### 2. Atualizar Categoria
**PUT /api/v1/categories/{id}**

**Requisição:**
```json
{
  "name": "Nova Categoria"
}
```

**Resposta:**
- 204 No Content (se atualizado com sucesso)

---

### 3. Buscar Categoria por ID
**GET /api/v1/categories/{id}**

**Resposta:**
```json
{
  "id": 1,
}
```

---

### 4. Listar Todas as Categorias
**GET /api/v1/categories**

**Resposta:**
```json
[
  {
    "id": 1,
  },
  {
    "id": 2,
  }
]
```

---

### 5. Deletar Categoria
**DELETE /api/v1/categories/{id}**

**Resposta:**
- 204 No Content (se removido com sucesso)





