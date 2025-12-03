package nijat.project.appointment.model.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.model.enums.VerificationProgress;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "verification_tokens")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerificationTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @UuidGenerator
    UUID id;
    @Column(name = "token", nullable = false)
    String token;
    @Column(name = "username", nullable = false)
    String username;
    @Column(name = "email", nullable = false, unique = true)
    String email;
    @Column(name = "password",  nullable = false)
    String password;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    UserRole userRole =  UserRole.PATIENT;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    VerificationProgress progress = VerificationProgress.PENDING;
    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;
}
