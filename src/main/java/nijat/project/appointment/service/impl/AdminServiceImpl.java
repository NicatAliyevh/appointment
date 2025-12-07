package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.BadRequestException;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.model.entity.PendingAppointmentEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.AppointmentStatus;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.repository.PendingAppointmentRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.AdminService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import static nijat.project.appointment.utils.common.EnumUtils.formatRole;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final PendingAppointmentRepository pendingAppointmentRepository;

    @Override
    public SuccessResponseDto<List<UserResponseDto>> findAllUsers(UserRole userRole) {
        List<UserEntity> users = userRepository.findAllByUserRole(userRole);
        String role = formatRole(userRole);
        String message = role + "s retrieved successfully";
        return SuccessResponseDto.of(users.stream().map(this::mapUserToDto).toList(), message);
    }

    @Override
    public SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointments(AppointmentStatus appointmentStatus) {
        List<AppointmentResponseDto> appointmentResponseDtos = List.of();
        String message = "";
        if(appointmentStatus==AppointmentStatus.SCHEDULED){
            List<AppointmentEntity> appointmentEntities = appointmentRepository.findAll();
            appointmentResponseDtos = appointmentEntities.stream()
                    .map(this::mapAppointmentToDto).toList();
            message = "Appointments retrieved successfully";
        }
        else if(appointmentStatus==AppointmentStatus.PENDING){
            List<PendingAppointmentEntity> pendingAppointmentEntities = pendingAppointmentRepository.findAll();
            appointmentResponseDtos = pendingAppointmentEntities.stream().map(this::mapPendingAppointmentToDto).toList();
            message = "Pending appointments retrieved successfully";
        }

        if(appointmentResponseDtos.isEmpty()){
            throw new ResourceNotFoundException("No appointment found");
        }

        return SuccessResponseDto.of(appointmentResponseDtos, message);
    }

    @Override
    public SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointmentsByUserId(String userId) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with this id: " + id + " not found"));

        List<AppointmentEntity> appointment;
        String role = formatRole(user.getUserRole());

        if(user.getUserRole()==UserRole.DOCTOR){
            appointment = appointmentRepository.findAllByDoctorId(id);
        } else if(user.getUserRole()==UserRole.PATIENT){
            appointment = appointmentRepository.findAllByPatientId(id);
        } else {
            throw new BadRequestException("Invalid user role");
        }

        if(appointment.isEmpty()){
            throw new ResourceNotFoundException(role + " with the given id has no appointments");
        }
        List<AppointmentResponseDto> appointmentResponseDtos = appointment.stream().map(this::mapAppointmentToDto).toList();
        return SuccessResponseDto.of(appointmentResponseDtos, role + " appointments for the given id retrieved successfully");
    }

    @Override
    public SuccessResponseDto<Void> deleteUser(String userId, UserRole userRole) {
        UUID id = parse(userId);
        String role = formatRole(userRole);

        UserEntity user = userRepository.findByIdAndUserRole(id, userRole).orElseThrow(
                () -> new ResourceNotFoundException(role + " with this id: " + id + " not found")
        );

        userRepository.delete(user);
        return SuccessResponseDto.of(role + " deleted successfully");
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
    
    public AppointmentResponseDto mapPendingAppointmentToDto(PendingAppointmentEntity pendingAppointmentEntity) {
        return AppointmentResponseDto.builder()
                .id(pendingAppointmentEntity.getId())
                .patientId(parse(pendingAppointmentEntity.getDoctorId()))
                .doctorId(parse(pendingAppointmentEntity.getPatientId()))
                .appointmentDate(pendingAppointmentEntity.getAppointmentDate())
                .appointmentTime(pendingAppointmentEntity.getAppointmentTime())
                .status(pendingAppointmentEntity.getStatus())
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
