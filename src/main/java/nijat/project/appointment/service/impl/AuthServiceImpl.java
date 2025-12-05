package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.BadRequestException;
import nijat.project.appointment.handler.exception.EmailAlreadyExistsException;
import nijat.project.appointment.handler.exception.InvalidCredentialsException;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.dto.request.ForgotPasswordRequestDto;
import nijat.project.appointment.model.dto.request.ResetPasswordRequestDto;
import nijat.project.appointment.model.dto.request.UserLoginRequestDto;
import nijat.project.appointment.model.dto.request.UserRegisterRequestDto;
import nijat.project.appointment.model.dto.request.UserVerificationRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserAuthResponseDto;
import nijat.project.appointment.model.entity.PasswordResetTokenEntity;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.model.entity.VerificationTokenEntity;
import nijat.project.appointment.model.enums.VerificationProgress;
import nijat.project.appointment.repository.PasswordResetTokenRepository;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.repository.VerificationTokenRepository;
import nijat.project.appointment.service.AuthService;
import nijat.project.appointment.service.EmailService;
import nijat.project.appointment.utils.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final EmailService emailService;


    @Override
    public SuccessResponseDto<Void> register(UserRegisterRequestDto userRegisterRequestDto) {
        if(userRepository.findByEmail(userRegisterRequestDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByEmail(userRegisterRequestDto.getEmail())
                .orElse(new VerificationTokenEntity());

        String code = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
        verificationTokenEntity.setToken(bCryptPasswordEncoder.encode(code));
        verificationTokenEntity.setUsername(userRegisterRequestDto.getUsername());
        verificationTokenEntity.setPassword(bCryptPasswordEncoder.encode(userRegisterRequestDto.getPassword()));
        verificationTokenEntity.setEmail(userRegisterRequestDto.getEmail());
        verificationTokenEntity.setUserRole(userRegisterRequestDto.getRole());
        verificationTokenEntity.setExpiryDate(LocalDateTime.now().plusMinutes(5));
        verificationTokenRepository.save(verificationTokenEntity);

        emailService.sendVerificationCode(userRegisterRequestDto.getEmail(), userRegisterRequestDto.getUsername(), code);

        return SuccessResponseDto.of("Verification code has been sent to your email address");
    }

    public SuccessResponseDto<UserAuthResponseDto> verifyAccount(UserVerificationRequestDto userVerificationRequestDto) {
        VerificationTokenEntity verificationTokenEntity = verificationTokenRepository.findByEmail(userVerificationRequestDto.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("No pending verification found. It may have expired or been verified already."));

        if (verificationTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            verificationTokenEntity.setProgress(VerificationProgress.REJECTED);
            throw new InvalidCredentialsException("Verification token expired. Please request a new one.");
        }

        if (!bCryptPasswordEncoder.matches(userVerificationRequestDto.getCode(), verificationTokenEntity.getToken())) {
            throw new InvalidCredentialsException("Invalid verification code.");
        }

        verificationTokenRepository.delete(verificationTokenEntity);

        var user = UserEntity.builder()
                .email(verificationTokenEntity.getEmail())
                .username(verificationTokenEntity.getUsername())
                .password(bCryptPasswordEncoder.encode(verificationTokenEntity.getPassword()))
                .userRole(verificationTokenEntity.getUserRole())
                .build();

        userRepository.save(user);

        var userDetails = User.withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities(user.getUserRole().name())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        UserAuthResponseDto userAuthResponseDto = UserAuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
        return SuccessResponseDto.of(userAuthResponseDto, "Verification successful");
    }

    @Override
    public SuccessResponseDto<UserAuthResponseDto> login(UserLoginRequestDto userLoginRequestDto) {
        UserEntity user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("Email or password is invalid")
        );

        try{
            UUID userId = user.getId();
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userId.toString(),
                            userLoginRequestDto.getPassword()
                    )
            );
        } catch (Exception e) {
            throw new InvalidCredentialsException("Email or password is invalid");
        }

        var userDetails = User.withUsername(user.getId().toString())
                .password(user.getPassword())
                .authorities(user.getUserRole().name())
                .build();

        String accessToken = jwtService.generateToken(userDetails);
        UserAuthResponseDto userAuthResponseDto = UserAuthResponseDto.builder()
                .accessToken(accessToken)
                .build();
        return SuccessResponseDto.of(userAuthResponseDto, "Login successful");
    }

    @Override
    public SuccessResponseDto<Void> forgotPassword(ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String email = forgotPasswordRequestDto.getEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                ()-> new ResourceNotFoundException("User with this: " + email + " email not found")
        );
        String token =  UUID.randomUUID().toString();

        PasswordResetTokenEntity forgotPasswordEntity = passwordResetTokenRepository.findByEmail(email).
                orElse(new PasswordResetTokenEntity());
        forgotPasswordEntity.setEmail(email);
        forgotPasswordEntity.setToken(token);
        forgotPasswordEntity.setExpiryDate(LocalDateTime.now().plusMinutes(15));
        passwordResetTokenRepository.save(forgotPasswordEntity);

        emailService.sendPasswordResetLink(email, user.getUsername(), token);
        return SuccessResponseDto.of("Password reset link has been sent to your email address");
    }

    @Override
    public SuccessResponseDto<Void> resetPassword(ResetPasswordRequestDto resetPasswordRequestDto) {
        String email =  resetPasswordRequestDto.getEmail();
        UserEntity user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User with this: " + email + " email not found")
        );
        PasswordResetTokenEntity passwordResetTokenEntity = passwordResetTokenRepository.findByEmail(email).orElseThrow(
                        () -> new ResourceNotFoundException("Your password reset link is invalid or has expired. Please request a new one")
        );

        if(passwordResetTokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(passwordResetTokenEntity);
            throw new ResourceNotFoundException("Your password reset link has expired, please request a new one");
        }

        if(!resetPasswordRequestDto.getToken().equals(passwordResetTokenEntity.getToken())) {
            System.out.println("from repo: " + passwordResetTokenEntity.getToken());
            System.out.println("from user: " + resetPasswordRequestDto.getToken());
            throw new InvalidCredentialsException("Invalid token");
        }

        if(bCryptPasswordEncoder.matches(resetPasswordRequestDto.getPassword(), user.getPassword())) {
            throw new BadRequestException("Your request could not be processed");
        }

        user.setPassword(bCryptPasswordEncoder.encode(resetPasswordRequestDto.getPassword()));
        userRepository.save(user);
        passwordResetTokenRepository.delete(passwordResetTokenEntity);

        return SuccessResponseDto.of("Your password has been reset successfully");
    }
}
