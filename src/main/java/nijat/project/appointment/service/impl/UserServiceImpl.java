package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.EmailAlreadyExistsException;
import nijat.project.appointment.handler.exception.InvalidCredentialsException;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.EmailChangeRequestDto;
import nijat.project.appointment.model.dto.request.EmailChangeVerificationRequestDto;
import nijat.project.appointment.model.dto.request.PasswordChangeRequestDto;
import nijat.project.appointment.model.dto.request.UpdateProfileRequestDto;
import nijat.project.appointment.model.dto.response.PasswordChangeResponseDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserResponseDto;
import nijat.project.appointment.model.entity.EmailUpdateTokenEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.repository.EmailUpdateTokenRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.EmailService;
import nijat.project.appointment.service.UserService;
import nijat.project.appointment.utils.security.JwtService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Instant;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtService jwtService;

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
    public SuccessResponseDto<Void> changeMyEmail(String userId, EmailChangeRequestDto emailChangeRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );

        if(userRepository.existsByEmail(emailChangeRequestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        EmailUpdateTokenEntity emailUpdateTokenEntity = emailUpdateTokenRepository.findByEmail(emailChangeRequestDto.getEmail())
                .orElse(new EmailUpdateTokenEntity());
        emailUpdateTokenEntity.setEmail(emailChangeRequestDto.getEmail());
        emailUpdateTokenEntity.setToken(code);
        emailUpdateTokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        emailUpdateTokenRepository.save(emailUpdateTokenEntity);

        emailService.sendVerificationCode(emailChangeRequestDto.getEmail(), user.getUsername(), code, true);
        return SuccessResponseDto.of("Verification code has been sent to your email address");
    }

    @Override
    public SuccessResponseDto<Void> verifyMyEmail(String userId, EmailChangeVerificationRequestDto emailChangeVerificationRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        EmailUpdateTokenEntity emailUpdateTokenEntity = emailUpdateTokenRepository.findByEmail(emailChangeVerificationRequestDto.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("No pending verification found. It may have expired or been verified already")
        );
        if(emailUpdateTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            emailUpdateTokenRepository.delete(emailUpdateTokenEntity);
            throw new InvalidCredentialsException("Verification token expired. Please request a new one.");
        }
        if(!emailUpdateTokenEntity.getToken().equals(emailChangeVerificationRequestDto.getCode())) {
            throw new InvalidCredentialsException("Invalid verification code");
        }
        emailUpdateTokenRepository.delete(emailUpdateTokenEntity);
        user.setEmail(emailChangeVerificationRequestDto.getEmail());
        userRepository.save(user);
        return SuccessResponseDto.of("Email has been updated successfully");
    }

    @Override
    public SuccessResponseDto<PasswordChangeResponseDto> changeMyPassword(String userId, PasswordChangeRequestDto passwordChangeRequestDto) {
        UUID id = parse(userId);
        UserEntity user = userRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("User with this id: " + id + " not found")
        );
        if(!bCryptPasswordEncoder.matches(passwordChangeRequestDto.getOldPassword(), user.getPassword())){
            throw new InvalidCredentialsException("Invalid credentials");
        }
        if(passwordChangeRequestDto.getOldPassword().equals(passwordChangeRequestDto.getNewPassword())) {
            throw new IllegalArgumentException("New password must be different from the old password");
        }
        user.setPassword(bCryptPasswordEncoder.encode(passwordChangeRequestDto.getNewPassword()));
        user.setPasswordChangedAt(Instant.now());
        userRepository.save(user);

        var userDetails = User.withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities(user.getUserRole().name())
                .build();

        String accessToken = jwtService.generateToken(userDetails);

        PasswordChangeResponseDto passwordChangeResponseDto = PasswordChangeResponseDto.builder()
                .accessToken(accessToken)
                .userResponseDto(mapUserToDto(user))
                .build();
        emailService.sendPasswordChanged(user.getEmail(), user.getUsername());
        return SuccessResponseDto.of(passwordChangeResponseDto,"Password has been updated successfully");
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
