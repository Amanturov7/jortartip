package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.security.Timestamp;

@Data
public class FeedbackDto {
    private Long userId;
    private String message;
    private Timestamp createdDate;
    private Long feedbackTypeId;

}
