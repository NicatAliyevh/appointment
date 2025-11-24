package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    @Override

    public List<AppointmentResponseDto> getAppointments() {
        List<AppointmentEntity> appointments = appointmentRepository.findAll();
        return appointments.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto) {
        AppointmentEntity appointmentEntity = AppointmentEntity.builder()
                .doctorId(appointmentRequestDto.getDoctorId())
                .patientId(appointmentRequestDto.getPatientId())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .build();
        appointmentRepository.save(appointmentEntity);
        return mapToDto(appointmentEntity);
    }

    @Override
    public AppointmentResponseDto getAppointmentById(String appointmentId) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Appointment with id: " + appointmentId + " not found")
        );
        return mapToDto(appointmentEntity);
    }

    @Override
    public AppointmentResponseDto updateAppointment(String appointmentId, AppointmentRequestDto appointmentRequestDto) {
        UUID id = parse(appointmentId);

        AppointmentEntity appointmentEntity = appointmentRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Appointment with id: " + id + " not found"));

        appointmentEntity.setDoctorId(appointmentRequestDto.getDoctorId());
        appointmentEntity.setPatientId(appointmentRequestDto.getPatientId());
        appointmentEntity.setAppointmentDate(appointmentRequestDto.getAppointmentDate());
        appointmentEntity.setAppointmentTime(appointmentRequestDto.getAppointmentTime());
        appointmentRepository.save(appointmentEntity);

        return AppointmentResponseDto.builder()
                .id(id)
                .patientId(appointmentRequestDto.getPatientId())
                .doctorId(appointmentRequestDto.getDoctorId())
                .appointmentDate(appointmentRequestDto.getAppointmentDate())
                .appointmentTime(appointmentRequestDto.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
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
