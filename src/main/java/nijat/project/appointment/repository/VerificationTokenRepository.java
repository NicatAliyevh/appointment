package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.VerificationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface VerificationTokenRepository extends JpaRepository<VerificationTokenEntity, UUID> {
    Optional<VerificationTokenEntity> findByEmail(String email);
}
