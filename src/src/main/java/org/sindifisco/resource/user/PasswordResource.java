package org.conefisco.resource.user;

import lombok.RequiredArgsConstructor;
import org.conefisco.model.ChangePasswordRequest;
import org.conefisco.model.Usuario;
import org.conefisco.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

import java.util.logging.Logger;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResource {

    private UsuarioRepository userRepository;
    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PutMapping("/change/{codigo}")
    @PreAuthorize("hasAuthority('ROLE_READ') and #oauth2.hasScope('read')")
    public ResponseEntity<?>  changePassword(@PathVariable Long codigo, @RequestBody @Valid ChangePasswordRequest request) {
        LOGGER.info("Código recebido: " + codigo);
        LOGGER.info("Request recebido: " + request);

        Usuario user = userRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        NOT_FOUND, "Usuário não encontrado para o ID: " + codigo));
            LOGGER.info("Código recebido: " + userRepository.findById(codigo));

        // Check if old password matches
        if (!encoder.matches(request.getOldPassword(), user.getSenha())) {
            LOGGER.info("Verificou senha antiga");
        }
        // Check if new password and confirmation match
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            LOGGER.info("Confirmação de senha verificada");
        }
        // Update user's password
        user.setSenha(new BCryptPasswordEncoder().encode(request.getNewPassword()));
        LOGGER.info("Senha pronta para ser atualizada: " + user.getSenha());

        userRepository.save(user);

        return ResponseEntity.ok().body("Senha atualizada com sucesso!");
    }
}
