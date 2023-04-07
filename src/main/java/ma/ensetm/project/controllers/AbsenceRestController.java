package ma.ensetm.project.controllers;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Absence;
import ma.ensetm.project.entities.Justification;
import ma.ensetm.project.services.AbsenceService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
public class AbsenceRestController {
    private AbsenceService absenceService;

    @GetMapping("absences/{id}")
    public Absence getAbsence(@PathVariable Long id) {
        return absenceService.findById(id);
    }

    @GetMapping("absences/{id}/justifications")
    public List<Justification> getAbsenceJustifications(@PathVariable Long id) {
        Absence absence = absenceService.findById(id);
        return absence.getJustifications();
    }

    @PostMapping("absences")
    public void saveAbsence(@RequestBody Absence absence) {
        Absence absence1 = absenceService.findDuplicate(absence.getElement().getId(), absence.getStudent().getId(), absence.getDate());
        if (absence1 == null) {
            absenceService.addAbsence(absence);
        }
    }

    @PostMapping("absences/{id}/justifications")
    public void saveJustification(@PathVariable Long id, @RequestBody Justification justification) {
        Absence absence = absenceService.findById(id);
        absence.getJustifications().add(justification);
        absenceService.addAbsence(absence);  // update absence adding justifs
    }

    @PutMapping("absences/{id}")
    public void updateAbsence(@PathVariable Long id, @RequestBody Absence absence) {
        absenceService.updateAbsence(absence);
    }

    @DeleteMapping("absences/{id}")
    public void deleteAbsence(@PathVariable Long id) {
        absenceService.deleteAbsence(id);
    }

    @GetMapping("absencesByStudent/{id}")
    public List<Absence> getAbsencesByStudent(@PathVariable String id) {
        return absenceService.findByStudent(id);
    }

    @GetMapping("absencesByStudentCode/{code}")
    public List<Absence> getAbsencesByStudentCode(@PathVariable String code) {
        return absenceService.findByStudentCode(code);
    }

    @GetMapping("absencesByElementModule/{id}")
    public List<Absence> getAbsencesByElementModule(@PathVariable String id) {
        return absenceService.findByElementModule(id);
    }


}
