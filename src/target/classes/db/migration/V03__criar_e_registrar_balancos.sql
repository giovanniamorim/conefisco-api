CREATE TABLE ctb_balanco (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	ano INT(4) NOT NULL,
	mes VARCHAR(30) NOT NULL,
	descricao VARCHAR(100) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (1, 2022, 'Janeiro', 'uma descrição qualquer' ,'teste.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (2, 2022, 'Fevereiro', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (3, 2022, 'Março', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (4, 2022, 'Abril', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (5, 2022, 'Maio', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (6, 2022, 'Junho', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (7, 2022, 'Julho', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (8, 2022, 'Agosto', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (9, 2022, 'Setembro', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (10, 2022, 'Outubro', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (11, 2022, 'Novembro', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (12, 2022, 'Dezembro', 'uma descrição qualquer' ,'teste2.pdf');
INSERT INTO ctb_balanco (id, ano, mes, descricao, file_url) values (13, 2023, 'Janeiro', 'uma descrição qualquer' ,'teste2.pdf');

