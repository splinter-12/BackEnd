package ma.ensetm.project.services;

import ma.ensetm.project.entities.Justification;

import java.util.List;

public interface JustificationService {
    void addJustification(Justification justification);
    //List<Justification> findAll();
    Justification findById(Long justificationId);
    void deleteJustification(Long justificationId);
    void updateJustification(Justification justification);
    List<Justification> findByStudent(Long studentId);
    List<Justification> findByAbsence(Long absenceId);
}
