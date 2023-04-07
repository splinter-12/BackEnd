package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Semester;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SemesterRepository extends JpaRepository<Semester,Long> {
    Semester findByTitreContains(String semesterTitle);
}
