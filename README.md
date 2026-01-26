#  SimLab - Sistema Simplificado de Laboratório

API REST para gestão de pacientes e exames laboratoriais, desenvolvida com Spring Boot.

##  Sobre o Projeto

Sistema que permite o cadastro e gerenciamento de pacientes e seus respectivos exames laboratoriais, com relacionamento entre as entidades e validações de negócio.

##  Tecnologias Utilizadas

- **Java 17** (LTS)
- **Spring Boot 3.4.1**
- **Spring Data JPA** (Persistência)
- **H2 Database** (Banco de dados em memória)
- **Bean Validation** (Validação de dados)
- **Maven** (Gerenciamento de dependências)
- **Swagger 2.7.0**

## Estrutura do Projeto
```
src/main/java/com/example/simlab/
├── model/          # Entidades JPA (Paciente, Exame)
├── repository/     # Interfaces de acesso aos dados
├── dto/            # Objetos de transferência de dados
├── service/        # Lógica de negócio
├── controller/     # Endpoints REST
└── exception/      # Tratamento de erros customizado
```

##  Funcionalidades

### Pacientes
- ✅ Criar paciente (com validação de Cartão de Cidadão único)
- ✅ Listar pacientes (com paginação e filtros)
- ✅ Buscar paciente por ID
- ✅ Atualizar dados do paciente
- ✅ Remover paciente

### Exames
- ✅ Criar exame (vinculado a um paciente)
- ✅ Listar exames (com paginação e filtros)
- ✅ Buscar exame por ID
- ✅ Atualizar dados do exame
- ✅ Remover exame

### Validações
- ✅ Cartão de Cidadão único por paciente
- ✅ Nome de exame único
- ✅ Exame deve estar vinculado a um paciente existente
- ✅ Campos obrigatórios validados

##  Relacionamentos

**Paciente 1:N Exame** - Um paciente pode ter múltiplos exames
- `@OneToMany` em Paciente
- `@ManyToOne` em Exame

##  Pré-requisitos

- Java 17 ou superior
- Maven 3.6+

##  Como Executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/simlab.git
cd simlab
```

2. Execute o projeto:
```bash
mvn spring-boot:run
```

3. A API estará disponível em: `http://localhost:8080`

##  Endpoints da API

### Pacientes

#### Criar Paciente
```http
POST /pacientes
Content-Type: application/json

{
  "nome": "Maria Silva",
  "dataDeNascimento": "1985-03-15",
  "cartaoCidadao": "12345678",
  "telefone": "912345678",
  "email": "maria@email.com"
}
```

#### Listar Pacientes
```http
GET /pacientes
GET /pacientes?nome=Maria
GET /pacientes?cartaoCidadao=12345678
GET /pacientes?dataDeNascimento=1985-03-15
```

#### Buscar Paciente por ID
```http
GET /pacientes/{id}
```

#### Atualizar Paciente
```http
PUT /pacientes/{id}
Content-Type: application/json

{
  "nome": "Maria Silva Santos",
  "dataDeNascimento": "1985-03-15",
  "cartaoCidadao": "12345678",
  "telefone": "919999999",
  "email": "maria.nova@email.com"
}
```

#### Remover Paciente
```http
DELETE /pacientes/{id}
```

---

### Exames

#### Criar Exame
```http
POST /exames
Content-Type: application/json

{
  "nome": "Hemograma Completo",
  "descricao": "Análise completa do sangue",
  "preco": 25.50,
  "pacienteId": 1
}
```

#### Listar Exames
```http
GET /exames
GET /exames?nome=Hemograma
GET /exames?descricao=sangue
```

#### Buscar Exame por ID
```http
GET /exames/{id}
```

#### Atualizar Exame
```http
PUT /exames/{id}
Content-Type: application/json

{
  "nome": "Hemograma Completo",
  "descricao": "Análise completa do sangue - Atualizado",
  "preco": 30.00
}
```

#### Remover Exame
```http
DELETE /exames/{id}
```

##  Respostas da API

### Sucesso

**200 OK** - Consulta/Atualização bem-sucedida
```json
{
  "id": 1,
  "nome": "Maria Silva",
  "dataDeNascimento": "1985-03-15",
  "cartaoCidadao": "12345678",
  "telefone": "912345678",
  "email": "maria@email.com"
}
```

**201 Created** - Recurso criado com sucesso

**204 No Content** - Remoção bem-sucedida

### Erros

**400 Bad Request** - Dados inválidos ou duplicados
```json
{
  "timestamp": "2026-01-16T16:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Não é possível cadastrar paciente, pois já existe paciente com este Cartão Cidadão"
}
```

**404 Not Found** - Recurso não encontrado
```json
{
  "timestamp": "2026-01-16T16:30:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Paciente não encontrado com ID: 999"
}
```

##  Banco de Dados

O projeto utiliza **H2 Database** (em memória):
- Dados são resetados a cada reinicialização
- Console H2 disponível em: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:simlab`
- Username: `sa`
- Password: *(vazio)*

##  Testes

28 testes manuais realizados via Postman:
- ✅ 16 testes de Pacientes (CRUD + validações)
- ✅ 12 testes de Exames (CRUD + validações)
- ✅ 100% de cobertura dos endpoints

##  Aprendizados

Este projeto foi desenvolvido como parte do aprendizado de:
- Relacionamentos JPA (`@OneToMany`, `@ManyToOne`)
- DTOs para separação de camadas
- Validações com Bean Validation
- Tratamento customizado de exceptions
- Arquitetura em camadas (Controller-Service-Repository)
- Boas práticas REST

## 👩‍💻 Autora

**Amanda** - Estudante de Desenvolvimento Backend Java



⭐ Se este projeto foi útil, considere dar uma estrela!
