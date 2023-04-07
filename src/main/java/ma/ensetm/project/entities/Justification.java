package ma.ensetm.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Justification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String document; //base64
    @ManyToOne
    private Absence absence;
}
