CREATE TABLE ctb_plano_conta (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	codigo VARCHAR(255) NOT NULL,
	nome VARCHAR(255) NOT NULL,
	profundidade VARCHAR(30) NOT NULL,
	tipo_lancamento VARCHAR(30) NOT NULL,
	conta_pai_id BIGINT(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE ctb_plano_conta
    ADD FOREIGN KEY (conta_pai_id) REFERENCES ctb_plano_conta(id);


INSERT INTO ctb_plano_conta (id, codigo, nome, profundidade, tipo_lancamento, conta_pai_id)
    values (1, '3', 'Despesas Gerais', 'Sintética', 'Despesa', null);

INSERT INTO ctb_plano_conta (id, codigo, nome, profundidade, tipo_lancamento, conta_pai_id)
    values (2, '3.1', 'Despesas Internas', 'Sintética', 'Despesa', 1);

INSERT INTO ctb_plano_conta (id, codigo, nome, profundidade, tipo_lancamento, conta_pai_id)
    values (3, '3.1.1', 'Despesas com Transportes', 'Sintética', 'Despesa', 2);

INSERT INTO ctb_plano_conta (id, codigo, nome, profundidade, tipo_lancamento, conta_pai_id)
    values (4, '3.1.1.001', 'Recibo Uber', 'Analítica', 'Despesa', 3);

INSERT INTO ctb_plano_conta (id, codigo, nome, profundidade, tipo_lancamento, conta_pai_id)
    values (5, '3.1.1.002', 'Recibo Táxi', 'Analítica', 'Despesa', 3);
