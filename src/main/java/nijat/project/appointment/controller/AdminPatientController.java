package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.AdminUserResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/patients")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminPatientController {
    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<AdminUserResponseDto>>> getAllPatients() {
        return new ResponseEntity<>(adminService.findAllUsers(UserRole.PATIENT), HttpStatus.OK);
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<SuccessResponseDto<Void>> deletePatient(@PathVariable String patientId) {
        return new ResponseEntity<>(adminService.deleteUser(patientId, UserRole.PATIENT), HttpStatus.OK);
    }
}
