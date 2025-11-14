# Universidade — Projeto Spring Boot (AP2 - TAP)

Este repositório contém uma aplicação Spring Boot para gerenciamento de um sistema universitário, desenvolvida como projeto da disciplina de Técnicas Avançadas de Programação.

## 🎯 AP2 - Problemas Resolvidos

### ✅ 1. Sistema de Inscrição (Factory Pattern)
- Criação de inscrições regulares e de transferência
- Factory Pattern para diferentes tipos de inscrição

### ✅ 2. Matrícula do Aluno em Curso
- Relacionamento Aluno ↔ Curso
- CRUD completo de alunos com matrícula

### ✅ 3. Cálculo de Mensalidade (Strategy Pattern)
- Três estratégias: Integral, Meio Período, Bolsista
- Cálculo dinâmico baseado em inscrições

### 🆕 4. Professor (Conforme UML)
- Entidade Professor integrada ao modelo
- Relacionamento Professor ↔ Turma (1 para *)

## 📚 Documentação

- **[QUICK_START.md](QUICK_START.md)** - Guia rápido para iniciar e testar (5 min)
- **[RESUMO_EXECUTIVO.md](RESUMO_EXECUTIVO.md)** - Resumo para apresentação
- **[RESUMO_IMPLEMENTACAO.md](RESUMO_IMPLEMENTACAO.md)** - Detalhes técnicos completos
- **[TROUBLESHOOTING.md](TROUBLESHOOTING.md)** - Resolução de problemas comuns
- **[API_PROFESSOR.md](API_PROFESSOR.md)** - Documentação da API de professores

## Sumário

- [Pré-requisitos](#pré-requisitos)
- [Setup Rápido](#setup-rápido)
- [Design Patterns Implementados](#design-patterns-implementados)
- [Endpoints da API](#endpoints-da-api)
- [Problemas Comuns](#problemas-comuns)
- [Testes](#testes)

## Pré-requisitos

- Java 17 (JDK 17)
- Maven 3.6+ (ou utilize o wrapper Maven incluido: `./mvnw`)
- MySQL rodando localmente
- Git

Confirme sua versão do Java:

```bash
java -version
```

## Setup Rápido

### 1. Clonar o Repositório
```bash
git clone <url-do-repositorio>
cd TAP-Refactoring202523
```

### 2. Configurar Banco de Dados
```bash
# Criar banco
mysql -u root -p
CREATE DATABASE universidade_db;
exit;

# Aplicar migrações
mysql -u root -p universidade_db < FIX_SITUACAO_COLUMN.sql
mysql -u root -p universidade_db < ADD_PROFESSOR_TABLE.sql
```

### 3. Executar Aplicação
```bash
./mvnw spring-boot:run
```

### 4. Acessar Swagger
```
http://localhost:8080/swagger-ui.html
```

## Design Patterns Implementados

### 1. Factory Pattern 🏭
**Onde:** `factory/InscricaoFactory.java`  
**Uso:** Criação de diferentes tipos de inscrição

```java
InscricaoRegularFactory - Inscrição para novos alunos
InscricaoTransferenciaFactory - Inscrição para transferidos
```

### 2. Strategy Pattern 📊
**Onde:** `strategy/MensalidadeStrategy.java`  
**Uso:** Cálculo de mensalidade com diferentes regras

```java
MensalidadeIntegralStrategy - 100% do valor base
MensalidadeMeioPeriodoStrategy - 50% do valor base
MensalidadeBolsistaStrategy - 30% do valor base
```

### 3. Repository Pattern 💾
**Onde:** `repository/*Repository.java`  
**Uso:** Abstração de acesso a dados com Spring Data JPA

### 4. Service Layer Pattern 🔧
**Onde:** `service/*Service.java`  
**Uso:** Lógica de negócio separada dos controllers

### 5. DTO Pattern 📦
**Onde:** `dto/*DTO.java`  
**Uso:** Transferência de dados entre camadas

## Endpoints da API

### Cursos
- `GET /api/curso` - Listar todos
- `POST /api/curso` - Criar novo
- `GET /api/curso/{codigo}` - Buscar por código
- `PUT /api/curso/{codigo}` - Atualizar
- `DELETE /api/curso/{codigo}` - Remover

### Disciplinas
- `GET /api/disciplina` - Listar todas
- `POST /api/disciplina` - Criar nova
- `GET /api/disciplina/{codigo}` - Buscar por código
- `PUT /api/disciplina/{codigo}` - Atualizar
- `DELETE /api/disciplina/{codigo}` - Remover

### Professores ⭐ NOVO
- `GET /api/professores` - Listar todos
- `POST /api/professores` - Criar novo
- `GET /api/professores/{codigo}` - Buscar por código
- `PUT /api/professores/{codigo}` - Atualizar
- `DELETE /api/professores/{codigo}` - Remover

### Turmas
- `GET /api/turma` - Listar todas
- `POST /api/turma` - Criar nova (agora aceita professor)
- `GET /api/turma/{codigo}/{ano}/{semestre}` - Buscar específica
- `PUT /api/turma/{codigo}/{ano}/{semestre}` - Atualizar
- `DELETE /api/turma/{codigo}/{ano}/{semestre}` - Remover

### Alunos
- `GET /api/aluno` - Listar todos
- `POST /api/aluno` - Criar novo (com matrícula em curso)
- `GET /api/aluno/{matricula}` - Buscar por matrícula
- `PUT /api/aluno/{matricula}` - Atualizar
- `DELETE /api/aluno/{matricula}` - Remover

### Inscrições (Factory Pattern) ⭐
- `GET /api/inscricao` - Listar todas
- `POST /api/inscricao/regular/{matricula}/{codigo}/{ano}/{semestre}` - Criar inscrição regular
- `POST /api/inscricao/transferencia/{matricula}/{codigo}/{ano}/{semestre}` - Criar inscrição de transferência
- `GET /api/inscricao/{matricula}/{codigo}/{ano}/{semestre}` - Buscar específica
- `PUT /api/inscricao/{matricula}/{codigo}/{ano}/{semestre}/notas` - Lançar notas
- `DELETE /api/inscricao/{matricula}/{codigo}/{ano}/{semestre}` - Remover

### Mensalidades (Strategy Pattern) ⭐
- `GET /api/mensalidades/aluno/{matricula}/integral` - Calcular mensalidade integral
- `GET /api/mensalidades/aluno/{matricula}/meio-periodo` - Calcular meio período
- `GET /api/mensalidades/aluno/{matricula}/bolsista` - Calcular com desconto de bolsista
- `GET /api/mensalidades/aluno/{matricula}/comparar` - Comparar todas as estratégias

## Problemas Comuns

Deve exibir algo como `openjdk version "17.x.x"`.

## Clonando o repositório

No terminal (pasta onde quer colocar o projeto):

```bash
git clone <URL_DO_REPOSITORIO>
cd TAP-Refactoring20252
```

Substitua `<URL_DO_REPOSITORIO>` pela URL do repositório remoto (HTTPS ou SSH).

## Configuração do banco de dados (MySQL)

A aplicação está configurada por padrão para conectar em um MySQL local com as seguintes credenciais (arquivo `src/main/resources/application.properties`):

- URL: jdbc:mysql://localhost:3306/universidade_db?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
- Usuário: `root`
- Senha: `admin`

Observações:
- O parâmetro `createDatabaseIfNotExist=true` (na URL) permite que o MySQL crie o banco `universidade_db` automaticamente se o usuário tiver permissão.
- A propriedade `spring.jpa.hibernate.ddl-auto=update` faz o Hibernate ajustar o esquema automaticamente conforme as entidades.

Se quiser usar outras credenciais ou um servidor remoto, edite `src/main/resources/application.properties` (ou use variáveis de ambiente) antes de iniciar a aplicação. Exemplo com variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://meu-host:3306/universidade_db?createDatabaseIfNotExist=true"
export SPRING_DATASOURCE_USERNAME="meu_usuario"
export SPRING_DATASOURCE_PASSWORD="minha_senha"
```

No macOS com zsh, você pode exportar no terminal atual; para persistir, coloque no `~/.zshrc`.

### Criando o usuário e banco manualmente (opcional)

Se você preferir criar o banco e o usuário manualmente no MySQL:

1. Acesse o MySQL como root:

```bash
mysql -u root -p
# digite a senha do root
```

2. Execute os comandos (substitua `admin` pela senha desejada):

```sql
CREATE DATABASE IF NOT EXISTS universidade_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON universidade_db.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

> Dica: para ambiente de desenvolvimento é comum usar um usuário com permissões amplas; em produção, crie um usuário com permissões mínimas necessárias.

## Rodando a aplicação

No diretório do projeto você pode usar o wrapper Maven incluído (recomendado) ou o Maven instalado globalmente.

Com o wrapper (Unix/macOS):

```bash
./mvnw spring-boot:run
```

Ou com Maven local:

```bash
mvn spring-boot:run
```

Ou gerar um jar e executar:

```bash
./mvnw clean package -DskipTests
java -jar target/universidade-0.0.1-SNAPSHOT.jar
```

Ao iniciar, você verá logs mostrando a conexão com o banco e o Hibernate atualizando/criando as tabelas. A aplicação estará disponível em http://localhost:8080 por padrão (configurado em `application.properties`).

Se você usou variáveis de ambiente para sobrescrever as configurações do banco, garanta que elas estejam definidas no mesmo terminal antes de iniciar.

## Endpoints úteis

- Swagger UI (documentação interativa): http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

Também existem diversos controllers no projeto (`AlunoController`, `CursoController`, `DisciplinaController`, `TurmaController`, `InscricaoController`) com endpoints REST para manipular as entidades.

## Como a criação automática do banco funciona

Duas configurações trabalham juntas:

1. Na URL do datasource há `createDatabaseIfNotExist=true`, parâmetro do conector MySQL que instrui o servidor a criar o schema se ele não existir (desde que o usuário tenha permissão).
2. `spring.jpa.hibernate.ddl-auto=update` faz com que o Hibernate compare as entidades JPA com o esquema e execute alterações necessárias (criação/alteração de tabelas/colunas) sem excluir dados.

Isto permite iniciar a aplicação sem criar manualmente o banco e ainda assim ter as tabelas geradas automaticamente.

## Problemas comuns e soluções

1. Erro de conexão (Access denied / authentication):
   - Verifique usuário/senha no `application.properties`.
   - Teste a conexão direta com o MySQL usando `mysql -u root -p -h localhost -P 3306`.

2. Porta 8080 ocupada:
   - Pare o serviço que está usando a porta ou mude a porta editando `server.port` em `application.properties`.

3. Versão do Java incompatível:
   - Projeto configurado para Java 17 no `pom.xml`. Instale JDK 17 ou a versão apropriada.

4. Permissões para criar o banco automaticamente:
   - Se `createDatabaseIfNotExist=true` falhar, crie o banco manualmente conforme a seção acima e tente novamente.

5. Dependências faltando / build falhando:
   - Execute `./mvnw clean package` para ver os erros completos. Se faltarem credenciais do Maven ou problemas de rede, corrija antes de rodar.

## Testes

O projeto contém testes básicos de Spring Boot. Execute:

```bash
./mvnw test
```

## Observações finais

- O autor do repositório testou os passos acima: ao clonar e executar a aplicação, o banco foi criado automaticamente e a aplicação ficou funcional.
- Em produção, reveja configurações de `ddl-auto`, credenciais e parâmetros de criação automática de banco — essas opções são práticas para desenvolvimento, mas podem ser perigosas em produção.

Se quiser, posso:
- Adicionar instruções para rodar com Docker (MySQL + app)
- Incluir um script `docker-compose.yml` para facilitar a execução local
- Documentar endpoints principais com exemplos de requisições curl

---

README gerado automaticamente pelo assistente para o projeto.
