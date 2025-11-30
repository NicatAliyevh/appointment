package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.response.AdminUserResponseDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface AdminService {
    SuccessResponseDto<List<AdminUserResponseDto>> findAllByUserRole(UserRole doctor);
    SuccessResponseDto<List<AppointmentResponseDto>> findAllAppointments();
}
