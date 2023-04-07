package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Module;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.entities.Speciality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<ma.ensetm.project.entities.Module, Long> {
    List<Module> findBySpecialityAndSemester(Speciality speciality, Semester semester);
}
