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

## 🚀 Endpoints

### 📌 Criar Usuário

**POST** `/usuarios`

🔸 **Corpo da Requisição**

#### Para Cliente:

```json
{
  "nome": "João",
  "email": "joao@email.com",
  "username": "joao123",
  "password": "senha123",
  "endereco": "Rua A",
  "tipo": "cliente",
  "numeroFidelidade": "FID12345"
}

