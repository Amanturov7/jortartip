package kg.amanturov.jortartip.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "common_reference")
@NoArgsConstructor
@AllArgsConstructor
public class CommonReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id", insertable = false, updatable = false)
    private CommonReferenceType type;

    @Column(name = "parent_id")
    private Long parentId;

    private String books;
}
