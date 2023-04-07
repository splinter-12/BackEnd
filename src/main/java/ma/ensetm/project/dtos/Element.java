package ma.ensetm.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Element {
    float note;
    private String code;
    private String titre;
    private Float bareme;
    private Float ponderation;
}
