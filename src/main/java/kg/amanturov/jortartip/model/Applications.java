package kg.amanturov.jortartip.model;

import jakarta.persistence.*;
import lombok.Data;
import java.sql.Timestamp;

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

    private String numberAuto;

    private float lon;

    private float lat;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private CommonReference status;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private CommonReference region;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "district_id")
    private CommonReference district;

    private Timestamp createdDate;

    @Column(name = "date_of_violation")
    private Timestamp dateOfViolation;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "violations_id")
    private Violations typeViolations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moder_id")
    private User moder;
}
