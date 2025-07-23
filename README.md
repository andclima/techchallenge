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
```

#### Para DonoRestaurante:

```json
{
  "nome": "Maria",
  "email": "maria@email.com",
  "username": "maria123",
  "password": "senha123",
  "endereco": "Rua B",
  "tipo": "dono",
  "nomeDoRestaurante": "Sabor da Serra"
}
```

🔸 Respostas

201 CREATED – Usuário criado com sucesso.

409 CONFLICT – username ou campo exclusivo (numeroFidelidade, nomeDoRestaurante) já existente.

400 BAD REQUEST – Tipo inválido ou campo obrigatório ausente.

---

🔍 Buscar Usuários por Nome
GET /usuarios?q=joao

Retorna lista de usuários cujo nome contém a string q.

Inclui campos exclusivos conforme o tipo (numeroFidelidade, nomeDoRestaurante).

---

📌 Buscar por ID
GET /usuarios/{id}

Retorna um usuário pelo ID.

---

✏️ Atualizar Usuário
PUT /usuarios

🔸 Corpo da Requisição

```json
{
  "id": 1,
  "nome": "João Atualizado",
  "email": "joao@novo.com",
  "endereco": "Rua Nova",
  "numeroFidelidade": "FID99999" 
}
```
🔸 Campo numeroFidelidade deve ser enviado apenas para clientes.

🔸 Regras de Validação

409 CONFLICT – Ao tentar atualizar numeroFidelidade para um valor já existente.

400 BAD REQUEST – Se um cliente tentar alterar nomeDoRestaurante, ou vice-versa.

---

🔒 Alterar Senha
POST /change-password

🔸 Corpo da Requisição

```json
{
  "username": "joao123",
  "password": "senhaAntiga",
  "newPassword": "senhaNova"
}
```

🔸 Respostas

200 OK – Senha alterada com sucesso.

400 BAD REQUEST – Campos inválidos ou senha atual incorreta.

---

## ✅ Boas Práticas Aplicadas

- ✅ Princípios **SOLID** (`SRP`, `OCP`, `LSP`, `ISP`, `DIP`)
- ✅ Uso de **DTOs** para entrada e saída de dados
- ✅ Definição de **Enum** para tipos de usuário
- ✅ Herança utilizando `@Inheritance(strategy = JOINED)`
- ✅ Validações condicionais com base no tipo do usuário
- ✅ Tratamento de erro `409 CONFLICT` para campos únicos
- ✅ Separação clara de responsabilidades entre `service`, `controller` e `model`


