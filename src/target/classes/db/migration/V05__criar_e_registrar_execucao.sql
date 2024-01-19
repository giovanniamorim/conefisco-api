CREATE TABLE orc_execucao (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	ano INT(4) NOT NULL,
	mes VARCHAR(30) NOT NULL,
	descricao VARCHAR(100) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (1, 2022, 'Janeiro', 'Execução Orçamentário 2022', 'teste.pdf');
INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (2, 2023, 'Fevereiro', 'Execução Orçamentário 2023', 'teste.pdf');
INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (3, 2024, 'Março', 'Execução Orçamentário 2024', 'teste.pdf');
INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (4, 2025, 'Abril', 'Execução Orçamentário 2025', 'teste.pdf');
INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (5, 2026, 'Maio', 'Execução Orçamentário 2026', 'teste.pdf');
INSERT INTO orc_execucao (id, ano, mes, descricao, file_url) values (6, 2027, 'Junho', 'Execução Orçamentário 2027', 'teste.pdf');


