package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.EmailUpdateTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EmailUpdateTokenRepository extends JpaRepository<EmailUpdateTokenEntity, UUID> {
    Optional<EmailUpdateTokenEntity> findByEmail(String email);
}
