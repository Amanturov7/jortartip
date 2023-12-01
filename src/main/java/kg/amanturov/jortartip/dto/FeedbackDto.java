package kg.amanturov.jortartip.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class FeedbackDto {
    private Long userId;
    private String message;
    private Timestamp createdDate;
    private Long feedbackTypeId;

}
