# Controle Financeiro Backend

## Descrição
API RESTful para gerenciamento de finanças pessoais, permitindo criar, listar e gerenciar usuários, categorias, transações e metas financeiras.

## Tecnologias
- Java 17
- Spring Boot 3.x
- MySQL
- Maven
- Swagger (Springdoc OpenAPI)

## Configuração
1. **Clonar o Repositório**:
   ```bash
   git clone https://github.com/gabrielrodris/controle_financeiro_pessoal.git
   cd controle_financeiro_pessoal
   ```

2. **Configurar o Banco de Dados**:
   - Crie um banco MySQL:
     ```sql
     CREATE DATABASE if not exists controle_financeiro;

     use controle_financeiro;

     CREATE TABLE usuario (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       nome VARCHAR(255) NOT NULL,
       email VARCHAR(255) NOT NULL UNIQUE,
       senha VARCHAR(255) NOT NULL,
       data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
      );

      CREATE TABLE categoria (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       nome VARCHAR(255) NOT NULL,
       tipo_transacao VARCHAR(50) NOT NULL
      );

      CREATE TABLE meta_financeira (
       id  BIGINT AUTO_INCREMENT PRIMARY KEY,
       nome VARCHAR(255) NOT NULL,
       valor_objetivo DECIMAL(19,2) NOT NULL,
       data_limite DATE NOT NULL,
       usuario_id BIGINT NOT NULL,
       CONSTRAINT fk_meta_financeira_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE
      );

      CREATE TABLE transacao (
       id BIGINT AUTO_INCREMENT PRIMARY KEY,
       descricao VARCHAR(255) NOT NULL,
       valor DECIMAL(19,2) NOT NULL,
       data DATE NOT NULL,
       tipo VARCHAR(50) NOT NULL,

       usuario_id BIGINT NOT NULL,
       categoria_id BIGINT NOT NULL,

       CONSTRAINT fk_transacao_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id)
        ON DELETE CASCADE,

       CONSTRAINT fk_transacao_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
      );
     ```
   - Edite `src/main/resources/application.properties`:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/controle_financeiro
     spring.datasource.username=seu_usuario
     spring.datasource.password=sua_senha
     spring.jpa.hibernate.ddl-auto=update
     springdoc.swagger-ui.path=/swagger-ui.html
     ```

3. **Executar a Aplicação**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   - Acesse em `http://localhost:8081`.

4. **Testar com Swagger**:
   - Abra `http://localhost:8081/swagger-ui.html` para testar os endpoints.

## Endpoints Principais
- **POST /transacoes**: Criar transação
  ```json
  {
    "descricao": "Compra de supermercado",
    "valor": 200.00,
    "data": "2025-07-01",
    "tipo": "SAIDA",
    "usuarioId": 1,
    "categoriaId": 1
  }
  ```
- **GET /transacoes/usuario/{usuarioId}/categoria/{categoriaId}**: Listar transações por usuário e categoria
- **POST /metasFinanceira**: Criar meta financeira
- **GET /categorias**: Listar categorias

## Dados de Teste
```sql
INSERT INTO usuario (id, nome, email, senha, data_cadastro) VALUES (1, 'John Doe', 'john@example.com', 'password', '2025-07-01');
INSERT INTO categoria (id, nome, tipo_transacao) VALUES (1, 'Supermercado', 'SAIDA');
INSERT INTO transacao (id, descricao, valor, data, tipo, usuario_id, categoria_id) VALUES (1, 'Compra de supermercado', 200.00, '2025-07-01', 'SAIDA', 1, 1);
```
