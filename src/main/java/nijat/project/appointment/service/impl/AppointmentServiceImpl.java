package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.entity.AppointmentEntity;
import nijat.project.appointment.repository.AppointmentRepository;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

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

    public AppointmentResponseDto mapToDto(AppointmentEntity appointmentEntity) {
        return AppointmentResponseDto.builder()
                .doctorId(appointmentEntity.getDoctorId())
                .patientId(appointmentEntity.getPatientId())
                .appointmentDate(appointmentEntity.getAppointmentDate())
                .appointmentTime(appointmentEntity.getAppointmentTime())
                .status(appointmentEntity.getStatus())
                .build();
    }
}
