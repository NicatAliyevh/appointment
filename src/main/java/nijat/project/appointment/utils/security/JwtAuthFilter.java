package nijat.project.appointment.utils.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import nijat.project.appointment.model.entity.UserEntity;
import nijat.project.appointment.repository.UserRepository;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import static nijat.project.appointment.utils.common.UUIDUtils.parse;

@RequiredArgsConstructor
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userUuid;

        if (Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            jwt = authHeader.substring(7);
            userUuid = jwtService.extractUsername(jwt);
        } catch (JwtException | IllegalArgumentException e) {
            filterChain.doFilter(request, response);
            return;
        }

        if (Objects.nonNull(userUuid) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userUuid);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UUID id = parse(userUuid);
                UserEntity userEntity = userRepository.findById(id).orElse(null);
                if (Objects.nonNull(userEntity) && Objects.nonNull(userEntity.getPasswordChangedAt())) {
                    Date tokenIssuedAt = jwtService.extractIssuedAt(jwt);
                    var lastPasswordChangeAtTruncated = userEntity.getPasswordChangedAt().truncatedTo(ChronoUnit.SECONDS);

                    if (Objects.nonNull(tokenIssuedAt) && tokenIssuedAt.toInstant().isBefore(lastPasswordChangeAtTruncated)) {
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
