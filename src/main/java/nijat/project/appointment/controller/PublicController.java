package nijat.project.appointment.controller;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.enums.UserRole;
import nijat.project.appointment.service.PublicService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/public")
public class PublicController {
    private final PublicService publicService;

    @GetMapping("/doctors")
    public ResponseEntity<SuccessResponseDto<Page<UserResponseDto>>> getAllDoctors(Pageable pageable) {
        return new ResponseEntity<>(publicService.findAllUsers(UserRole.DOCTOR, pageable), HttpStatus.OK);
    }
}
