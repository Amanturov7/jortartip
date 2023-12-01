package kg.amanturov.jortartip.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;


@Entity
@Data
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feedbackType_id")
    private CommonReference feedbackType;

    private String message;


    private Timestamp createdDate;

}
