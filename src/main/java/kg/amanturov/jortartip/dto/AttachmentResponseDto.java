package kg.amanturov.jortartip.dto;


import lombok.Data;
import org.springframework.core.io.Resource;

@Data
public class AttachmentResponseDto {
    private String filePath;
    private String type;
    private String extension;
    private Long AttachmentId;
    private String name;
    private String downloadUrl;
    private Long userId;
    private Long appicationsId;
    private Long ticketsId;
    private Long reviewsId;
    private Resource file;
    private String description;
    private String originName;

}
