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
    private Long waterUserId;
    private Resource file;
    private Long organizationId;
    private Long objectId;
    private Long systemId;
    private String description;
    private String originName;

}
