CREATE TABLE orc_planejamento (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	ano INT(4) NOT NULL,
	descricao VARCHAR(100) NOT NULL,
	file_url VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (1, 2022, 'Planejamento Orçamentário 2022', 'teste.pdf');
INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (2, 2023, 'Planejamento Orçamentário 2023', 'teste.pdf');
INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (3, 2024, 'Planejamento Orçamentário 2024', 'teste.pdf');
INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (4, 2025, 'Planejamento Orçamentário 2025', 'teste.pdf');
INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (5, 2026, 'Planejamento Orçamentário 2026', 'teste.pdf');
INSERT INTO orc_planejamento (id, ano, descricao, file_url) values (6, 2027, 'Planejamento Orçamentário 2027', 'teste.pdf');


