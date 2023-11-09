package kg.amanturov.jortartip.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EmployeeSIgnUpDto {


    @NotBlank(message = "Login cannot be blank")
    private String login;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 6, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z]).*$", message = "Password must contain at least one letter and one digit")
    private String password;

//    private String pin;
//    private String inn;
    private String email;
//    private String positionEnum;
//    private Long organizationId;
//    private Boolean isOrganization;

}
