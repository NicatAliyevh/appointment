package nijat.project.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.AppointmentUpdateRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/users/me/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<SuccessResponseDto<Page<AppointmentResponseDto>>> getAppointments(Principal principal,
                                                                                            Pageable pageable) {
        return new ResponseEntity<>(appointmentService.getAppointments(principal.getName(), pageable),  HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<AppointmentResponseDto>> getAppointmentById(@PathVariable String appointmentId,
                                                                                         Principal principal) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(appointmentId, principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<AppointmentResponseDto>> updateAppointment(@Valid @RequestBody AppointmentUpdateRequestDto appointmentUpdateRequestDto,
                                                                                        @PathVariable String appointmentId,
                                                                                        Principal principal) {
        return new ResponseEntity<>(appointmentService.updateAppointment(appointmentId, appointmentUpdateRequestDto, principal.getName()), HttpStatus.OK);
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<Void>> deleteAppointment(@PathVariable String appointmentId,
                                                                        Principal principal) {
        return new ResponseEntity<>(appointmentService.deleteAppointment(appointmentId, principal.getName()), HttpStatus.OK);
    }
}
