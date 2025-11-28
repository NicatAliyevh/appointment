package nijat.project.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/me/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<SuccessResponseDto<List<AppointmentResponseDto>>> getAppointments(Principal principal) {
        return new ResponseEntity<>(appointmentService.getAppointments(principal.getName()),  HttpStatus.OK);
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<AppointmentResponseDto>> getAppointmentById(@PathVariable String appointmentId,
                                                                                         Principal principal) {
        return new ResponseEntity<>(appointmentService.getAppointmentById(appointmentId, principal.getName()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<SuccessResponseDto<AppointmentResponseDto>> createAppointment(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto,
                                                                                        Principal principal) {
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentRequestDto, principal.getName()), HttpStatus.CREATED);
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<SuccessResponseDto<AppointmentResponseDto>> updateAppointment(@Valid @RequestBody AppointmentRequestDto appointmentRequestDto,
                                                                                        @PathVariable String appointmentId,
                                                                                        Principal principal) {
        return new ResponseEntity<>(appointmentService.updateAppointment(appointmentId, appointmentRequestDto, principal.getName()), HttpStatus.OK);
    }
}
