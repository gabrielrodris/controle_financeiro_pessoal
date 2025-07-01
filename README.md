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
   cd gabriel_portifolio
   ```

2. **Configurar o Banco de Dados**:
   - Crie um banco MySQL:
     ```sql
     CREATE DATABASE controle_financeiro;
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
   - Acesse em `http://localhost:8080`.

4. **Testar com Swagger**:
   - Abra `http://localhost:8080/swagger-ui.html` para testar os endpoints.

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
