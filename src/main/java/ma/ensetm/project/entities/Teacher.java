package ma.ensetm.project.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ma.ensetm.project.security.entities.AppUser;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Teacher {
    @Id
    private String cin;
    private String nom;
    private String prenom;

    @OneToMany
    private List<Element> elementModules = new ArrayList<>();

    @OneToOne
    private AppUser user;

    public Teacher(String cin, String nom, String prenom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }
}
