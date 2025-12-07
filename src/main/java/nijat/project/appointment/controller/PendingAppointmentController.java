package nijat.project.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.PendingAppointmentRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.service.PendingAppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users/me/appointments")
@RequiredArgsConstructor
public class PendingAppointmentController {

    private  final PendingAppointmentService pendingAppointmentService;

    @PreAuthorize("hasAuthority('PATIENT')")
    @PostMapping("/request")
    public ResponseEntity<SuccessResponseDto<Void>> requestAppointment(@Valid @RequestBody PendingAppointmentRequestDto pendingAppointmentRequestDto,
                                                                       Principal principal) {
        return new ResponseEntity<>(pendingAppointmentService.requestAppointment(pendingAppointmentRequestDto, principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PostMapping("/approve/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<Void>> approveAppointment(@PathVariable String appointmentId,
                                                                       Principal principal) {
        return new ResponseEntity<>(pendingAppointmentService.approveAppointment(appointmentId, principal.getName()), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('DOCTOR')")
    @PostMapping("/reject/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<Void>> rejectAppointment(@PathVariable String appointmentId,
                                                                      Principal principal){
        return new ResponseEntity<>(pendingAppointmentService.rejectAppointment(appointmentId, principal.getName()), HttpStatus.OK);
    }
}
