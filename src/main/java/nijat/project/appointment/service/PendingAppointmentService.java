package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.request.PendingAppointmentRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;

public interface PendingAppointmentService {
    SuccessResponseDto<Void> requestAppointment(PendingAppointmentRequestDto pendingAppointmentRequestDto, String userId);
    SuccessResponseDto<Void> approveAppointment(String appointmentId, String userId);
    SuccessResponseDto<Void> cancelAppointment(String appointmentId, String userId);
}
