package ma.ensetm.project.services;


import ma.ensetm.project.entities.Mark;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MarkService {
    Mark save(Mark element);
    List<Mark> getAll();
    Mark update(Mark element);
    void delete(Long id);
}
