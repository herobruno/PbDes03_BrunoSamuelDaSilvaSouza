# ✨ Sistema de Gerenciamento de Eventos e Ingressos ✨

Bem-vindo ao **Sistema de Gerenciamento de Eventos e Ingressos**! Este projeto foi desenvolvido como parte do **Desafio III - Scholarship Program** e utiliza uma arquitetura de microsserviços para gerenciar eventos e ingressos de forma eficiente.

---

## **🌐 Visão Geral do Projeto**

Este sistema permite:
- Gerenciar eventos (criar, atualizar, listar e excluir).
- Gerenciar ingressos (criar, consultar, atualizar e cancelar).
- Validar CEP com a API ViaCEP.
- Enviar e-mails de confirmação utilizando RabbitMQ.

**Principais Tecnologias Utilizadas:**
- **Mensageria:** RabbitMQ
- **Banco de Dados:** MongoDB (Compass ou Atlas)
- **Java:** Versão 17 (LTS)
- **Spring Boot:** Versão 3.3.x (LTS)
- **Deploy:** AWS EC2

---

## **💡 Microsserviços**

### **1. `ms-event-manager`**  
Microsserviço responsável por gerenciar os eventos.

#### **Operações Disponíveis:**
| Operação      | Método | Path                   | Regra                                  |
|------------------|--------|------------------------|----------------------------------------|
| Criar           | POST   | `api/create-event`        | Cria um evento                        |
| Consultar       | GET    | `api/get-event/{id}`      | Busca um evento pelo ID               |
| Consultar       | GET    | `api/get-all-events`      | Lista todos os eventos                |
| Consultar       | GET    | `api/get-all-events/sorted` | Lista eventos em ordem alfabética     |
| Atualizar       | PUT    | `api/update-event/{id}`   | Atualiza um evento pelo ID            |
| Excluir         | DELETE | `api/delete-event/{id}`   | Exclui um evento pelo ID              |

#### **Regra para Exclusão de Eventos:**
Antes de deletar um evento, o microsserviço verifica no **ms-ticket-manager** se há ingressos vendidos para o evento. Caso existam ingressos:
- A operação é bloqueada.
- Um erro (HTTP 409 - Conflict) é retornado:

```json
{
  "error": "O evento não pode ser deletado porque possui ingressos vendidos."
}
```

---

### **2. `ms-ticket-manager`**  
Microsserviço responsável por gerenciar os ingressos dos eventos.

#### **Operações Disponíveis:**
| Operação      | Método | Path                                | Regra                                   |
|------------------|--------|-------------------------------------|----------------------------------------|
| Criar           | POST   | `api/create-ticket`                    | Cria um ingresso após validar o evento |
| Consultar       | GET    | `api/get-ticket/{id}`                  | Busca um ingresso pelo ID              |
| Consultar       | GET    | `api/get-ticket-by-cpf/{cpf}`          | Consulta ingressos por CPF             |
| Atualizar       | PUT    | `api/update-ticket/{id}`               | Atualiza o ingresso                    |
| Verificar       | GET    | `api/check-tickets-by-event/{eventId}` | Verifica ingressos vinculados a um evento |
| Cancelar        | DELETE | `api/cancel-ticket/{id}`               | Cancela um ingresso por ID (soft-delete) |
| Cancelar        | DELETE | `api/cancel-ticket/cpf/{cpf}`          | Cancela ingressos por CPF (soft-delete) |

---

## **📊 Configuração do Ambiente**

### **Requisitos:**
- Java 17 instalado.
- MongoDB Compass ou Atlas configurado.
- RabbitMQ configurado (local ou cloud).
- Base de dados: `db_event` e `db_ticket`.



### **Deploy AWS Disponivel em:**
- http://35.153.169.191:8080
- http://35.153.169.191:8081

 ### **OBS:** 
- Se possuir duvidas cada Api possui seu README.

---

## **⚙️ Funcionalidades Extras**

1. **Validação de CEP:**  
Integração com a API ViaCEP para buscar informações de endereço com base no CEP.

2. **Mensageria:**  
Envio de e-mails de confirmação utilizando RabbitMQ.

3. **Soft-Delete:**  
Cancelamento de ingressos utilizando soft-delete.

---

## **🔧 Boas Práticas Adotadas**

### **Controle de Versão:**
- Branches de desenvolvimento e main/master.
- Commits semânticos seguindo o padrão:
  - `feat`: Adição de novas funcionalidades.
  - `fix`: Correção de bugs.
  - `docs`: Atualizações de documentação.
  - `test`: Adição ou atualização de testes.
  - `refactor`: Melhorias no código sem alterar funcionalidades.
  - `chore`: Atualizações de ferramentas ou configurações.
  - `build`: Mudanças no sistema de build ou dependências.

### **Testes Unitários:**
- Cobertura mínima de 80%.
- Para verificar a cobertura:
- Clique com o botão direito na classe/pacote e selecione **Run with coverage**.


---

## **📊 Contato**

Caso tenha dúvidas ou sugestões, fique à vontade para entrar em contato:
- **E-mail:** [brunoss2118@gmail.com]
---


