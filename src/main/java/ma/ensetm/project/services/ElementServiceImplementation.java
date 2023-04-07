package ma.ensetm.project.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
import ma.ensetm.project.repositories.ElementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class ElementServiceImplementation implements ElementService {

    @Autowired
    ElementRepository elementRepository;


    @Override
    public Element save(Element element) {
        return elementRepository.save(element);
    }

    @Override
    public Element findByCode(String code) {
        return elementRepository.findByCode(code);
    }

    @Override
    public List<Element> getAll() {
        return elementRepository.findAll();
    }

    @Override
    public Element update(Element element) {
        return elementRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        elementRepository.deleteById(id);
    }

    @Override
    public List<Element> findByModule(Module module) {
        return elementRepository.findByModule(module);
    }
}
