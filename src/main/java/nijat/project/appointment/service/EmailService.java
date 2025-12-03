package nijat.project.appointment.service;

public interface EmailService {
    void sendVerificationCode(String to, String name, String code);
}
