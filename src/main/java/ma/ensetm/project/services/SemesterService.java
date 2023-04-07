package ma.ensetm.project.services;


import ma.ensetm.project.entities.Semester;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SemesterService {
    Semester save(Semester element);
    List<Semester> getAll();
    Semester update(Semester element);
    void delete(Long id);
    Semester getByTitleContains(String semesterTitle);
}
