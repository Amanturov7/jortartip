package kg.amanturov.jortartip.dto.securityDto;

import lombok.Data;

@Data
public class SignupRequest {

    private String login;
    private String email;
    private String password;
    private String inn;
    private Long waterUserId;
    private Long organizationId;
    private Boolean isOrganization;
}
