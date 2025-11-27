package nijat.project.appointment.service.impl;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.EmailAlreadyExistsException;
import nijat.project.appointment.handler.exception.InvalidCredentialsException;
import nijat.project.appointment.model.dto.request.UserLoginRequestDto;
import nijat.project.appointment.model.dto.request.UserRegisterRequestDto;
import nijat.project.appointment.model.dto.response.SuccessResponseDto;
import nijat.project.appointment.model.dto.response.UserAuthResponseDto;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.repository.UserRepository;
import nijat.project.appointment.service.AuthService;
import nijat.project.appointment.utils.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public SuccessResponseDto<UserAuthResponseDto> register(UserRegisterRequestDto userRegisterRequestDto) {
        if(userRepository.findByEmail(userRegisterRequestDto.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("Email already exists.");
        }

        var user = UserEntity.builder()
                .email(userRegisterRequestDto.getEmail())
                .username(userRegisterRequestDto.getUsername())
                .password(bCryptPasswordEncoder.encode(userRegisterRequestDto.getPassword()))
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
        return SuccessResponseDto.of(userAuthResponseDto, "Registration successful");
    }

    @Override
    public SuccessResponseDto<UserAuthResponseDto> login(UserLoginRequestDto userLoginRequestDto) {
        UserEntity user = userRepository.findByEmail(userLoginRequestDto.getEmail()).orElseThrow(
                () -> new InvalidCredentialsException("Email or password is invalid.")
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
            throw new InvalidCredentialsException("Email or password is invalid.");
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
}
