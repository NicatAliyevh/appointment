package nijat.project.appointment.service;

import jakarta.validation.Valid;
import nijat.project.appointment.model.dto.request.ForgotPasswordRequestDto;
import nijat.project.appointment.model.dto.request.ResetPasswordRequestDto;
import nijat.project.appointment.model.dto.request.UserLoginRequestDto;
import nijat.project.appointment.model.dto.request.UserRegisterRequestDto;
import nijat.project.appointment.model.dto.request.UserVerificationRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserAuthResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    SuccessResponseDto<UserAuthResponseDto> login(@Valid UserLoginRequestDto userLoginRequestDto);
    SuccessResponseDto<Void> register(@Valid UserRegisterRequestDto userRegisterRequestDto);
    SuccessResponseDto<UserAuthResponseDto> verifyAccount(@Valid UserVerificationRequestDto userVerificationRequestDto);
    SuccessResponseDto<Void> forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto);
    SuccessResponseDto<Void> resetPassword(@Valid ResetPasswordRequestDto resetPasswordRequestDto);
}
