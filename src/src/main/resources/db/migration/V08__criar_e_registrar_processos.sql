CREATE TABLE jur_processo (
	id BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	numero VARCHAR(50) NOT NULL,
	exequente VARCHAR(50) NOT NULL,
	executado VARCHAR(50) NOT NULL,
	juizo VARCHAR(50) NOT NULL,
	juiz VARCHAR(50) NOT NULL,
	assunto VARCHAR(50) NOT NULL,
	valor DECIMAL(19,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO jur_processo (id, numero, exequente, executado, juizo, juiz, assunto, valor )
    values (1, '0049460-71.2014.814.0301', 'ESTADO DO PARÁ', 'CERPA CERVEJARIA PARAENSE S/A', '3ª Vara de Execução Fiscal', 'ANA PATRICIA NUNES ALVES FERNANDES', 'Dívida Ativa  DIREITO TRIBUTÁRIO', 769221573);
