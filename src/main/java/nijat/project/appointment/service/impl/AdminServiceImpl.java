package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.AdminUserResponseDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.AdminService;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public SuccessResponseDto<List<AdminUserResponseDto>> findAllByUserRole(UserRole userRole) {
        List<UserEntity> users = userRepository.findAllByUserRole(userRole);
        String userRoleString = userRole.toString().charAt(0) + userRole.toString().substring(1).toLowerCase();
        String message = userRoleString + "s retrieved successfully";
        return SuccessResponseDto.of(users.stream().map(this::mapUserToDto).toList(), message);
    }

    @Override
    public SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointments() {
        List<AppointmentEntity> appointmentEntities = appointmentRepository.findAll();
        List<AppointmentResponseDto> appointmentResponseDtos = appointmentEntities.stream()
                .map(this::mapAppointmentToDto).toList();
        return SuccessResponseDto.of(appointmentResponseDtos, "Appointments retrieved successfully");
    }

    public AdminUserResponseDto mapUserToDto(UserEntity userEntity) {
        String userRole = userEntity.getUserRole().toString().charAt(0) +
                userEntity.getUserRole().toString().substring(1).toLowerCase();
        return AdminUserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userRole)
                .build();
    }

    public AppointmentResponseDto mapAppointmentToDto(AppointmentEntity appointmentEntity) {
        return AppointmentResponseDto.builder()
                .id(appointmentEntity.getId())
                .patientId(appointmentEntity.getPatient().getId())
                .doctorId(appointmentEntity.getDoctor().getId())
                .appointmentDate(appointmentEntity.getAppointmentDate())
                .appointmentTime(appointmentEntity.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
    }
}
