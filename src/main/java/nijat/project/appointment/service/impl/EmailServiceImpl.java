package nijat.project.appointment.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nijat.project.appointment.service.EmailService;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    @Value("${application.frontend.base-url}")
    private String frontendBaseUrl;

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

    public void sendPasswordResetLink(String to, String name, String token){
        try{
            String resetLink = frontendBaseUrl + "/reset-password?token=" + token;

            Context context = new Context();
            context.setVariable("username", name);
            context.setVariable("resetLink", resetLink);

            String htmlContent = templateEngine.process("forgot-password-email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Your Hospital-Management System Forgot Password Link");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            log.error("Error sending forgot password link to {}", to, e);
        }
    }

    @Override
    public void sendAppointmentRequest(String to, String doctorName, String patientName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try{
            Context context = new Context();
            context.setVariable("doctorName", doctorName);
            context.setVariable("patientName", patientName);
            context.setVariable("appointmentDate", appointmentDate);
            context.setVariable("appointmentTime", appointmentTime);


            String htmlContent = templateEngine.process("appointment-request-email", context);
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Hospital-Management System Appointment Request");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            log.error("Error sending email to {}", to, e);
        }
    }

    @Override
    public void sendAppointmentApproval(String to, String doctorName, String patientName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try{
            Context context = new Context();
            context.setVariable("doctorName", doctorName);
            context.setVariable("patientName", patientName);
            context.setVariable("appointmentDate", appointmentDate);
            context.setVariable("appointmentTime", appointmentTime);

            String htmlContent = templateEngine.process("appointment-approval-email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Hospital-Management System Appointment Approval");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            log.error("Error sending email to {}", to, e);
        }
    }

    @Override
    public void sendAppointmentRejection(String to, String doctorName, String patientName, LocalDate appointmentDate, LocalTime appointmentTime) {
        try{
            Context context = new Context();
            context.setVariable("doctorName", doctorName);
            context.setVariable("patientName", patientName);
            context.setVariable("appointmentDate", appointmentDate);
            context.setVariable("appointmentTime", appointmentTime);

            String htmlContent = templateEngine.process("appointment-rejection-email", context);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Hospital-Management System Appointment Rejection");
            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e){
            log.error("Error sending email to {}", to, e);
        }
    }
}
