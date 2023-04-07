package ma.ensetm.project.services;


import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.entities.Speciality;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ModuleService {
    Module findById(Long id);
    Module save(Module element);
    List<Module> getAll();
    Module update(Module element);
    void delete(Long id);
    Module findByElement(Element element);

    List<Module> findBySpecialityAndSemester(Speciality speciality, Semester semester);
}
