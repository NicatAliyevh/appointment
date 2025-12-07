package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.request.AppointmentUpdateRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AppointmentService {
    SuccessResponseDto<Page<AppointmentResponseDto>> getAppointments(String userId, Pageable pageable);
    SuccessResponseDto<AppointmentResponseDto> getAppointmentById(String appointmentId, String userId);
    SuccessResponseDto<AppointmentResponseDto> updateAppointment(String appointmentId, AppointmentUpdateRequestDto appointmentUpdateRequestDto, String userId);
    SuccessResponseDto<Void> deleteAppointment(String appointmentId, String userId);
}
