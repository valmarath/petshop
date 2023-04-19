DROP TABLE IF EXISTS usuarios;
create table usuarios(
    id VARCHAR(256) NOT NULL,
    cpf VARCHAR(16),
    password VARCHAR(256),
    role VARCHAR(256),
    nome VARCHAR(256),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS clientes;
create table clientes(
    id VARCHAR(36) NOT NULL,
    nome VARCHAR(256),
    cpf VARCHAR(16),
    data_cadastro DATE,
    PRIMARY KEY (id)

);

DROP TABLE IF EXISTS contatos;
create table contatos(
    id VARCHAR(36) NOT NULL,
    cliente VARCHAR(36) NOT NULL,
    tag VARCHAR(256),
    tipo VARCHAR(256),
    valor VARCHAR(256),
    PRIMARY KEY (id, cliente)
);

DROP TABLE IF EXISTS enderecos;
create table enderecos(
    id VARCHAR(36) NOT NULL,
    cliente VARCHAR(36) NOT NULL,
    logradouro VARCHAR(256),
    cidade VARCHAR(256),
    bairro VARCHAR(256),
    complemento VARCHAR(256),
    tag VARCHAR(256),
    PRIMARY KEY (id, cliente)
);

DROP TABLE IF EXISTS pets;
create table pets(
    id VARCHAR(36) NOT NULL,
    cliente VARCHAR(36) NOT NULL,
    raca VARCHAR(256),
    data_nascimento DATE,
    nome VARCHAR(256),
    PRIMARY KEY (id, cliente)
);

DROP TABLE IF EXISTS atendimentos;
create table atendimentos(
    id VARCHAR(36) NOT NULL,
    pet VARCHAR(36) NOT NULL,
    descricao VARCHAR(256),
    valor DECIMAL(10,2),
    data_atendimento DATE,
    PRIMARY KEY (id, pet)
);

DROP TABLE IF EXISTS racas;
create table racas(
    id VARCHAR(36) NOT NULL,
    descricao VARCHAR(256),
    PRIMARY KEY (id)
);