package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.EmailAlreadyExistsException;
import nijat.project.appointment.handler.exception.InvalidCredentialsException;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.UpdateEmailRequestDto;
import nijat.project.appointment.model.dto.request.UpdateEmailVerificationRequestDto;
import nijat.project.appointment.model.dto.request.UpdateProfileRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.entity.EmailUpdateTokenEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.repository.EmailUpdateTokenRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.EmailService;
import nijat.project.appointment.service.UserService;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import static nijat.project.appointment.utils.common.EnumUtils.formatRole;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EmailUpdateTokenRepository emailUpdateTokenRepository;
    private final EmailService emailService;

    @Override
    public SuccessResponseDto<UserResponseDto> getMyProfile(String userId) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        return SuccessResponseDto.of(mapUserToDto(user),"Profile retrieved successfully");
    }

    @Override
    public SuccessResponseDto<UserResponseDto> updateMyProfile(String userId, UpdateProfileRequestDto updateProfileRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        user.setUsername(updateProfileRequestDto.getUsername());
        userRepository.save(user);
        return SuccessResponseDto.of(mapUserToDto(user),"Profile updated successfully");
    }

    @Override
    public SuccessResponseDto<Void> updateMyEmail(String userId, UpdateEmailRequestDto updateEmailRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );

        if(userRepository.existsByEmail(updateEmailRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        EmailUpdateTokenEntity emailUpdateTokenEntity = emailUpdateTokenRepository.findByEmail(updateEmailRequestDto.getEmail())
                .orElse(new EmailUpdateTokenEntity());
        emailUpdateTokenEntity.setEmail(updateEmailRequestDto.getEmail());
        emailUpdateTokenEntity.setToken(code);
        emailUpdateTokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        emailUpdateTokenRepository.save(emailUpdateTokenEntity);

        emailService.sendVerificationCode(updateEmailRequestDto.getEmail(), user.getUsername(), code, true);
        return SuccessResponseDto.of("Verification code has been sent to your email address");
    }

    @Override
    public SuccessResponseDto<Void> verifyMyEmail(String userId, UpdateEmailVerificationRequestDto updateEmailVerificationRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        EmailUpdateTokenEntity emailUpdateTokenEntity = emailUpdateTokenRepository.findByEmail(updateEmailVerificationRequestDto.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("No pending verification found. It may have expired or been verified already")
        );
        if(emailUpdateTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            emailUpdateTokenRepository.delete(emailUpdateTokenEntity);
            throw new InvalidCredentialsException("Verification token expired. Please request a new one.");
        }
        if(!emailUpdateTokenEntity.getToken().equals(updateEmailVerificationRequestDto.getCode())) {
            throw new InvalidCredentialsException("Invalid verification code.");
        }
        emailUpdateTokenRepository.delete(emailUpdateTokenEntity);
        user.setEmail(updateEmailVerificationRequestDto.getEmail());
        userRepository.save(user);
        return SuccessResponseDto.of("Email has been updated successfully");
    }

    public UserResponseDto mapUserToDto(UserEntity userEntity) {
        String userRole = formatRole(userEntity.getUserRole());
        return UserResponseDto.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .role(userRole)
                .build();
    }
}
