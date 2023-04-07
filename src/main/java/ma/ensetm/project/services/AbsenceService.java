package ma.ensetm.project.services;

import ma.ensetm.project.entities.Absence;

import java.util.Date;
import java.util.List;

public interface AbsenceService {
    void addAbsence(Absence absence);
    List<Absence> findAll();
    Absence findById(Long absenceId);
    void deleteAbsence(Long absenceId);
    void updateAbsence(Absence absence);
    List<Absence> findByStudent(Long studentId);

    List<Absence> findByStudent(String studentId);

    Absence findDuplicate(Long elementId, Long studentId, Date date);
    List<Absence> findByElementModule(String elementModuleId);

    List<Absence> findByStudentCode(String code);
}
