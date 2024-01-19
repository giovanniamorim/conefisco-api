CREATE TABLE doc_acordo (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_aprovacao DATE,
	descricao VARCHAR(150) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO doc_acordo (id, data_aprovacao, descricao, file_url)
    values (1, '2018-06-10', 'Apenas uma dscricao de teste do acordo', 'teste.pdf');
