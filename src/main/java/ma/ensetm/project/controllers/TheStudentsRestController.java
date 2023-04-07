package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Student;
import ma.ensetm.project.repositories.StudentRepository;
import ma.ensetm.project.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/students")
public class TheStudentsRestController {
    @Autowired
    SpecialityService specialityService;
    @Autowired
    StudentService studentService;
    @Autowired
    MarkService markService;
    @Autowired
    ModuleService moduleService;
    @Autowired
    SemesterService semesterService;
    @Autowired
    ElementService elementService;


    @GetMapping("byCode/{code}")
    public Student getStudentByCode(@PathVariable("code") String code) {
        ma.ensetm.project.entities.Student student = studentService.findByCode(code);
        Student returned = new Student();
        returned.setNumero(student.getNumero());
        returned.setNom(student.getNom());
        returned.setPrenom(student.getPrenom());
        returned.setDateNaissance(student.getDateNaissance());
        returned.setGender(student.getGender());
        return returned;
    }
    @Autowired
    private StudentRepository studentRepository;

    @GetMapping("/specialities/{specialityId}/semesters/{semesterId}")
    public List<ma.ensetm.project.entities.Student> getStudentsBySpecialityAndSemester(
            @PathVariable Long specialityId,
            @PathVariable Long semesterId
    )
    {
        return studentRepository.findBySpecialityIdAndSemesterId(specialityId, semesterId);
    }


    @GetMapping("/collegeYear/{collegeYearId}/semester/{semesterId}/speciality/{specialityId}")
    public List<ma.ensetm.project.entities.Student> getStudentsByCollegeYearAndSemesterAndSpeciality(
            @PathVariable Long collegeYearId,
            @PathVariable Long semesterId,
            @PathVariable Long specialityId
    ) {
        return studentRepository.findByCollegeYearIdAndSemesterIdAndSpecialityId(
                collegeYearId,
                semesterId,
                specialityId
        );
    }
}
