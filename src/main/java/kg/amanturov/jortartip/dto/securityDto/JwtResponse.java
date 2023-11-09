package kg.amanturov.jortartip.dto.securityDto;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String type = "Bearer";
    private Long id;
    private String username;
    private String email;
}
