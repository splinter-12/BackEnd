package ma.ensetm.project.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Student {
    private String numero;
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String gender;
    private String photo;
    private float noteSemestre;
    private String resultatSemestre;
    private String codeSemestre;
    private String titreSemestre;
    private List<Module> modules;
}
