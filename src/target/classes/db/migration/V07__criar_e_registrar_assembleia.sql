CREATE TABLE doc_assembleia (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	data DATE,
	tipo VARCHAR(100) NOT NULL,
	assunto VARCHAR(120) NOT NULL,
	comentario VARCHAR(255) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (1, '2017-06-10', 'Ordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2027', 'teste.jpg');

INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (2, '2018-06-10', 'Extraordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2022', 'teste.jpg');

INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (3, '2017-06-10', 'Ordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2027', 'teste.jpg');

INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (4, '2018-06-10', 'Extraordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2022', 'teste.jpg');

INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (5, '2017-06-10', 'Ordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2027', 'teste.jpg');

INSERT INTO doc_assembleia (id, data, tipo, assunto, comentario, file_url)
    values (6, '2018-06-10', 'Extraordinária', 'Prestação de Contas', 'Apreciar e deliberar a prestação de contas do ano de 2022', 'teste.jpg');


