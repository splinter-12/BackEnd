package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Absence;
import ma.ensetm.project.entities.Justification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JustificationRepository extends JpaRepository<Justification, Long> {
    List<Justification> findByAbsence(Absence absence);

}
