package nijat.project.appointment.service;

import jakarta.validation.Valid;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AppointmentService {
    SuccessResponseDto<List<AppointmentResponseDto>> getAppointments(String name);
    SuccessResponseDto<AppointmentResponseDto> createAppointment(AppointmentRequestDto appointmentRequestDto, String name);
    SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId, String name);
    SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId, @Valid AppointmentRequestDto appointmentRequestDto, String name);
}
