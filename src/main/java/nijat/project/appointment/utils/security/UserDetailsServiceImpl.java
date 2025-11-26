package nijat.project.appointment.utils.security;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.handler.exception.ResourceNotFoundException;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.repository.UserRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@RequiredArgsConstructor
@Service
@NullMarked
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String UUID) throws UsernameNotFoundException
    {
        UUID id = parse(UUID);
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with this id: " + UUID + " not found"));

        return User.withUsername(userEntity.getId().toString())
                .password(userEntity.getPassword())
                .authorities(userEntity.getUserRole().name())
                .build();
    }
}
