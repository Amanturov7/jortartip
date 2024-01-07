package kg.amanturov.jortartip.dto;


import lombok.Data;

@Data
public class AttachmentRequestDto {
    private String type;
    private String originName;
    private String description;
    private Long userId;
    private Long applicationsId;
    private Long ticketsId;
    private Long reviewsId;
}
