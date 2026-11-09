-- ESF - EASY SYSTEM FLEET

CREATE DATABASE IF NOT EXISTS esf_frota;
USE esf_frota;

-- 1. TABELA FUNCIONARIO
-- Armazena motoristas e demais funcionarios da empresa.

CREATE TABLE funcionario (
    id_funcionario INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    cpf CHAR(11) NOT NULL UNIQUE,
    cargo VARCHAR(50) NOT NULL,
    telefone VARCHAR(20),
    email VARCHAR(100),
    situacao ENUM('Ativo', 'Inativo') NOT NULL DEFAULT 'Ativo'
);

-- 2. TABELA VEICULO
-- Todos os veiculos cadastrados.

CREATE TABLE veiculo (
    id_veiculo INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(7) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(60) NOT NULL,
    ano_fabricacao YEAR NOT NULL,
    quantidade_portas TINYINT NOT NULL DEFAULT 4,
    quilometragem DECIMAL(10,2) NOT NULL DEFAULT 0,
    situacao ENUM(
        'Disponivel',
        'Em uso',
        'Em manutencao',
        'Inativo'
    ) NOT NULL DEFAULT 'Disponivel',

    CONSTRAINT chk_portas
        CHECK (quantidade_portas IN (2, 4, 5)),

    CONSTRAINT chk_quilometragem
        CHECK (quilometragem >= 0)
);

-- 3. TABELA ALOCACAO
-- Relaciona funcionarios e veiculos.
-- Preserva o historico das vinculacoes.

CREATE TABLE alocacao (
    id_alocacao INT AUTO_INCREMENT PRIMARY KEY,
    id_funcionario INT NOT NULL,
    id_veiculo INT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    situacao ENUM(
        'Ativa',
        'Encerrada'
    ) NOT NULL DEFAULT 'Ativa',

    CONSTRAINT fk_alocacao_funcionario
        FOREIGN KEY (id_funcionario)
        REFERENCES funcionario(id_funcionario),

    CONSTRAINT fk_alocacao_veiculo
        FOREIGN KEY (id_veiculo)
        REFERENCES veiculo(id_veiculo),

    CONSTRAINT chk_periodo_alocacao
        CHECK (data_fim IS NULL OR data_fim >= data_inicio),

    CONSTRAINT chk_situacao_alocacao
        CHECK (
            (situacao = 'Ativa' AND data_fim IS NULL)
            OR
            (situacao = 'Encerrada' AND data_fim IS NOT NULL)
        )
);

-- 4. TABELA DOCUMENTO_VEICULO
-- Armazena os documentos vinculados a cada carro.

CREATE TABLE documento_veiculo (
    id_documento INT AUTO_INCREMENT PRIMARY KEY,
    id_veiculo INT NOT NULL,
    numero_documento VARCHAR(50),
    tipo_documento VARCHAR(50) NOT NULL,
    data_emissao DATE,
    data_validade DATE,
    situacao ENUM(
        'Regular',
        'Pendente',
        'Vencido'
    ) NOT NULL DEFAULT 'Regular',

    CONSTRAINT fk_documento_veiculo
        FOREIGN KEY (id_veiculo)
        REFERENCES veiculo(id_veiculo)
);

-- 5. TABELA OBRIGACAO_VEICULAR
-- Controla impostos, licenciamento e pagamentos.

CREATE TABLE obrigacao_veicular (
    id_obrigacao INT AUTO_INCREMENT PRIMARY KEY,
    id_veiculo INT NOT NULL,
    tipo_obrigacao VARCHAR(50) NOT NULL,
    exercicio YEAR NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    situacao ENUM(
        'Pendente',
        'Pago',
        'Vencido'
    ) NOT NULL DEFAULT 'Pendente',

    CONSTRAINT fk_obrigacao_veiculo
        FOREIGN KEY (id_veiculo)
        REFERENCES veiculo(id_veiculo),

    CONSTRAINT chk_valor_obrigacao
        CHECK (valor >= 0),

    CONSTRAINT chk_pagamento
        CHECK (
            data_pagamento IS NULL
            OR data_pagamento >= '1900-01-01'
        )
);

-- 6. TABELA MANUTENCAO
-- Registra manutencoes preventivas e corretivas.

CREATE TABLE manutencao (
    id_manutencao INT AUTO_INCREMENT PRIMARY KEY,
    id_veiculo INT NOT NULL,
    tipo_manutencao ENUM(
        'Preventiva',
        'Corretiva'
    ) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_manutencao DATE,
    data_prevista DATE,
    quilometragem DECIMAL(10,2),
    valor DECIMAL(10,2) NOT NULL DEFAULT 0,
    situacao ENUM(
        'Programada',
        'Em andamento',
        'Concluida',
        'Cancelada'
    ) NOT NULL DEFAULT 'Programada',

    CONSTRAINT fk_manutencao_veiculo
        FOREIGN KEY (id_veiculo)
        REFERENCES veiculo(id_veiculo),

    CONSTRAINT chk_valor_manutencao
        CHECK (valor >= 0),

    CONSTRAINT chk_km_manutencao
        CHECK (quilometragem IS NULL OR quilometragem >= 0)
);

-- 7. INDICES PARA CONSULTAS FREQUENTES

CREATE INDEX idx_alocacao_funcionario
    ON alocacao(id_funcionario);

CREATE INDEX idx_documento_validade
    ON documento_veiculo(data_validade);

CREATE INDEX idx_obrigacao_vencimento
    ON obrigacao_veicular(data_vencimento, situacao);

CREATE INDEX idx_manutencao_prevista
    ON manutencao(data_prevista, situacao);

-- 8. CONFERENCIA DAS TABELAS CRIADAS

SHOW TABLES;

DESCRIBE funcionario;
DESCRIBE veiculo;
DESCRIBE alocacao;
DESCRIBE documento_veiculo;
DESCRIBE obrigacao_veicular;
DESCRIBE manutencao;

ALTER TABLE veiculo
MODIFY situacao ENUM('Disponivel', 'Em_uso', 'Em_manutencao', 'Inativo')
NOT NULL DEFAULT 'Disponivel';

ALTER TABLE funcionario
ADD COLUMN senha VARCHAR(255) NOT NULL;

  ALTER TABLE manutencao
  MODIFY situacao ENUM('Programada', 'Em_andamento', 'Concluida', 'Cancelada')
  NOT NULL DEFAULT 'Programada';