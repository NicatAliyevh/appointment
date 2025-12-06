package nijat.project.appointment.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nijat.project.appointment.model.enums.AppointmentStatus;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "pending_appointments")
public class PendingAppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    UUID id;
    @Column(name = "doctor_email", nullable = false)
    String doctorEmail;
    @Column(name = "patient_email", nullable = false)
    String patientEmail;
    @Builder.Default
    @Column(name = "status", nullable = false)
    AppointmentStatus status = AppointmentStatus.PENDING;
    @Column(name = "appointment_date", nullable = false)
    LocalDate appointmentDate;
    @Column(name = "appointment_time", nullable = false)
    LocalTime appointmentTime;
}
