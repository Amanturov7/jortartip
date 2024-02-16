package kg.amanturov.jortartip.dto.securityDto;

import lombok.Data;


@Data
public class LoginResponse {
    private String accessToken;
    private String userName;
    private Long ruvhId;
    private Long organizationId;
    private String role;
}
