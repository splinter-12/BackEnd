package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Absence;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    List<Absence> findByStudent(Student student);
    List<Absence> findByElement(Element elementModule);
    List<Absence> findByStudentAndElement(Student student, Element elementModule);
    Absence findByStudentAndElementAndDate(Student student, Element element, Date date);
}
