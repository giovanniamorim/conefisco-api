package org.conefisco.resource.security;


import org.conefisco.model.Usuario;
import org.conefisco.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private JavaMailSender mailSender;

    private static Logger LOGGER = Logger.getLogger("InfoLogging");

    @PostMapping
    public ResponseEntity<?> processForgotPassword(@RequestBody Map<String, String> request) throws MessagingException, UnsupportedEncodingException {
        String email = request.get("email");
        Usuario user = userService.findUserByEmail(email);
        LOGGER.info("Chegou no metodo processForgotPassword e verificou o email: " + user);

        if (user == null) {
            return ResponseEntity.badRequest().body("Nenhum usuário encontrado para o email solicitado.");
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user, token);
        LOGGER.info("criou o token: " + token);
        String resetPasswordUrl = "https://www.conefisco.app.br/#/reset-password?token=" + token;

        LOGGER.info("criou a url: " + resetPasswordUrl);
        LOGGER.info("enviou mensagem para o email: " + user.getEmail());

        // Send the reset password email to the user
        return sendResetPasswordEmail(user.getEmail(), resetPasswordUrl);
    }

    @GetMapping("get-reset")
    private ResponseEntity<?> sendResetPasswordEmail(String email, String resetPasswordUrl) throws MessagingException, UnsupportedEncodingException {

        String mensagem = "Olá, <br><br>Recebemos uma solicitação para resetar sua senha no Portal Transparência. " +
                "Para continuar o processo, clique no link abaixo e siga as instruções na página:<br><br>" +
                resetPasswordUrl +
                "<br><br>Se você não solicitou a mudança de senha, por favor, " +
                "entre em contato com o conefisco para mais informações.<br>" +
                "<br>" +
                "Atenciosamente,<br>" +
                "A equipe conefisco-PA<br>"
                ;

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.setTo(email);
            helper.setSubject("Solicitação de Resete de Senha");
            helper.setText(mensagem, true);
            mailSender.send(message);
            return ResponseEntity.status(HttpStatus.OK).body("Email enviado com sucesso!") ;
        } catch (MailException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro tipo: MailException: " + e);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro tipo: MessagingException: "  + e);
        }
    }


}
