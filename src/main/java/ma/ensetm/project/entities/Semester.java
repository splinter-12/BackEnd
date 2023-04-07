package ma.ensetm.project.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "semesters")
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String titre;

    @OneToMany(mappedBy = "semester")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<Student> students = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Semester module = (Semester) o;
        return getCode().equals(module.getCode()) && getTitre().equals(module.getTitre());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getTitre());
    }
}
