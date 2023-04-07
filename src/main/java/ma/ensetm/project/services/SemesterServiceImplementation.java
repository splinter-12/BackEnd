package ma.ensetm.project.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.repositories.SemesterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SemesterServiceImplementation implements SemesterService {

    @Autowired
    SemesterRepository semesterRepository;


    @Override
    public Semester save(Semester element) {
        return semesterRepository.save(element);
    }

    @Override
    public List<Semester> getAll() {
        return semesterRepository.findAll();
    }

    @Override
    public Semester update(Semester element) {
        return semesterRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        semesterRepository.deleteById(id);
    }

    @Override
    public Semester getByTitleContains(String semesterTitle) {
        return semesterRepository.findByTitreContains(semesterTitle);
    }
}
