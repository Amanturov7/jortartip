package kg.amanturov.jortartip.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "applications")
public class Applications {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private String place;

    private float lon;

    private float lat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "violations_id")
    private CommonReference typeViolations;

}
