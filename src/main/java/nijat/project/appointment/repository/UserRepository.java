package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByIdAndUserRole(UUID id, UserRole role);
    Page<UserEntity> findAllByUserRole(UserRole userRole, Pageable pageable);

}
