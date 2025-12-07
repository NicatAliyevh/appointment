package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.AppointmentStatus;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AdminService {
    SuccessResponseDto<List<UserResponseDto>> findAllUsers(UserRole doctor);
    SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointments(AppointmentStatus appointmentStatus);
    SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointmentsByUserId(String userId);
    SuccessResponseDto<Void> deleteUser(String userId, UserRole userRole);
}
