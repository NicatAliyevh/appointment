package nijat.project.appointment.service;

import nijat.project.appointment.model.dto.request.UpdateEmailRequestDto;
import nijat.project.appointment.model.dto.request.UpdateEmailVerificationRequestDto;
import nijat.project.appointment.model.dto.request.UpdateProfileRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
        SuccessResponseDto<UserResponseDto> getMyProfile(String userId);
        SuccessResponseDto<UserResponseDto> updateMyProfile(String userId, UpdateProfileRequestDto updateProfileRequestDto);
        SuccessResponseDto<Void> updateMyEmail(String userId, UpdateEmailRequestDto updateEmailRequestDto);
        SuccessResponseDto<Void> verifyMyEmail(String userId, UpdateEmailVerificationRequestDto updateEmailVerificationRequestDto);
}
