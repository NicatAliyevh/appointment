package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
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
    public SuccessResponseDto<List<AppointmentResponseDto>> getAppointments(String UUID) {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        List<AppointmentResponseDto> appointmentResponseDtos = appointments.stream().map(this::mapToDto).toList();
        return SuccessResponseDto.of(appointmentResponseDtos, "Appointments retrieved successfully");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> createAppointment(AppointmentRequestDto appointmentRequestDto, String UUID) {
        UserEntity doctor = userRepository.findByIdAndUserRole(appointmentRequestDto.getDoctorId(), UserRole.DOCTOR).orElseThrow(
                () -> new ResourceNotFoundException("Doctor with this id: " + appointmentRequestDto.getDoctorId() + " not found"));
        UserEntity patient = userRepository.findByIdAndUserRole(appointmentRequestDto.getPatientId(), UserRole.PATIENT).orElseThrow(
                () -> new ResourceNotFoundException("Patient with this id: " + appointmentRequestDto.getPatientId() + " not found"));

        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .doctor(doctor)
                .patient(patient)
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .build();
        appointmentRepository.save(appointmentEntity);
        return SuccessResponseDto.of(mapToDto(appointmentEntity), "Appointment created successfully");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId, String UUID) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Appointment with id: " + appointmentId + " not found")
        );
        return SuccessResponseDto.of(mapToDto(appointmentEntity), "Appointment retrieved successfully");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId, AppointmentRequestDto appointmentRequestDto, String UUID) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appointment with id: " + id + " not found"));

        appointmentEntity.getDoctor().setId(appointmentRequestDto.getDoctorId());
        appointmentEntity.getPatient().setId(appointmentRequestDto.getPatientId());
        appointmentEntity.setAppointmentDate(appointmentRequestDto.getAppointmentDate());
        appointmentEntity.setAppointmentTime(appointmentRequestDto.getAppointmentTime());
        appointmentRepository.save(appointmentEntity);

        AppointmentResponseDto appointmentResponseDto = AppointmentResponseDto.builder()
                .id(id)
                .patientId(appointmentRequestDto.getPatientId())
                .doctorId(appointmentRequestDto.getDoctorId())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
        return SuccessResponseDto.of(appointmentResponseDto, "Appointment updated successfully");
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
