package kg.amanturov.jortartip.dto.securityDto;

import lombok.Data;

import java.math.BigInteger;

@Data
public class SignupRequest {

    private String login;
    private String email;
    private String password;
    private String inn;
    private BigInteger phone;
    private String address;
    private Long waterUserId;
    private Long organizationId;
    private Boolean isOrganization;
}
