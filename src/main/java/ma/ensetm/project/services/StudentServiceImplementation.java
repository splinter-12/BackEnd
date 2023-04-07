package ma.ensetm.project.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.entities.Student;
import ma.ensetm.project.repositories.StudentRepository;
import ma.ensetm.project.security.entities.AppRole;
import ma.ensetm.project.security.entities.AppUser;
import ma.ensetm.project.security.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class StudentServiceImplementation implements StudentService {

    @Autowired
    StudentRepository studentRepository;
    @Autowired
    SecurityService securityService;


    @Override
    public Student findById(Long id) {
        return studentRepository.findById(id).get();
    }

    @Override
    public Student findByUsername(String username) {
        return studentRepository.findByNumero(username);
    }

    @Override
    public Student findByCode(String code) {
        return studentRepository.findByNumero(code);
    }

    @Override
    public Student save(Student element) {
        ///
        AppUser newUser = new AppUser();
        newUser.setUsername(element.getNumero());
        newUser.setEmail(element.getNumero()+"@gmail.com");
        newUser.setPassword(element.getNumero());
        AppRole role = securityService.findRoleByRoleName("STUDENT");
        newUser.setRoles(Collections.singletonList(role));
        securityService.addNewUser(newUser);
        ///
        return studentRepository.save(element);
    }

    @Override
    public List<Student> getAll() {
        return studentRepository.findAll();
    }

    @Override
    public Student update(Student element) {
        return studentRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        studentRepository.deleteById(id);
    }

    @Override
    public List<Student> findBySpeciality(Speciality speciality) {
        return studentRepository.findBySpeciality(speciality);
    }
}
