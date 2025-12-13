package nijat.project.appointment.controller;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.dto.request.UpdateEmailRequestDto;
import nijat.project.appointment.model.dto.request.UpdateEmailVerificationRequestDto;
import nijat.project.appointment.model.dto.request.UpdateProfileRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/me")
public class UserController {
    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<SuccessResponseDto<UserResponseDto>> getMyProfile(Principal principal) {
        return new ResponseEntity<>(userService.getMyProfile(principal.getName()), HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<SuccessResponseDto<UserResponseDto>> updateMyProfile(Principal principal,
                                                                               @Valid @RequestBody UpdateProfileRequestDto updateProfileRequestDto) {
        return new ResponseEntity<>(userService.updateMyProfile(principal.getName(), updateProfileRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PutMapping("/email")
    public ResponseEntity<SuccessResponseDto<Void>> updateMyEmail(Principal principal,
                                                                  @Valid @RequestBody UpdateEmailRequestDto updateEmailRequestDto) {
        return new ResponseEntity<>(userService.updateMyEmail(principal.getName(), updateEmailRequestDto), HttpStatus.OK);
    }

    @RateLimiter(name = "auth-rate-limiter")
    @PutMapping("/email/verify")
    public ResponseEntity<SuccessResponseDto<Void>> verifyMyEmail(Principal principal,
                                                                  @Valid @RequestBody UpdateEmailVerificationRequestDto updateEmailVerificationRequestDto) {
        return new ResponseEntity<>(userService.verifyMyEmail(principal.getName(), updateEmailVerificationRequestDto), HttpStatus.OK);
    }
}
