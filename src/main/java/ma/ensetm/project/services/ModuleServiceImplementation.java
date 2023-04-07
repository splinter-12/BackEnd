package ma.ensetm.project.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.repositories.ElementRepository;
import ma.ensetm.project.repositories.ModuleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ModuleServiceImplementation implements ModuleService {

    @Autowired
    ModuleRepository moduleRepository;
    @Autowired
    ElementRepository elementRepository;

    @Override
    public Module findById(Long id) {
        return moduleRepository.findById(id).get();
    }

    @Override
    public Module save(Module element) {
        return moduleRepository.save(element);
    }

    @Override
    public List<Module> getAll() {
        return moduleRepository.findAll();
    }

    @Override
    public Module update(Module element) {
        return moduleRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        moduleRepository.deleteById(id);
    }

    @Override
    public Module findByElement(Element element) {
        List<Module> modules = moduleRepository.findAll();
        List<Element> elements = elementRepository.findAll();
        Element target = null;
        for(Element object: elements){
            if(object.equals(element)){
                target = object;
                break;
            }
        }
        for (Module item : modules) {
            if (target != null && target.getModule().equals(item)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public List<Module> findBySpecialityAndSemester(Speciality speciality, Semester semester) {
        return moduleRepository.findBySpecialityAndSemester(speciality, semester);
    }
}
