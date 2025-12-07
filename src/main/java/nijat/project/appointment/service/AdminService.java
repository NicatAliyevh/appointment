package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.AppointmentStatus;
import nijat.project.appointment.model.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    SuccessResponseDto<Page<UserResponseDto>> findAllUsers(UserRole doctor, Pageable pageable);
    SuccessResponseDto<Page<AppointmentResponseDto>> findAllAppointments(AppointmentStatus appointmentStatus, Pageable pageable);
    SuccessResponseDto<Page<AppointmentResponseDto>> findAllAppointmentsByUserId(String userId, Pageable pageable);
    SuccessResponseDto<Void> deleteUser(String userId, UserRole userRole);
}
