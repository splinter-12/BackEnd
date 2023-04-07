package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
    Teacher findByCin(String cin);
    List<Element> findByElementModules(String cin);
}
