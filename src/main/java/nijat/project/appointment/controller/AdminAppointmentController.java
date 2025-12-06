package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/appointments")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminAppointmentController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<AppointmentResponseDto>>> getAllAppointments() {
        return new ResponseEntity<>(adminService.findAllAppointments(), HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponseDto<List<AppointmentResponseDto>>> getAllAppointmentsByUserId(@PathVariable String userId) {
        return new ResponseEntity<>(adminService.findAllAppointmentsByUserId(userId), HttpStatus.OK);
    }
}
