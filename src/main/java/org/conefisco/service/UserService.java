package org.conefisco.service;

import org.conefisco.model.PasswordResetToken;
import org.conefisco.model.Usuario;

import java.util.Optional;

public interface UserService {
    Optional<Usuario> findByEmail(String email);

    Usuario findUserByEmail(String email);

    void createPasswordResetTokenForUser(Usuario user, String token);

    PasswordResetToken getPasswordResetToken(String token);

    void deletePasswordResetToken(PasswordResetToken passwordResetToken);

    void updatePassword(Usuario user, String password);
}
