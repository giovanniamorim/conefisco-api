CREATE TABLE usuario (
	codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
	nome VARCHAR(50) NOT NULL,
	email VARCHAR(50) NOT NULL,
	senha VARCHAR(150) NOT NULL,
	confirmar_senha(150),
	celular VARCHAR(30),
	cpf VARCHAR(15) NOT NULL,
	rg VARCHAR(30),
	rg_orgao_exp VARCHAR(20),
	matricula VARCHAR(20),
	situacao VARCHAR(20)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE permissao (
	codigo BIGINT(20) PRIMARY KEY,
	descricao VARCHAR(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE usuario_permissao (
	codigo_usuario BIGINT(20) NOT NULL,
	codigo_permissao BIGINT(20) NOT NULL,
	PRIMARY KEY (codigo_usuario, codigo_permissao),
	FOREIGN KEY (codigo_usuario) REFERENCES usuario(codigo),
	FOREIGN KEY (codigo_permissao) REFERENCES permissao(codigo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO usuario (codigo, nome, email, senha,confirmar_senha, celular, cpf, rg, rg_orgao_exp, matricula, situacao) values (
    1, 'Giovanni Amorim',
    'giovanni.amorim.ti@gmail.com',
    '$2a$10$AjIhHcIKm/gbxOFXTTdCMuofMJaJ/n8D7xrGVpHt63JOWo1S1YXr.',
    '$2a$10$AjIhHcIKm/gbxOFXTTdCMuofMJaJ/n8D7xrGVpHt63JOWo1S1YXr.',
    '61981024595',
    '51189178249',
    '4234340',
    'SSP-DF',
    '',
    ''
    );
INSERT INTO usuario (codigo, nome, email, senha, celular, cpf, rg, rg_orgao_exp, matricula, situacao) values (
    2,
    'Maria Teste', 'teste@sindifisco-pa.org.br',
    '$2a$10$AjIhHcIKm/gbxOFXTTdCMuofMJaJ/n8D7xrGVpHt63JOWo1S1YXr.',
    '61981024595',
    '94673443004',
    '4234340',
    'SSP-DF',
    '',
    ''
    );

INSERT INTO permissao (codigo, descricao) values (1, 'ROLE_CREATE');
INSERT INTO permissao (codigo, descricao) values (2, 'ROLE_READ');
INSERT INTO permissao (codigo, descricao) values (3, 'ROLE_UPDATE');
INSERT INTO permissao (codigo, descricao) values (4, 'ROLE_DELETE');


-- admin
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 1);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 2);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 3);
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (1, 4);


-- maria
INSERT INTO usuario_permissao (codigo_usuario, codigo_permissao) values (2, 2);
