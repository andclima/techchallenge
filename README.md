# 🧾 Sistema de Cadastro de Usuários – Spring Boot

Projeto backend para gerenciamento de usuários com dois perfis distintos: **Cliente** e **Dono de Restaurante**, utilizando Java, Spring Boot e boas práticas de arquitetura como SOLID, DTOs, herança e validação.

---

## 🚀 Tecnologias Utilizadas

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- MariaDB (configurável)
- Spring Security (para codificação de senha)
- Lombok (opcional)
- Maven

---

## 🧠 Visão Geral

### Tipos de Usuário

| Tipo              | Campo exclusivo         |
|-------------------|--------------------------|
| `Cliente`         | `numeroFidelidade`       |
| `DonoRestaurante` | `nomeDoRestaurante`      |

Todos os usuários herdam da superclasse `Usuario`, que possui os campos comuns:

- `nome`, `email`, `username`, `senha`, `endereco`

---

## 🛠️ Endpoints

### 📌 Criar Usuário

```http
POST /usuarios
🔸 Corpo da Requisição

Para Cliente:

{
  "nome": "João",
  "email": "joao@email.com",
  "username": "joao123",
  "password": "senha123",
  "endereco": "Rua A",
  "tipo": "cliente",
  "numeroFidelidade": "FID12345"
}

Para DonoRestaurante:

{
  "nome": "Maria",
  "email": "maria@email.com",
  "username": "maria123",
  "password": "senha123",
  "endereco": "Rua B",
  "tipo": "dono",
  "nomeDoRestaurante": "Sabor da Serra"
}
🔸 Respostas
201 CREATED com usuário criado

409 CONFLICT se username ou campo exclusivo (numeroFidelidade, nomeDoRestaurante) já existir

400 BAD REQUEST se o tipo for inválido ou campo obrigatório ausente

🔍 Buscar Usuários por Nome

GET /usuarios?q=joao
Retorna lista de usuários com nome contendo q

Inclui campos exclusivos conforme o tipo

📌 Buscar por ID

GET /usuarios/{id}
✏️ Atualizar Usuário
PUT /usuarios
🔸 Corpo:
{
  "id": 1,
  "nome": "João Atualizado",
  "email": "joao@novo.com",
  "endereco": "Rua Nova",
  "numeroFidelidade": "FID99999" // apenas para cliente
}
409 CONFLICT se tentar atualizar numeroFidelidade para um já usado

400 BAD REQUEST se cliente tentar alterar nomeDoRestaurante (e vice-versa)

🔒 Alterar Senha

POST /change-password
{
  "username": "joao123",
  "password": "senhaAntiga",
  "newPassword": "senhaNova"
}

🧪 Exemplos de Responses
{
  "id": 5,
  "nome": "Maria",
  "email": "maria@email.com",
  "username": "maria123",
  "endereco": "Rua B",
  "nomeDoRestaurante": "Sabor da Serra",
  "numeroFidelidade": null
}
  
✅ Boas Práticas Aplicadas
✅ Princípios SOLID (SRP, OCP, LSP, ISP, DIP)
✅ DTOs para entrada e saída
✅ Enum para tipos de usuário
✅ Herança com @Inheritance(strategy = JOINED)
✅ Validações condicionais por tipo
✅ Tratamento de erro 409 para dados únicos
✅ Separação de responsabilidade (service, controller, model)