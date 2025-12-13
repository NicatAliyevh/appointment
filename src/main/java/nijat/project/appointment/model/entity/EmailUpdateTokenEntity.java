package nijat.project.appointment.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@Table(name = "email_update_tokens")
public class EmailUpdateTokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    UUID id;
    @Email
    @Column(nullable = false, unique = true)
    String email;
    @Column(nullable = false, unique = true)
    String token;
    @Column(name = "expiry_date", nullable = false)
    LocalDateTime expiryDate;
}
