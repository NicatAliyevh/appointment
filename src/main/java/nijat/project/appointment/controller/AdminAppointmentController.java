package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.AppointmentStatus;
import nijat.project.appointment.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/appointments")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminAppointmentController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<SuccessResponseDto<Page<AppointmentResponseDto>>> getAllAppointments(Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllAppointments(AppointmentStatus.SCHEDULED, pageable), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponseDto<Page<AppointmentResponseDto>>> getAllAppointmentsByUserId(@PathVariable String userId,
                                                                                                       Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllAppointmentsByUserId(userId, pageable), HttpStatus.OK);
    }

    @GetMapping("/pending")
    public ResponseEntity<SuccessResponseDto<Page<AppointmentResponseDto>>> getAllPendingAppointments(Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllAppointments(AppointmentStatus.PENDING, pageable), HttpStatus.OK);
    }
}
