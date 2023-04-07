package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findBySpeciality(Speciality speciality);
    Student findByNumero(String username);

    List<Student> findBySpecialityIdAndSemesterId(Long specialityId, Long semesterId);

    List<Student> findByCollegeYearIdAndSemesterIdAndSpecialityId(Long collegeYearId, Long semesterId, Long specialityId);
}
