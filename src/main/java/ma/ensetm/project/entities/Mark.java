package ma.ensetm.project.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "marks")
public class Mark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float value;
    private String session;
    @OneToOne
    private Element element;
    @OneToOne
    private Student student;
    //private  boolean isFinal;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mark mark = (Mark) o;
        return getSession().equals(mark.getSession()) && getElement().equals(mark.getElement()) && getStudent().equals(mark.getStudent());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSession(), getElement(), getStudent());
    }
}
