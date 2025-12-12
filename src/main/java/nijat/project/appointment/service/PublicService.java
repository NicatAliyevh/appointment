package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface PublicService {
    SuccessResponseDto<Page<UserResponseDto>> findAllUsers(UserRole userRole, Pageable pageable);
}
