package kg.amanturov.jortartip.dto.securityDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponseDto {
    private String accessToken;
    private String token;
    private LocalDateTime refresh;
}
