# ESF — Gestão de Frota

Aplicação de gestão de frota com backend Spring Boot, MySQL e interface servida pelo próprio backend.

## Requisitos

- Java 17 ou superior
- MySQL 8
- Navegador atualizado

## Executar localmente

1. Crie o banco de dados e um usuário com permissão para criar tabelas:

   ```sql
   CREATE DATABASE esf_frota CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Configure as credenciais do banco no ambiente; o padrão local é `DB_USER=root` e senha vazia:

   ```bash
   export DB_USER=seu_usuario
   export DB_PASSWORD=sua_senha
   ```

3. Inicie o projeto:

   ```bash
   bash ./mvnw spring-boot:run
   ```

   Na primeira execução, `src/main/resources/schema.sql` cria as tabelas no banco selecionado.

4. Abra `http://localhost:8080/`, selecione **Criar Conta**, cadastre um usuário e entre.

O frontend em `src/main/resources/static/index.html` usa a mesma origem da API. A autenticação da sessão do navegador usa HTTP Basic mantido somente em memória; sair ou recarregar a página exige novo login. Use HTTPS fora do ambiente local.

## Executar com Docker

Defina uma senha de desenvolvimento e suba a aplicação e o MySQL:

```bash
DB_PASSWORD='senha-local-forte' docker compose up --build -d
```

Abra `http://localhost:8080/`. A API usa o serviço MySQL da rede Compose; a porta local `3307` fica disponível para ferramentas de administração. Para desligar sem apagar os dados, use `docker compose down`. Para remover também o banco descartável, use `docker compose down --volumes`.

## API

As rotas de negócio em `/api/**` exigem autenticação HTTP Basic. O login e o cadastro inicial ficam em:

- `POST /api/auth/login` — JSON `{ "email": "...", "senha": "..." }`
- `POST /api/auth/register` — JSON com `nome`, `email`, `cpf`, `cargo` e `senha`

Os JSONs usam nomes em camelCase e os valores literais dos enums Java. Senhas novas são armazenadas com BCrypt e não são incluídas nas respostas. Contas antigas cuja senha foi gravada em texto puro são migradas para BCrypt no primeiro login válido.
