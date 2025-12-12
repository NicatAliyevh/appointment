package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/doctors")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDoctorController {
    private final AdminService adminService;

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<SuccessResponseDto<Void>> deleteDoctor(@PathVariable String doctorId) {
        return new ResponseEntity<>(adminService.deleteUser(doctorId, UserRole.DOCTOR),HttpStatus.OK);
    }
}
