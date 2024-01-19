package org.conefisco.resource.security;

import org.conefisco.model.PasswordResetToken;
import org.conefisco.model.Usuario;
import org.conefisco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @PostMapping
    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> request) {

        try {
            String token = request.get("token");
            String password = request.get("password");
            PasswordResetToken passwordResetToken = userService.getPasswordResetToken(token);

            if(passwordResetToken == null){
                return ResponseEntity.badRequest().body("Token Inválido");
            }
            if(passwordResetToken.isExpired()){
                return ResponseEntity.badRequest().body("O token está expirado");
            }
            Usuario user = passwordResetToken.getUser();
            userService.updatePassword(user, encoder.encode(password));

            // Delete the password reset token
            userService.deletePasswordResetToken(passwordResetToken);

            return ResponseEntity.ok("Sua senha foi resetada com sucesso. Você será redirecionado para o login.");

        } catch(Exception e){
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body("Ocorreu um erro interno");
        }










    }
}
