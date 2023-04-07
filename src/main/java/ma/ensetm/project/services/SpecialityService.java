package ma.ensetm.project.services;


import ma.ensetm.project.entities.Speciality;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SpecialityService {
    Speciality save(Speciality element);
    Speciality getById(long id);
    List<Speciality> getAll();
    Speciality update(Speciality element);
    void delete(Long id);
}
