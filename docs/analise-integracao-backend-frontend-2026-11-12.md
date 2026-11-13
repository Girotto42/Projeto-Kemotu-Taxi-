# Integração entre backend e frontend — análise e plano de correção

**Data:** 12/11/2026  
**Projeto:** ESF — Easy System Fleet / Kemotu Táxi  
**Público:** alunos responsáveis pelo desenvolvimento e integração

## Objetivo

Este documento registra os problemas que impedem ou tornam inconsistente a comunicação entre a interface web e a API Spring Boot. Também define uma ordem de trabalho e critérios de aceite para que a equipe possa corrigir e demonstrar a integração.

> Este documento é um plano técnico. A integração só estará concluída depois que os critérios de aceite forem verificados com a aplicação e o banco configurados.

## Diagnóstico

### 1. Login da interface não corresponde à API

A interface envia `POST /api/auth/login`, mas não há controller nem endpoint de autenticação no backend. A regra que libera `/api/auth/**` apenas permite acesso a essa rota caso ela exista; não implementa o login. As demais rotas exigem autenticação HTTP Basic, mas as chamadas `fetch` da interface não enviam credenciais.

**Efeito:** o login integrado não funciona e as chamadas à API protegida não são autorizadas. O modo local do frontend pode mascarar esse problema.

### 2. A entidade de funcionário expõe dados sensíveis

O cadastro envia a senha no JSON e a entidade `Funcionario` contém o campo `senha`. O controller retorna entidades diretamente, inclusive na listagem. Não há indicação de hash no fluxo de persistência nem exclusão desse campo das respostas.

**Efeito:** senhas podem ser armazenadas em texto puro e retornadas por endpoints de funcionário. Não se deve liberar a integração antes de corrigir esse fluxo.

### 3. Identificadores e propriedades não seguem um contrato comum

O backend usa nomes Java/Jackson em camelCase, como `idVeiculo`, `anoFabricacao` e `quantidadePortas`. O frontend espera em vários pontos nomes como `id`, `ano`, `portas` e `id_veiculo`.

**Efeito:** campos aparecem como indefinidos, relações não são encontradas e botões de editar/excluir podem gerar URLs com identificador ausente.

### 4. Valores de situação e tipo são incompatíveis

Os enums Java usam valores como `Disponivel`, `Em_uso` e `Em_manutencao`. A interface envia ou compara textos com acentos e espaços, como `Disponível`, `Em uso` e `Em manutenção`. Há divergências semelhantes nos estados de manutenção e nos nomes de propriedades, por exemplo `tipoManutencao` no backend e `tipo` na interface.

**Efeito:** desserialização pode falhar ao gravar; mesmo quando a leitura funciona, filtros, badges e contadores podem classificar incorretamente os registros.

### 5. Erros da API são confundidos com gravações bem-sucedidas

Em operações de veículo, respostas HTTP não exitosas levam a alterações no `localStorage`. Na exclusão, a interface remove o registro local mesmo sem verificar `resposta.ok`. Outros fluxos atualizam a interface após a chamada sem validar o status HTTP.

**Efeito:** a tela pode mostrar um estado que não existe no banco, por exemplo quando a API retorna `401`, `400` ou `500`.

### 6. Configuração e documentação de execução são insuficientes

O backend aponta para MySQL em `localhost`, usa `DB_USER` e `DB_PASSWORD` e está configurado com `spring.jpa.hibernate.ddl-auto=none`. Não foi encontrado script de criação do esquema nem instrução de configuração; o README atual não documenta a inicialização.

**Efeito:** a aplicação depende de configuração manual e tabelas previamente criadas, o que dificulta reproduzir a integração em outra máquina.

### 7. Dados inseridos na página são renderizados como HTML

Valores vindos da API são interpolados em strings atribuídas a `innerHTML`. Campos como nome, descrição, placa e modelo devem ser tratados como dados não confiáveis.

**Efeito:** conteúdo especialmente preparado pode ser interpretado como HTML/JavaScript pelo navegador. A interface deve escapar os valores ou criar elementos e inserir texto com `textContent`.

## Contrato recomendado entre interface e API

Adotem um único padrão. Para este projeto, recomenda-se manter **camelCase** nos JSONs, acompanhando os nomes Java, e usar sempre os enums exatamente como definidos no contrato. Não alternem entre `idVeiculo`, `id_veiculo` e `id`.

### Exemplo: veículo

Resposta de `GET /api/veiculos`:

```json
[
  {
    "idVeiculo": 12,
    "placa": "ABC1D23",
    "marca": "Chevrolet",
    "modelo": "Onix Sedan",
    "anoFabricacao": 2023,
    "quantidadePortas": 4,
    "quilometragem": 45200.00,
    "situacao": "Em_uso"
  }
]
```

Corpo de `POST /api/veiculos` (sem identificador):

```json
{
  "placa": "ABC1D23",
  "marca": "Chevrolet",
  "modelo": "Onix Sedan",
  "anoFabricacao": 2023,
  "quantidadePortas": 4,
  "quilometragem": 45200.00,
  "situacao": "Em_uso"
}
```

O frontend deve ler `idVeiculo`, `anoFabricacao`, `quantidadePortas` e `quilometragem`, e enviar `situacao` como um dos valores aceitos pelo enum. A apresentação ao usuário pode traduzir `Em_uso` para “Em uso”, mas essa tradução deve ficar na camada visual.

### Padrões para todos os recursos

- Identificadores: conservar o nome real do recurso, como `idFuncionario`, `idManutencao` e `idObrigacao`.
- Datas: usar o formato ISO `AAAA-MM-DD`.
- Valores monetários: números decimais JSON; não enviar moeda formatada como texto.
- Relações: enviar objetos mínimos com o identificador esperado pela API ou, preferencialmente, criar DTOs de entrada que recebam `veiculoId`/`funcionarioId`. Documentar e aplicar a mesma opção em todos os recursos.
- Enums: documentar os valores permitidos e validar entradas inválidas com resposta `400` legível.
- Erros: padronizar uma resposta JSON com status HTTP, mensagem e detalhes de validação. A interface deve exibir o erro sem substituir dados locais silenciosamente.

## Plano de correção — ordem de execução

### Etapa 1 — estabilizar execução e banco

1. Documentar requisitos de Java, MySQL, criação do banco e variáveis `DB_USER`/`DB_PASSWORD`.
2. Adicionar script SQL versionado ou configurar migrações para criar e atualizar as tabelas.
3. Manter credenciais fora do repositório e não usar senha vazia fora do ambiente local controlado.
4. Registrar como iniciar backend e frontend e qual origem/porta cada um usa.

**Aceite:** outro integrante consegue configurar o banco e iniciar a aplicação seguindo apenas as instruções do projeto.

### Etapa 2 — definir e aplicar os contratos JSON

1. Criar DTOs para entrada e saída da API; não expor entidades JPA diretamente.
2. Definir nomes de campos, identificadores, relações, enums, datas e formatos numéricos para cada recurso.
3. Atualizar a interface para usar exatamente esses nomes e valores.
4. Validar entradas no backend e retornar `400` com indicação do campo inválido.

**Aceite:** os exemplos de JSON deste documento e os contratos dos demais recursos são aceitos pela API; respostas têm todos os campos esperados pelo frontend.

### Etapa 3 — implementar autenticação segura de ponta a ponta

1. Escolher e documentar um mecanismo de autenticação para a aplicação. O formulário atual pressupõe um endpoint de login; o backend precisa implementá-lo ou a interface deve ser alterada para o mecanismo efetivamente adotado.
2. Armazenar somente hash de senha usando `PasswordEncoder`; nunca retornar senha ou hash em endpoints.
3. Aplicar autenticação e autorização às rotas conforme o papel do usuário. Restringir cadastro público de funcionários e operações administrativas.
4. Fazer a interface tratar `401` (não autenticado) e `403` (sem permissão) sem recorrer a login local como se fosse autenticação real.
5. Remover fallback de senha em `localStorage`. O `localStorage` não deve ser fonte de identidade nem de autorização.

**Aceite:** cadastro, login, sessão/token e logout funcionam pelo backend; chamadas sem autenticação recebem `401`; respostas não contêm senha nem hash.

### Etapa 4 — alinhar CORS e endereços

1. Substituir `http://localhost:8080` fixo por configuração adequada ao ambiente.
2. Permitir CORS somente para as origens reais do frontend e os métodos/headers necessários.
3. Confirmar que a configuração de segurança permite preflight `OPTIONS` quando necessário.

**Aceite:** o navegador completa chamadas da origem configurada sem erro de CORS; origens não autorizadas não recebem permissão ampla.

### Etapa 5 — corrigir sincronização e tratamento de erros

1. Centralizar chamadas em um cliente HTTP que inclua autenticação, `Content-Type`, tratamento de status e parsing de erros.
2. Atualizar a tela e o cache somente após resposta de sucesso do servidor.
3. Em falhas, manter o estado anterior e informar o usuário; não simular gravação ou exclusão local.
4. Se um modo offline for requisito, defini-lo explicitamente com fila/sincronização e indicador visual de pendência. Não misturá-lo silenciosamente com a API.

**Aceite:** `400`, `401`, `403`, `404` e `500` não produzem falso sucesso nem divergência silenciosa entre tela e banco.

### Etapa 6 — proteger a renderização da interface

1. Inserir valores textuais com `textContent` e construir elementos DOM; se templates HTML forem mantidos, escapar todo conteúdo variável.
2. Evitar colocar dados recebidos da API diretamente em atributos `onclick`; associar eventos por código e passar identificadores validados.

**Aceite:** entradas como nome, descrição ou modelo contendo caracteres `<`, `>` e aspas são exibidas como texto e não executam marcação/script.

## Roteiro de verificação ponta a ponta

Executar em ambiente de desenvolvimento com banco configurado e registrar os resultados:

1. Iniciar o MySQL e confirmar que o backend sobe sem erro de conexão ou esquema.
2. Sem autenticação, chamar uma rota protegida e verificar `401`.
3. Criar um usuário pelo fluxo autorizado e confirmar no banco que a senha está armazenada como hash.
4. Fazer login pela interface e confirmar que uma chamada autenticada a `GET /api/veiculos` retorna JSON válido.
5. Criar veículo pela interface; confirmar resposta `201`, identificador gerado e registro persistido no banco.
6. Recarregar a página e confirmar que o veículo aparece a partir da API, sem depender dos dados de exemplo do navegador.
7. Editar o veículo e confirmar `PUT`, persistência e atualização da tela.
8. Tentar enviar enum ou campo inválido e confirmar `400` com erro apresentado ao usuário, sem alteração local enganosa.
9. Excluir o veículo e confirmar `204` (ou o status documentado), remoção no banco e atualização da interface.
10. Verificar `GET /api/funcionarios` e confirmar que nenhum campo de senha/hash é retornado.
11. Repetir os fluxos principais para funcionários, alocações, manutenções, obrigações e documentos, conforme os recursos usados na tela.
12. Executar os mesmos passos a partir da origem oficial do frontend e conferir o console/rede do navegador para erros HTTP, CORS e JavaScript.

## Critério de conclusão do trabalho

A integração pode ser apresentada como concluída quando os critérios de aceite das etapas aplicáveis forem atendidos, os fluxos CRUD usados pela interface estiverem persistindo no banco, a autenticação não depender de dados locais e os resultados do roteiro ponta a ponta estiverem registrados. A equipe deve atualizar este documento com responsáveis, data de validação e evidências (por exemplo, capturas de respostas HTTP sem dados pessoais ou senhas).

## Arquivos de referência analisados

- `src/main/java/com/esf/frota/config/SecurityConfig.java`
- `src/main/java/com/esf/frota/controller/`
- `src/main/java/com/esf/frota/model/`
- `src/main/java/com/esf/frota/service/`
- `src/main/resources/application.properties`
- `src/main/resources/static/index.html`
- `Dockerfile` e `compose.yaml`
- `README.md`

## Atualização da implementação — 12/11/2026

Foram aplicadas alterações para cobrir os problemas listados: a interface agora é servida em `/` pelo Spring Boot; login e cadastro têm endpoints; as rotas de negócio usam HTTP Basic; as senhas novas são armazenadas com BCrypt e omitidas das respostas; contas legadas com senha em texto puro migram para BCrypt no primeiro login válido; a tela usa os nomes/enums do backend e não grava alterações falsas no navegador; foram adicionados esquema SQL, CORS restrito a origens locais e escaping de dados exibidos.

**Verificação ponta a ponta — 12/11/2026:** `./mvnw test` passou conectado a um MySQL 8.0.46 temporário em Docker. O teste de API confirmou página inicial `200`, recurso protegido sem autenticação `401`, cadastro/login `201/200`, omissão do campo `senha` em `GET /api/funcionarios` e CRUD de veículos (`201/200/204`), incluindo valores de enum. No Chromium, o cadastro e login pela tela, criação/listagem de veículo e pagamento de obrigação foram confirmados com os dados persistidos no MySQL. Uma entrada contendo marcação HTML foi exibida como texto, sem execução/injeção.

Também foi criado o ambiente reprodutível em `Dockerfile` e `compose.yaml`. O build da imagem Docker terminou com sucesso; o app e o MySQL estão atualmente ativos em Compose. O acesso local de teste é `http://localhost:8080/`; crie uma conta pela aba **Criar Conta**. O banco fica publicado em `127.0.0.1:3307`. Use `DB_PASSWORD='sua-senha-local' docker compose down` para parar os serviços sem apagar o volume.

Durante o teste de execução, foi encontrada e corrigida uma falha adicional: a segurança bloqueava a própria página de entrada com `401`. Agora somente `/` e `/index.html`, além das rotas de cadastro/login, são públicas; a API de negócio continua exigindo autenticação.
