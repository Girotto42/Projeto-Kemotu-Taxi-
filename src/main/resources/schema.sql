CREATE TABLE IF NOT EXISTS funcionario (
    id_funcionario BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    cargo VARCHAR(50) NOT NULL,
    telefone VARCHAR(255),
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_funcionario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS veiculo (
    id_veiculo BIGINT NOT NULL AUTO_INCREMENT,
    placa VARCHAR(7) NOT NULL UNIQUE,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(60) NOT NULL,
    ano_fabricacao INT NOT NULL,
    quantidade_portas TINYINT NOT NULL,
    quilometragem DECIMAL(12,2) NOT NULL,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_veiculo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS alocacao (
    id_alocacao BIGINT NOT NULL AUTO_INCREMENT,
    id_funcionario BIGINT NOT NULL,
    id_veiculo BIGINT NOT NULL,
    data_inicio DATE NOT NULL,
    data_fim DATE,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_alocacao),
    CONSTRAINT fk_alocacao_funcionario FOREIGN KEY (id_funcionario) REFERENCES funcionario(id_funcionario),
    CONSTRAINT fk_alocacao_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS documento_veiculo (
    id_documento BIGINT NOT NULL AUTO_INCREMENT,
    id_veiculo BIGINT NOT NULL,
    numero_documento VARCHAR(50),
    tipo_documento VARCHAR(50) NOT NULL,
    data_emissao DATE,
    data_validade DATE,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_documento),
    CONSTRAINT fk_documento_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS manutencao (
    id_manutencao BIGINT NOT NULL AUTO_INCREMENT,
    id_veiculo BIGINT NOT NULL,
    tipo_manutencao VARCHAR(32) NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    data_manutencao DATE,
    data_prevista DATE,
    quilometragem DECIMAL(12,2),
    valor DECIMAL(12,2) NOT NULL,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_manutencao),
    CONSTRAINT fk_manutencao_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS obrigacao_veicular (
    id_obrigacao BIGINT NOT NULL AUTO_INCREMENT,
    id_veiculo BIGINT NOT NULL,
    tipo_obrigacao VARCHAR(50) NOT NULL,
    exercicio INT NOT NULL,
    valor DECIMAL(12,2) NOT NULL,
    data_vencimento DATE NOT NULL,
    data_pagamento DATE,
    situacao VARCHAR(32) NOT NULL,
    PRIMARY KEY (id_obrigacao),
    CONSTRAINT fk_obrigacao_veiculo FOREIGN KEY (id_veiculo) REFERENCES veiculo(id_veiculo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
