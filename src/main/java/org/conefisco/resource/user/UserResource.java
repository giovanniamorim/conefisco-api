package org.conefisco.resource.user;

import org.conefisco.model.ChangePasswordRequest;
import org.conefisco.model.Usuario;
import org.conefisco.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.conefisco.security.AppUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;
import java.util.logging.Logger;

import static java.lang.Void.TYPE;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UserResource {

	@Autowired
	private final UsuarioRepository userRepository;
	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	@Autowired
	private final AppUserDetailsService appUserDetailsService;
	private static Logger LOGGER = Logger.getLogger("InfoLogging");

	@GetMapping()
	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
	public Page<Usuario> listAllUsers(@PageableDefault(size = 5, sort = "codigo", direction = Sort.Direction.DESC) Pageable pageable){
		return userRepository.findAll(pageable);
	}

	@PostMapping
	@ResponseStatus(CREATED)
	@PreAuthorize("hasAnyAuthority('ROLE_CREATE') and #oauth2.hasScope('write')")
	public ResponseEntity<?> addUsuario(@RequestBody @Valid Usuario user)  {
		if(!user.getSenha().equals(user.getConfirmarSenha())){
			return ResponseEntity.badRequest().body("As senhas não correspondem");
		}
		user.setSenha(encoder.encode(user.getSenha()));
		user.setConfirmarSenha(encoder.encode(user.getConfirmarSenha()));
		return ResponseEntity.ok(userRepository.save(user));
	}


//	@PutMapping("/{codigo}")
//	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
//	public ResponseEntity<?> editUser(@PathVariable Long codigo, @Valid @RequestBody Usuario userUpdated){
//
//		return userRepository.findById(codigo)
//				.map(usuario -> {
//					usuario.setNome(userUpdated.getNome());
//					usuario.setEmail(userUpdated.getEmail());
//					usuario.setCelular(userUpdated.getCelular());
//					usuario.setCpf(userUpdated.getCpf());
//					usuario.setRg(userUpdated.getRg());
//					usuario.setRgOrgaoExp(userUpdated.getRgOrgaoExp());
//					usuario.setMatricula(userUpdated.getMatricula());
//					usuario.setSituacao(userUpdated.getSituacao());
//
//					LOGGER.info("Valor do userUpdate getSenha:" + userUpdated.getSenha());
//					LOGGER.info("Valor do usuario getSenha:" + usuario.getSenha());
//
//					if(userUpdated.getSenha().isEmpty() || userUpdated.getSenha().isBlank()){
//						LOGGER.info("userUpdated Está vazio?:" + userUpdated.getSenha().isEmpty());
//						LOGGER.info("userUpdated Está em branco?:" + userUpdated.getSenha().isBlank());
//						usuario.setSenha(usuario.getSenha());
//						usuario.setConfirmarSenha(usuario.getConfirmarSenha());
//					}
//					if(userUpdated.getSenha().equals(usuario.getSenha())){
//						LOGGER.info("uma é igual a outra?" + userUpdated.getSenha().equals(usuario.getSenha()));
//						usuario.setSenha(usuario.getSenha());
//					}
//
//					if(!userUpdated.getSenha().isEmpty() || !userUpdated.getSenha().isBlank()){
//						usuario.setSenha(encoder.encode(userUpdated.getSenha()));
//						usuario.setConfirmarSenha(encoder.encode(userUpdated.getConfirmarSenha()));
//						LOGGER.info("Caiu terceito if" + usuario);
//					}
//
//					Usuario putUsuario = userRepository.save(usuario);
//
//					return ResponseEntity.ok().body(putUsuario);
//				}).orElseThrow(() -> new ResponseStatusException(
//						NOT_FOUND, "Usuário não encontrado"));
//	}


	@PutMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
	public ResponseEntity<?> editUser(@PathVariable Long codigo, @Valid @RequestBody Usuario userUpdated) {
		return userRepository.findById(codigo)
				.map(usuario -> {
					usuario.setNome(userUpdated.getNome());
					usuario.setEmail(userUpdated.getEmail());
					usuario.setCelular(userUpdated.getCelular());
					usuario.setCpf(userUpdated.getCpf());
					usuario.setRg(userUpdated.getRg());
					usuario.setRgOrgaoExp(userUpdated.getRgOrgaoExp());
					usuario.setMatricula(userUpdated.getMatricula());
					usuario.setSituacao(userUpdated.getSituacao());

					LOGGER.info("Valor do userUpdated getSenha: {}" + userUpdated.getSenha());
					LOGGER.info("Valor do usuario getSenha: {}" + usuario.getSenha());

					if (!userUpdated.getSenha().isEmpty() && !userUpdated.getSenha().isBlank()) {
						String encodedPassword = encoder.encode(userUpdated.getSenha());
						usuario.setSenha(encodedPassword);
						usuario.setConfirmarSenha(encodedPassword);
						LOGGER.info("Password updated");
					}

					Usuario putUsuario = userRepository.save(usuario);
					return ResponseEntity.ok().body(putUsuario);
				})
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Usuário não encontrado"));
	}

	@GetMapping("/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
	public Usuario findUserById(@PathVariable Long codigo) {
		return userRepository.findById(codigo)
				.orElseThrow(() -> new ResponseStatusException(
						NOT_FOUND, "Usuário não encontrado"));
	}

	@GetMapping("/perfil")
	@ResponseBody
	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
	public Optional<Usuario> findByEmail(@RequestParam("email") String email) {
		Optional<Usuario> usuario = userRepository.findByEmail(email);

		return usuario;
	}

	@DeleteMapping("/{codigo}")
	@ResponseStatus(NO_CONTENT)
	@PreAuthorize("hasAuthority('ROLE_DELETE') and #oauth2.hasScope('write')")
	public void deleteUser(@PathVariable Long codigo) {
		userRepository.findById(codigo).map(user -> {
			userRepository.delete(user);
			return TYPE;
		}).orElseThrow(() -> new ResponseStatusException(
				NOT_FOUND, "Usuário não encontrado"));
	}

	@PutMapping("/change/{codigo}")
	@PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
	public ResponseEntity<?> changePassword(@PathVariable Long codigo, @Valid @RequestBody ChangePasswordRequest request){

		Usuario userCheck = userRepository.findById(codigo)
				.orElseThrow(() -> new ResponseStatusException(
						NOT_FOUND, "Usuário não encontrado para o ID: " + codigo));
		LOGGER.info("userCheck: " + userCheck);

		// Check if old password matches
		if (!encoder.matches(request.getOldPassword(), userCheck.getSenha())) {
			LOGGER.info("Verificou senha antiga");
			return ResponseEntity.badRequest().body("Senha antiga não confere");
		}
		// Check if new password and confirmation match
		if (!request.getNewPassword().equals(request.getConfirmPassword())) {
			LOGGER.info("Confirmação de senha verificada");
			return ResponseEntity.badRequest().body("Erro ao confirmar a senha nova");
		}

		return userRepository.findById(codigo)
				.map(usuario -> {
					usuario.setSenha(encoder.encode(request.getNewPassword()));

					Usuario putUsuario = userRepository.save(usuario);

					return ResponseEntity.ok().body(putUsuario);
				}).orElseThrow(() -> new ResponseStatusException(
						NOT_FOUND, "Usuário não encontrado"));
	}






}
