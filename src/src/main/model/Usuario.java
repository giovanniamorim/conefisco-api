package org.conefisco.model;

import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.util.List;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "usuario")
public class Usuario {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long codigo;
	private String nome;
	private String email;
	private String senha;
	private String confirmarSenha;
	private String celular;
	@NotNull(message = "O campo CPF é obrigatório")
	@CPF(message = "CPF invalido")
	private String cpf;
	private String rg;
	private String rgOrgaoExp;
	private String matricula;
	private String situacao;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "usuario_permissao", joinColumns = @JoinColumn(name = "codigo_usuario"),
										   inverseJoinColumns = @JoinColumn(name = "codigo_permissao"))
	private List<Permissao> permissoes;

}
