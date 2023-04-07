package ma.ensetm.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Module {
    private String code;
    private String titre;
    private float note;
    private String resultat;
    private List<Element> elements;
}
