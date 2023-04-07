package ma.ensetm.project.services;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Absence;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Student;
import ma.ensetm.project.repositories.AbsenceRepository;
import ma.ensetm.project.repositories.ElementRepository;
import ma.ensetm.project.repositories.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class AbsenceServiceImpl implements AbsenceService {

    private AbsenceRepository absenceRepository;
    private StudentRepository studentRepository;
    private ElementRepository elementRepository;


    @Override
    public void addAbsence(Absence absence) {
        absenceRepository.save(absence);
    }

    @Override
    public List<Absence> findAll() {
        return absenceRepository.findAll();
    }

    @Override
    public Absence findById(Long absenceId) {
        return absenceRepository.findById(absenceId).orElseThrow(() -> new RuntimeException("Absence not found"));
    }

    @Override
    public void deleteAbsence(Long absenceId) {
        absenceRepository.deleteById(absenceId);
    }

    //update if justifs added
    @Override
    public void updateAbsence(Absence absence) {
        Absence absenceToUpdate = absenceRepository.findById(absence.getId()).orElseThrow(() -> new RuntimeException("Absence not found"));
        absenceToUpdate.setJustified(absence.isJustified());
        absenceToUpdate.setJustifications(absence.getJustifications());
        absenceRepository.save(absenceToUpdate);
    }

    @Override
    public List<Absence> findByStudent(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(() -> new RuntimeException("Student not found"));
        return absenceRepository.findByStudent(student);
    }

    @Override
    public List<Absence> findByStudent(String studentId) {
        Student student = studentRepository.findByNumero(studentId);
        return absenceRepository.findByStudent(student);
    }

    @Override
    public List<Absence> findByStudentCode(String code) {
        Student student = studentRepository.findByNumero(code);
        return absenceRepository.findByStudent(student);
    }

    @Override
    public Absence findDuplicate(Long elementId, Long studentId, Date date) {
        Element element = elementRepository.findById(elementId).get();
        Student student = studentRepository.findById(studentId).get();
        return absenceRepository.findByStudentAndElementAndDate(student, element, date);
    }

    @Override
    public List<Absence> findByElementModule(String elementModuleId) {
        Element element = elementRepository.findByCode(elementModuleId);
        return absenceRepository.findByElement(element);
    }
}
