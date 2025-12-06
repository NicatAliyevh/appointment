package nijat.project.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;

public interface EmailService {
    void sendVerificationCode(String to, String name, String code);
    void sendPasswordResetLink(String to, String name, String token);
    void sendAppointmentRequest(String to, String doctorName, String patientName,
                                LocalDate appointmentDate, LocalTime appointmentTime);
    void sendAppointmentApproval(String to, String doctorName, String patientName,
                                LocalDate appointmentDate, LocalTime appointmentTime);
}
