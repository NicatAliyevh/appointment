package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.AppointmentUpdateRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final UserRepository userRepository;

    @Override
    public SuccessResponseDto<List<AppointmentResponseDto>> getAppointments(String userId) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("User with this id: " + id + " not found")
        );

        List<AppointmentEntity> appointments;
        if(user.getUserRole().equals(UserRole.PATIENT)){
             appointments = appointmentRepository.findAllByPatientId(id);
             if(appointments.isEmpty()){
                 throw new ResourceNotFoundException("Patient with this id: " + id + " has no appointments");
             }
        }
        else {
            appointments = appointmentRepository.findAllByDoctorId(id);
            if(appointments.isEmpty()){
                throw new ResourceNotFoundException("Doctor with this id: " + id + " has no appointments");
            }
        }

        List<AppointmentResponseDto> appointmentResponseDtos = appointments.stream().map(this::mapToDto).toList();
        return SuccessResponseDto.of(appointmentResponseDtos, "Appointments retrieved successfully");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId,
                                                                         String userId) {
        AppointmentEntity appointment = getUserAppointment(appointmentId, userId);

        return SuccessResponseDto.of(mapToDto(appointment), "Appointment retrieved successfully");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId,
                                                                        AppointmentUpdateRequestDto appointmentUpdateRequestDto,
                                                                        String userId) {
        AppointmentEntity appointment = getUserAppointment(appointmentId, userId);

        appointment.setAppointmentDate(appointmentUpdateRequestDto.getAppointmentDate());
        appointment.setAppointmentTime(appointmentUpdateRequestDto.getAppointmentTime());
        appointmentRepository.save(appointment);

        AppointmentResponseDto appointmentResponseDto = AppointmentResponseDto.builder()
                .id(appointment.getId())
                .patientId(appointment.getPatient().getId())
                .doctorId(appointment.getDoctor().getId())
                .appointmentDate(appointmentUpdateRequestDto.getAppointmentDate())
                .appointmentTime(appointmentUpdateRequestDto.getAppointmentTime())
                .status(appointment.getStatus())
                .build();

        return SuccessResponseDto.of(appointmentResponseDto, "Appointment updated successfully");
    }

    @Override
    public SuccessResponseDto<Void> deleteAppointment(String appointmentId, String userId) {
        AppointmentEntity appointment = getUserAppointment(appointmentId, userId);
        appointmentRepository.delete(appointment);
        return SuccessResponseDto.of("Appointment deleted successfully");
    }

    public AppointmentEntity getUserAppointment(String appointmentId, String userId){
        UUID parsedAppointmentId = parse(appointmentId);
        UUID parsedUserId = parse(userId);

        UserEntity user = userRepository.findById(parsedUserId).orElseThrow(
                () -> new ResourceNotFoundException("User with this id: " + parsedUserId + " not found")
        );

        AppointmentEntity appointment;
        if(user.getUserRole().equals(UserRole.PATIENT)){
            appointment = appointmentRepository.findByPatientIdAndId(parsedUserId, parsedAppointmentId).orElseThrow(
                    () -> new ResourceNotFoundException("Patient and appointment ID do not match any records in the system.")
            );
        }
        else {
            appointment = appointmentRepository.findByDoctorIdAndId(parsedUserId, parsedAppointmentId).orElseThrow(
                    () -> new ResourceNotFoundException("Doctor and appointment ID do not match any records in the system")
            );
        }
        return appointment;
    }

    public AppointmentResponseDto mapToDto(AppointmentEntity appointmentEntity) {
        return AppointmentResponseDto.builder()
                .id(appointmentEntity.getId())
                .doctorId(appointmentEntity.getDoctor().getId())
                .patientId(appointmentEntity.getPatient().getId())
                .appointmentDate(appointmentEntity.getAppointmentDate())
                .appointmentTime(appointmentEntity.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
    }
}
