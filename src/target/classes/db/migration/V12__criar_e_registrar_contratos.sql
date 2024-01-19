CREATE TABLE ctb_contrato (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	prestador VARCHAR(150) NOT NULL,
	desc_servico VARCHAR(150) NOT NULL,
	data_inicial DATE,
	data_final DATE,
	obs VARCHAR(150) NOT NULL,
	valor DECIMAL(19,2) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;




INSERT INTO ctb_contrato (id, prestador, desc_servico, data_inicial, data_final, obs, valor, file_url)
    values (1, 'Joana da Silva', 'Programadora', '2017-06-10', '2018-06-10', 'Apenas uma observação de teste', 150.02, 'teste.jpg');
