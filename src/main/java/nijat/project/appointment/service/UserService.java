package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.request.EmailChangeRequestDto;
import nijat.project.appointment.model.dto.request.EmailChangeVerificationRequestDto;
import nijat.project.appointment.model.dto.request.PasswordChangeRequestDto;
import nijat.project.appointment.model.dto.request.UpdateProfileRequestDto;
import nijat.project.appointment.model.dto.response.PasswordChangeResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
        SuccessResponseDto<UserResponseDto> getMyProfile(String userId);
        SuccessResponseDto<UserResponseDto> updateMyProfile(String userId, UpdateProfileRequestDto updateProfileRequestDto);
        SuccessResponseDto<Void> changeMyEmail(String userId, EmailChangeRequestDto emailChangeRequestDto);
        SuccessResponseDto<Void> verifyMyEmail(String userId, EmailChangeVerificationRequestDto emailChangeVerificationRequestDto);
        SuccessResponseDto<PasswordChangeResponseDto> changeMyPassword(String userId, PasswordChangeRequestDto passwordChangeRequestDto);
}
