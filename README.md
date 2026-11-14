# ESF — Gestão de Frota

Aplicação de gestão de frota com backend Spring Boot, MySQL e interface servida pelo próprio backend.

## Requisitos

- Java 17 ou superior
- MySQL 8
- Navegador atualizado

## Executar no Windows com Docker Desktop

1. Instale e abra o Docker Desktop, aguardando o status **Engine running**.
2. Extraia o ZIP e abra o PowerShell na pasta que contém `compose.yaml`.
3. Crie o arquivo local de configuração e defina uma senha para o MySQL:

   ```powershell
   Copy-Item .env.example .env
   notepad .env
   ```

   No `.env`, substitua o valor de `DB_PASSWORD` por uma senha local sua. Esse arquivo é ignorado pelo Git e não deve ser enviado aos alunos.

4. Inicie os serviços:

   ```powershell
   docker compose up --build -d
   ```

5. Aguarde o MySQL ficar saudável e o backend iniciar:

   ```powershell
   docker compose ps
   docker compose logs -f app
   ```

6. Abra `http://localhost:8080/`, selecione **Criar Conta** e cadastre o usuário do aluno.

Para encerrar mantendo os dados, use `docker compose down`. Para acompanhar o backend depois, use `docker compose logs -f app`.

## Executar localmente sem Docker

1. Crie o banco de dados e um usuário com permissão para criar tabelas:

   ```sql
   CREATE DATABASE esf_frota CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

2. Configure as credenciais do banco no ambiente. O backend usa `DB_USER=root` por padrão e a senha vem de `DB_PASSWORD`:

   ```bash
   export DB_USER=seu_usuario
   export DB_PASSWORD=sua_senha
   ```

3. Inicie o projeto (PowerShell):

   ```powershell
   $env:DB_USER = "root"
   $env:DB_PASSWORD = "sua_senha_do_mysql"
   .\mvnw.cmd spring-boot:run
   ```

   Na primeira execução, `src/main/resources/schema.sql` cria as tabelas no banco selecionado.

4. Abra `http://localhost:8080/`, selecione **Criar Conta**, cadastre um usuário e entre.

O frontend em `src/main/resources/static/index.html` usa a mesma origem da API. A autenticação da sessão do navegador usa HTTP Basic mantido somente em memória; sair ou recarregar a página exige novo login. Use HTTPS fora do ambiente local.

## API

As rotas de negócio em `/api/**` exigem autenticação HTTP Basic. O login e o cadastro inicial ficam em:

- `POST /api/auth/login` — JSON `{ "email": "...", "senha": "..." }`
- `POST /api/auth/register` — JSON com `nome`, `email`, `cpf`, `cargo` e `senha`

Os JSONs usam nomes em camelCase e os valores literais dos enums Java. Senhas novas são armazenadas com BCrypt e não são incluídas nas respostas. Contas antigas cuja senha foi gravada em texto puro são migradas para BCrypt no primeiro login válido.
