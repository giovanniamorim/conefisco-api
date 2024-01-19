CREATE TABLE jur_evento (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(100) NOT NULL,
	data DATE,
	descricao VARCHAR(255) NOT NULL,
	processo_id BIGINT(20) NOT NULL,
	file_url VARCHAR(255) NOT NULL,
    FOREIGN KEY (processo_id) REFERENCES jur_processo(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (1, 'PETIÇÃO PGE', '2017-06-10', 'Execução Fiscal', 'teste.pdf', 1);

INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (2, 'DECISÃO Execução', '2017-06-10', 'DECISÃO Execução', 'teste.pdf', 1);

INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (3, 'CITAÇÃO CERPA', '2017-06-10', 'Acerca da decisão', 'teste.pdf', 1);

INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (4, 'PETIÇÃO PGE', '2017-06-10', 'Em que solicita penhora de faturamento 5% e que fosse nomeada a representante da empresa Helga como depositária. Além disso solicita a reunião de todas as execuções fiscais', 'teste.pdf', 1);

INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (5, 'PETIÇÃO PGE', '2017-06-10', 'Em que solicita penhora de faturamento 5% e que fosse nomeada a representante da empresa Helga como depositária. Além disso solicita a reunião de todas as execuções fiscais', 'teste.pdf', 1);

INSERT INTO jur_evento (id, nome, data, descricao, file_url, processo_id)
    values (6, 'PETIÇÃO PGE', '2017-06-10', 'Em que solicita penhora de faturamento 5% e que fosse nomeada a representante da empresa Helga como depositária. Além disso solicita a reunião de todas as execuções fiscais', 'teste.pdf', 1);

