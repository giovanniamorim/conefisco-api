package org.conefisco;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.conefisco.service.EnviaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EnviaEmailServiceTest {


    @Autowired
    private EnviaEmailService emailService;

    @Test
    public void testEmailService(){
        emailService.enviar(
                "giovanni.amorim",
                "Título de Teste",
                "Este é o conteúdo"
        );

    }

}
