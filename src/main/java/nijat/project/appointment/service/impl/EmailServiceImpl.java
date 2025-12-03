package nijat.project.appointment.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nijat.project.appointment.service.EmailService;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    @Override

    public void sendVerificationCode(String to, String name, String code) {
        try{
            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("verificationCode", code);

            String htmlContent = templateEngine.process("verification-email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Your Hospital-Management System Verification Code");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            log.error("Error sending verification code to {}", to, e);
        }
    }
}
