package kg.amanturov.jortartip.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float lat;

    private Float lon;

    private String locationAddress;


    private Timestamp createdDate;

    @Column(name = "update_date")
    private Timestamp updateDate;



    @Column(columnDefinition = "TEXT")
    private String Description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roads_id")
    private CommonReference roads;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lights_id")
    private CommonReference lights;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "road_sign_id")
    private CommonReference roadSign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ecologic_factor_id")
    private CommonReference ecologicFactors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private CommonReference status;
}
