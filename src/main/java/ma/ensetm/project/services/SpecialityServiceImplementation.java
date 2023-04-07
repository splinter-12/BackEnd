package ma.ensetm.project.services;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.repositories.SpecialityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Service
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class SpecialityServiceImplementation implements SpecialityService {

    @Autowired
    SpecialityRepository specialityRepository;


    @Override
    public Speciality save(Speciality element) {
        return specialityRepository.save(element);
    }

    @Override
    public Speciality getById(long id) {
        return specialityRepository.findById(id).get();
    }

    @Override
    public List<Speciality> getAll() {
        return specialityRepository.findAll();
    }

    @Override
    public Speciality update(Speciality element) {
        return specialityRepository.save(element);
    }

    @Override
    public void delete(Long id) {
        specialityRepository.deleteById(id);
    }
}
