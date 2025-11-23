package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.AppointmentRequestDto;
import nijat.project.appointment.model.dto.response.AppointmentResponseDto;
import nijat.project.appointment.service.AppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users/me/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public ResponseEntity<List<AppointmentResponseDto>> getAppointments() {
        return new ResponseEntity<>(appointmentService.getAppointments(),  HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<AppointmentResponseDto> createAppointment(AppointmentRequestDto appointmentRequestDto) {
        return new ResponseEntity<>(appointmentService.createAppointment(appointmentRequestDto), HttpStatus.CREATED);
    }
}
