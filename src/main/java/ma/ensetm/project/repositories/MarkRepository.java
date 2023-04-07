package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Mark;
import ma.ensetm.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MarkRepository extends JpaRepository<Mark,Long> {
    Mark getByElementAndSessionAndStudent(Element element, String session, Student student);
}
