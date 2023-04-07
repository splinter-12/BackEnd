package ma.ensetm.project.services;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Teacher;
import ma.ensetm.project.repositories.ElementRepository;
import ma.ensetm.project.repositories.TeacherRepository;
import ma.ensetm.project.security.entities.AppRole;
import ma.ensetm.project.security.entities.AppUser;
import ma.ensetm.project.security.service.SecurityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private TeacherRepository teacherRepository;
    private ElementRepository elementRepository;
    private SecurityService securityService;

    @Override
    public Teacher findById(String id) {
        return teacherRepository.findById(id).orElseThrow(() -> new RuntimeException("Teacher not found"));
    }

    @Override
    public Teacher save(Teacher teacher) {
        AppUser newUser = new AppUser();
        newUser.setId(UUID.randomUUID().toString());
        newUser.setUsername(teacher.getCin());
        newUser.setPassword(teacher.getCin());
        newUser.setEmail(teacher.getCin()+"@gmail.com");
        AppRole role = securityService.findRoleByRoleName("PROF");
        newUser.setRoles(Collections.singletonList(role));
        securityService.addNewUser(newUser);
        return teacherRepository.save(teacher);
    }

    @Override
    public Teacher update(Teacher teacher) {
        Teacher teacherToUpdate = teacherRepository.findByCin(teacher.getCin());

        teacherToUpdate.setNom(teacher.getNom());
        teacherToUpdate.setPrenom(teacher.getPrenom());
        return teacherRepository.save(teacherToUpdate);
    }

    @Override
    public void delete(String id) {
        teacherRepository.deleteById(id);
    }

    @Override
    public List<Teacher> getAll()
    {
        return teacherRepository.findAll();
    }

    @Override
    public void assignTeacherToModule(String teacherId, Long moduleId) {
        Teacher teacher = teacherRepository.findByCin(teacherId);
        System.out.println(teacher.getNom());
        Element element = elementRepository.findById(moduleId).get();
        System.out.println(element.getTitre());
        teacher.getElementModules().add(element);
        teacherRepository.save(teacher);
    }

    @Override
    public void unassignTeacherFromModule(String teacherId, Long moduleId) {
        Teacher teacher = teacherRepository.findByCin(teacherId);
        Element element = elementRepository.findById(moduleId).get();
        teacher.getElementModules().remove(element);
        teacherRepository.save(teacher);
    }
}
