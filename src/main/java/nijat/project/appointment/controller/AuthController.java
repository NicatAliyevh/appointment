package nijat.project.appointment.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.ForgotPasswordRequestDto;
import nijat.project.appointment.model.dto.request.ResetPasswordRequestDto;
import nijat.project.appointment.model.dto.request.UserLoginRequestDto;
import nijat.project.appointment.model.dto.request.UserRegisterRequestDto;
import nijat.project.appointment.model.dto.request.UserVerificationRequestDto;
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

    @RateLimiter(name = "auth-rate-limiter")
    @PostMapping("/register")
    public ResponseEntity<SuccessResponseDto<Void>> register(@Valid @RequestBody UserRegisterRequestDto userRegisterRequestDto) {
        return new ResponseEntity<>(authService.register(userRegisterRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto<UserAuthResponseDto>> login(@Valid @RequestBody UserLoginRequestDto userLoginRequestDto) {
        return new ResponseEntity<>(authService.login(userLoginRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PostMapping("/verify")
    public ResponseEntity<SuccessResponseDto<UserAuthResponseDto>> verifyAccount(@Valid @RequestBody UserVerificationRequestDto userVerificationRequestDto) {
        return new ResponseEntity<>(authService.verifyAccount(userVerificationRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PostMapping("/forgot-password")
    public ResponseEntity<SuccessResponseDto<Void>> forgotPassword(@Valid @RequestBody ForgotPasswordRequestDto forgotPasswordRequestDto) {
        return new ResponseEntity<>(authService.forgotPassword(forgotPasswordRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PostMapping("/reset-password")
    public ResponseEntity<SuccessResponseDto<Void>> resetPassword(@Valid @RequestBody ResetPasswordRequestDto resetPasswordRequestDto){
        return new ResponseEntity<>(authService.resetPassword(resetPasswordRequestDto), HttpStatus.OK);
    }
}
