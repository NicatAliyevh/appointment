package nijat.project.appointment.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.UserLoginRequestDto;
import nijat.project.appointment.model.dto.request.UserRegisterRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserAuthResponseDto;
import nijat.project.appointment.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDto<UserAuthResponseDto>> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return new ResponseEntity<>(authService.register(userRegisterRequestDto), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto<UserAuthResponseDto>> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto){
        return new ResponseEntity<>(authService.login(userLoginRequestDto), HttpStatus.OK);
    }

}
