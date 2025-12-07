package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.service.AdminService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @GetMapping
    public ResponseEntity<SuccessResponseDto<Page<UserResponseDto>>> getAllDoctors(Pageable pageable) {
        return new ResponseEntity<>(adminService.findAllUsers(UserRole.DOCTOR, pageable), HttpStatus.OK);
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<SuccessResponseDto<Void>> deleteDoctor(@PathVariable String doctorId) {
        return new ResponseEntity<>(adminService.deleteUser(doctorId, UserRole.DOCTOR),HttpStatus.OK);
    }
}
