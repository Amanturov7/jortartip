package kg.amanturov.jortartip.dto;


import lombok.Data;

import java.util.List;
@Data
public class TicketsDto {
    private Long id;
    private String question;
    private String correctAnswer;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private List<AttachmentResponseDto> attachments;
}
