package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.request.AppointmentUpdateRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AppointmentService {
    SuccessResponseDto<List<AppointmentResponseDto>> getAppointments(String userId);
    SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId, String userId);
    SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId, AppointmentUpdateRequestDto appointmentUpdateRequestDto, String userId);
    SuccessResponseDto<Void> deleteAppointment(String appointmentId, String userId);
}
