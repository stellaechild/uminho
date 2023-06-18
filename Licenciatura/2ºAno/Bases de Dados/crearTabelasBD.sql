/* ModeloLógico: */

CREATE SCHEMA IF NOT EXISTS trabalhobd;
USE trabalhobd;

CREATE TABLE cliente (
    id INT PRIMARY KEY,
    dinheiroGasto DOUBLE,
    numeroContribuinte VARCHAR(20),
    nome VARCHAR(50),
    telemovel VARCHAR(10),
    eMail VARCHAR(30)
);

CREATE TABLE morada (
    cliente INT,
    morada VARCHAR(30) PRIMARY KEY
);

CREATE TABLE entrega (
    id INT PRIMARY KEY,
    data VARCHAR(20),
    preço DOUBLE,
    confirmaEntrega BOOLEAN,
    idCliente INT
);

CREATE TABLE compra (
    id INT PRIMARY KEY,
    preço DOUBLE,
    hora VARCHAR(15),
    dia VARCHAR(20),
    desconto DOUBLE,
    idCliente INT
);

CREATE TABLE fornecedor (
    id INT PRIMARY KEY,
    nomeEmpresa VARCHAR(40)
);

CREATE TABLE contatos (
    fornecedor INT,
    contato VARCHAR(20) PRIMARY KEY
);

CREATE TABLE produto (
    id INT PRIMARY KEY,
    valor DOUBLE,
    prazoValidade DATETIME,
    idCompra INT,
    idEntrega INT,
    idFornecedor INT,
    nome VARCHAR(45),
    stock INT
);

CREATE TABLE tipo (
    produto INT,
    tipo VARCHAR(30),
    idTipo INT PRIMARY KEY
);
 
ALTER TABLE morada ADD CONSTRAINT FK_morada_1
    FOREIGN KEY (clienteId)
    REFERENCES cliente (idCliente);
 
ALTER TABLE entrega ADD CONSTRAINT FK_Entrega_2
    FOREIGN KEY (cliente)
    REFERENCES cliente (idCliente);
 
ALTER TABLE compra ADD CONSTRAINT FK_Compra_2
    FOREIGN KEY (idCliente)
    REFERENCES cliente (idCliente);
 
ALTER TABLE contatos ADD CONSTRAINT FK_Contatos_1
    FOREIGN KEY (fornecedor)
    REFERENCES fornecedor (idFornecedor);
 
ALTER TABLE produto ADD CONSTRAINT FK_Produto_2
    FOREIGN KEY (idCompra)
    REFERENCES compra (idcompra),
    FOREIGN KEY (idEntrega)
    REFERENCES entrega(identrega),
    FOREIGN KEY (idFornecedor)
    REFERENCES fornecedor (idfornecedor);

ALTER TABLE tipo ADD CONSTRAINT FK_Tipo_1
    FOREIGN KEY (produto)
    REFERENCES produto (idproduto);