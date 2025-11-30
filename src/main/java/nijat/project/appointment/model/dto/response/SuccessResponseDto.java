package nijat.project.appointment.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SuccessResponseDto<T> {
    @Builder.Default
    HttpStatus status = HttpStatus.OK;
    @Builder.Default
    int statusCode = 200;
    @Builder.Default
    LocalDateTime timestamp = LocalDateTime.now();
    String message;
    T data;

    public static SuccessResponseDto<Void> of(String message){
        return SuccessResponseDto.<Void>builder()
                .message(message).build();
    }

    public static <T> SuccessResponseDto<T> of(T data, String message){
        return SuccessResponseDto.<T>builder()
                .message(message)
                .data(data).build();
    }
}
