package kg.amanturov.jortartip.model;


import jakarta.persistence.*;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "tickets")
public class Tickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String question;

    private String correctAnswer;

    private Integer ticketNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "theme_id")
    private CommonReference theme;

    private String option1;

    private String option2;

    private String option3;

    private String option4;

    public Tickets() {
    }

    public Tickets(String question, String correctAnswer, String option1, String option2, String option3, String option4) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
    }


}
