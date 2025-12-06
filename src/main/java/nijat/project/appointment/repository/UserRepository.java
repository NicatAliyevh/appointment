package nijat.project.appointment.repository;

import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByEmailAndUserRole(String email, UserRole userRole);
    Optional<UserEntity> findByIdAndUserRole(UUID id, UserRole role);
    List<UserEntity> findAllByUserRole(UserRole userRole);

}
