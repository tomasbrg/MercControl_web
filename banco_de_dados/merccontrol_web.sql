
CREATE DATABASE IF NOT EXISTS merccontrol_web
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE merccontrol_web;

CREATE TABLE IF NOT EXISTS clientes (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(80) NOT NULL,
  cpf VARCHAR(11) NOT NULL,
  tel VARCHAR(30),
  PRIMARY KEY (id),
  UNIQUE KEY uk_clientes_cpf (cpf)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS produtos (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(80) NOT NULL,
  preco DECIMAL(12,2) NOT NULL,
  qtd INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT chk_produtos_preco CHECK (preco > 0),
  CONSTRAINT chk_produtos_qtd CHECK (qtd >= 0)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS vendas (
  id BIGINT NOT NULL AUTO_INCREMENT,
  data DATETIME(6) NOT NULL,
  cliente VARCHAR(255),
  pagamento VARCHAR(255) NOT NULL,
  total DECIMAL(14,2) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS itens_venda (
  id BIGINT NOT NULL AUTO_INCREMENT,
  venda_id BIGINT NOT NULL,
  produto_id BIGINT NOT NULL,
  nome VARCHAR(255) NOT NULL,
  qtd INT NOT NULL,
  preco DECIMAL(12,2) NOT NULL,
  PRIMARY KEY (id),
  KEY idx_itens_venda_venda (venda_id),
  CONSTRAINT fk_itens_venda_venda FOREIGN KEY (venda_id)
    REFERENCES vendas(id) ON DELETE CASCADE
) ENGINE=InnoDB;
