package kg.amanturov.jortartip.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "common_reference_type")
@NoArgsConstructor
@AllArgsConstructor
public class CommonReferenceType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String code;

}
