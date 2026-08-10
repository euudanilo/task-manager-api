# Task Manager API

REST API para gerenciamento de tarefas construída com **Java** e **Spring Boot**, seguindo arquitetura em camadas (controller → service → repository → model). Projeto pessoal desenvolvido durante o 1º período de Engenharia de Software, aplicando práticas usadas em ambientes profissionais de backend.

🔗 **API em produção:** [https://task-manager-api-2y19.onrender.com](https://task-manager-api-2y19.onrender.com)
📄 **Documentação interativa (Swagger):** [https://task-manager-api-2y19.onrender.com/swagger-ui.html](https://task-manager-api-2y19.onrender.com/swagger-ui.html)

> ⚠️ A API está hospedada no plano gratuito do Render, que "dorme" após um período de inatividade. A primeira requisição pode levar de 30 a 60 segundos para responder.

---

## 🔧 Funcionalidades

- CRUD completo de tarefas (criar, listar, buscar, atualizar, deletar)
- Cadastro e login de usuários com autenticação **JWT**
- Senhas criptografadas com **BCrypt**
- Autorização por usuário — cada usuário só acessa suas próprias tarefas
- Tratamento global de exceções com respostas de erro padronizadas
- Validação de dados de entrada com **Bean Validation**
- Documentação interativa da API com **Swagger/OpenAPI**
- Testes unitários com **JUnit** e **Mockito**
- Deploy em produção com banco de dados **PostgreSQL**

## 🎖 Tecnologias utilizadas

- **Java 21**
- **Spring Boot 4**
- **Spring Data JPA** (persistência)
- **Spring Security** (autenticação e autorização)
- **JWT** (JSON Web Token)
- **BCrypt** (hash de senhas)
- **PostgreSQL** (banco de dados relacional)
- **Bean Validation** (validação de requisições)
- **Swagger / OpenAPI** (documentação da API)
- **JUnit 5** e **Mockito** (testes unitários)
- **Docker** (containerização para deploy)
- **Maven** (gerenciamento de dependências)

## 🏗 Arquitetura

O projeto segue uma arquitetura em camadas, separando responsabilidades:

```
com.danilo.taskmanager
├── config          → configurações do Spring (Security, OpenAPI)
├── controller       → endpoints REST
├── dto              → objetos de transferência de dados (request/response)
├── exception         → exceções customizadas e tratamento global de erros
├── model             → entidades JPA (Task, User)
├── repository        → interfaces de acesso ao banco de dados
├── security           → JWT, filtros de autenticação
└── service            → regras de negócio
```

## 📋 Endpoints principais

### Autenticação (públicos)
| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/api/auth/register` | Cadastra um novo usuário |
| POST | `/api/auth/login` | Autentica e retorna um token JWT |

### Tarefas (requerem autenticação)
| Método | Endpoint | Descrição |
|---|---|---|
| GET | `/api/tasks` | Lista todas as tarefas do usuário logado |
| GET | `/api/tasks/{id}` | Busca uma tarefa por ID |
| GET | `/api/tasks/status/{status}` | Filtra tarefas por status |
| POST | `/api/tasks` | Cria uma nova tarefa |
| PUT | `/api/tasks/{id}` | Atualiza uma tarefa existente |
| DELETE | `/api/tasks/{id}` | Remove uma tarefa |

A lista completa de endpoints, com exemplos de request/response, está disponível na [documentação Swagger](https://task-manager-api-2y19.onrender.com/swagger-ui.html).

## 🚀 Rodando o projeto localmente

### Pré-requisitos
- Java 21+
- Maven (ou use o `mvnw` incluído no projeto)
- PostgreSQL rodando localmente

### Passos

1. Clone o repositório:
```bash
git clone https://github.com/euudanilo/task-manager-api.git
cd task-manager-api
```

2. Crie um banco de dados PostgreSQL local (ex: `taskdb`).

3. Configure as variáveis de ambiente (ou ajuste os valores padrão em `application.properties`):
```
DATABASE_URL=jdbc:postgresql://localhost:5432/taskdb
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
JWT_SECRET=sua_chave_secreta
```

4. Rode a aplicação:
```bash
./mvnw spring-boot:run
```

5. Acesse a documentação local em `http://localhost:8080/swagger-ui.html`.

### Rodando os testes
```bash
./mvnw test
```

## 🐳 Deploy

A aplicação é containerizada com Docker e está hospedada no [Render](https://render.com), com banco de dados PostgreSQL gerenciado pelo [Neon](https://neon.tech).

## 👤 Autor

**Danilo Alves**
Estudante de Engenharia de Software
📧 contacteuudan@gmail.com
