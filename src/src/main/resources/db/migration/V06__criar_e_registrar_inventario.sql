CREATE TABLE ctb_inventario (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data_Aquisicao DATE,
	departamento VARCHAR(100) NOT NULL,
	numero VARCHAR(20) NOT NULL,
	quant INT(9) NOT NULL,
	descricao VARCHAR(255) NOT NULL,
	estado_conservacao VARCHAR(30) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (1, '2017-06-10', 'Secretaria', 'SEC001-SINDPA', 1, 'Impressora matricial', 'Novo', 'teste.jpg');

INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (2, '2018-06-10', 'Secretaria', 'SEC002-SINDPA', 1, 'Monitor LCD', 'Bom', 'teste.jpg');

INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (3, '2018-08-10', 'Secretaria', 'SEC003-SINDPA', 1, 'Cabo de Impressora', 'Regular', 'teste.jpg');

INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (4, '2018-06-10', 'Cozinha', 'SEC004-SINDPA', 4, 'Panela de Ferro Tramontina', 'Péssimo', 'teste.jpg');

INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (5, '2018-06-10', 'Secretaria', 'SEC005-SINDPA', 1, 'Computador Notebook Ace', 'Sucata', 'teste.jpg');

INSERT INTO ctb_inventario (id, data_aquisicao, departamento, numero, quant, descricao, estado_conservacao, file_url)
    values (6, '2018-06-10', 'Cozinha', 'SEC006-SINDPA', 4, 'Geladeira', 'Novo', 'teste.jpg');

