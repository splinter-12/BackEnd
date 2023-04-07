package ma.ensetm.project.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Mark;
import ma.ensetm.project.repositories.MarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class MarkServiceImplementation implements MarkService {

    @Autowired
    MarkRepository markRepository;


    @Override
    public Mark save(Mark element) {
        if (!getAll().contains(element)) {
            return markRepository.save(element);
        }
        return null;
    }

    @Override
    public List<Mark> getAll() {
        return markRepository.findAll();
    }

    @Override
    public Mark update(Mark element) {
        return markRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        markRepository.deleteById(id);
    }
}
