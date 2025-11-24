package nijat.project.appointment.service;

import jakarta.validation.Valid;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
public interface AppointmentService {
    List<AppointmentResponseDto> getAppointments();
    AppointmentResponseDto createAppointment(AppointmentRequestDto appointmentRequestDto);
    AppointmentResponseDto getAppointmentById(UUID appointmentId);
    AppointmentResponseDto updateAppointment(UUID appointmentId, @Valid AppointmentRequestDto appointmentRequestDto);
}
