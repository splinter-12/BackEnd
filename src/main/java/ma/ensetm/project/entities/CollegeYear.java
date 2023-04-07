package ma.ensetm.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class CollegeYear {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String collegeYear;
    @OneToMany(mappedBy = "collegeYear")
    private Collection<Student> students;
}
