package nijat.project.appointment.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
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
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name="appointments")
@Builder
public class AppointmentEntity {
    @Id
    @GeneratedValue
    @UuidGenerator
    UUID id;
    @Column(name = "doctor_id", nullable = false)
    UUID doctorId;
    @Column(name = "patient_id", nullable = false)
    UUID patientId;
    @Column(name = "appointment_date", nullable = false)
    LocalDate appointmentDate;
    @Column(name = "appointment_time", nullable = false)
    LocalTime appointmentTime;
    @Builder.Default
    @Column(name = "status", nullable = false)
    AppointmentStatus status = AppointmentStatus.PENDING;
    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    Instant createdAt;
    @Column(name = "updated_at", nullable = false)
    @CreationTimestamp
    Instant updatedAt;
}
