package ma.ensetm.project.services;


import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.entities.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface StudentService {
    Student findById(Long id);
    Student findByUsername(String username);
    Student findByCode(String code);
    Student save(Student Student);
    List<Student> getAll();
    Student update(Student Student);
    void delete(Long id);
    List<Student> findBySpeciality(Speciality speciality);
}
