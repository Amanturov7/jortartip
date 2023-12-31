package kg.amanturov.jortartip.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "violations")
@Data
public class Violations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String statya;

    private Integer part;

    private String description;

    private Timestamp createdDate;

    private Timestamp updateDate;

    private Float costFiz;

    private Float costUr;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organ_id")
    private CommonReference organ;


}
