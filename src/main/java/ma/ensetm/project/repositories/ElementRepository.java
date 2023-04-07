package ma.ensetm.project.repositories;

import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ElementRepository extends JpaRepository<Element,Long> {
    List<Element> findByModule(Module module);
    Element findByCode(String code);
}
