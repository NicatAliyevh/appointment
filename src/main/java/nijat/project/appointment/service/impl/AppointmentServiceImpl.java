package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;

    @Override
    public SuccessResponseDto<List<AppointmentResponseDto>> getAppointments(String name) {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        List<AppointmentResponseDto> appointmentResponseDtos = appointments.stream().map(this::mapToDto).toList();
        return SuccessResponseDto.of(appointmentResponseDtos, "Appointments retrieved successfully.");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> createAppointment(AppointmentRequestDto appointmentRequestDto, String name) {
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .doctorId(appointmentRequestDto.getDoctorId())
                .patientId(appointmentRequestDto.getPatientId())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .build();
        appointmentRepository.save(appointmentEntity);
        return SuccessResponseDto.of(mapToDto(appointmentEntity), "Appointment created successfully.");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId, String name) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Appointment with id: " + appointmentId + " not found")
        );
        return SuccessResponseDto.of(mapToDto(appointmentEntity), "Appointment retrieved successfully.");
    }

    @Override
    public SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId, AppointmentRequestDto appointmentRequestDto, String name) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appointment with id: " + id + " not found"));

        appointmentEntity.setDoctorId(appointmentRequestDto.getDoctorId());
        appointmentEntity.setPatientId(appointmentRequestDto.getPatientId());
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
        return SuccessResponseDto.of(appointmentResponseDto, "Appointment updated successfully.");
    }

    public AppointmentResponseDto mapToDto(AppointmentEntity appointmentEntity) {
        return AppointmentResponseDto.builder()
                .id(appointmentEntity.getId())
                .doctorId(appointmentEntity.getDoctorId())
                .patientId(appointmentEntity.getPatientId())
                .appointmentDate(appointmentEntity.getAppointmentDate())
                .appointmentTime(appointmentEntity.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
    }
}
