package ma.ensetm.project.services;


import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ElementService {
    Element save(Element element);
    Element findByCode(String code);
    List<Element> getAll();
    Element update(Element element);
    void delete(Long id);
    List<Element> findByModule(Module module);
}
