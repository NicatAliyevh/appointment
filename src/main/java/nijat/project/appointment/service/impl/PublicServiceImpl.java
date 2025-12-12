package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.PublicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import static nijat.project.appointment.utils.common.EnumUtils.formatRole;

@Service
@RequiredArgsConstructor
public class PublicServiceImpl implements PublicService {
    private final UserRepository userRepository;

    @Override
    public SuccessResponseDto<Page<UserResponseDto>> findAllUsers(UserRole userRole, Pageable pageable) {
        Page<UserEntity> users = userRepository.findAllByUserRole(userRole, pageable);
        return SuccessResponseDto.of(users.map(this::mapUserToDto), "Doctors retrieved successfully");
    }
    public UserResponseDto mapUserToDto(UserEntity userEntity) {
        String userRole = formatRole(userEntity.getUserRole());
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userRole)
                .build();
    }
}
