package ma.ensetm.project.services;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Absence;
import ma.ensetm.project.entities.Justification;
import ma.ensetm.project.entities.Student;
import ma.ensetm.project.repositories.JustificationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class JustificationServiceImpl implements JustificationService {

    private JustificationRepository justificationRepository;
    private AbsenceService absenceService;
    private StudentService studentService;

    @Override
    public void addJustification(Justification justification) {
        justificationRepository.save(justification);
        absenceService.updateAbsence(justification.getAbsence());
    }

    @Override
    public Justification findById(Long justificationId) {
        return justificationRepository.findById(justificationId).orElseThrow( () -> new RuntimeException("Justification not found"));
    }

    @Override
    public void deleteJustification(Long justificationId) {
        justificationRepository.deleteById(justificationId);
    }

    @Override
    public void updateJustification(Justification justification) {
        Justification justificationToUpdate = justificationRepository.findById(justification.getId()).orElseThrow( () -> new RuntimeException("Justification not found"));
        justificationToUpdate.setDocument(justification.getDocument());
        justificationRepository.save(justificationToUpdate);
    }

    @Override
    public List<Justification> findByStudent(Long studentId) {
        Student student = studentService.findById(studentId);
        List<Absence> absences = absenceService.findByStudent(studentId);
        return absences.stream().map(Absence::getJustifications).flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public List<Justification> findByAbsence(Long absenceId)
    {
        Absence absence = absenceService.findById(absenceId);
        return justificationRepository.findByAbsence(absence);
    }
}
