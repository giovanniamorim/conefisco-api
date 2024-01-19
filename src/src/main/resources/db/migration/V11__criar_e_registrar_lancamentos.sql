CREATE TABLE ctb_lancamento (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	tipo_lancamento VARCHAR(10) NOT NULL,
	data_lancamento DATE,
	plano_conta VARCHAR(50) NOT NULL,
	valor DECIMAL(19,2) NOT NULL,
	modo_pagamento VARCHAR(50) NOT NULL,
	tipo_comprovante VARCHAR(50) NOT NULL,
	num_doc VARCHAR(30),
	num_cheque VARCHAR(30),
	obs VARCHAR(255) NOT NULL,
	sup_caixa VARCHAR(5) NOT NULL,
	file_url VARCHAR(255),
	ano_exercicio INT(5),
	created TIMESTAMP DEFAULT NULL,
	updated TIMESTAMP DEFAULT NULL

) ENGINE=InnoDB DEFAULT CHARSET=utf8;


--INSERT INTO ctb_lancamento (id, tipo_lancamento, data_lancamento, plano_conta_id, valor, modo_pagamento, tipo_comprovante, obs, sup_caixa, file_url, createDateTime, updateDateTime)
--    values (1, 'Receita', '2017-06-10', 4, 15, 'PIX', 'Nota Fiscal', 'Pagamento de corrida de Uber', 'Sim', 'teste.jpg');
--
--INSERT INTO ctb_lancamento (id, tipo_lancamento, data_lancamento, plano_conta_id, valor, modo_pagamento, tipo_comprovante, obs, sup_caixa, file_url, createDateTime, updateDateTime)
--    values (2, 'Receita', '2017-06-10', 4, 2562.63, 'CHEQUE', 'Nota Fiscal', 'Compra de Mesa para cozinha', 'Sim', 'teste.jpg');
--
