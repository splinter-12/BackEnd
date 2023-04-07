package ma.ensetm.project.controllers;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Teacher;
import ma.ensetm.project.services.TeacherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class TeacherRestController {
     private TeacherService teacherService;

     @GetMapping("/teachers")
     public List<Teacher> getTeachers() {
         return teacherService.getAll();
     }

     @GetMapping("/teachers/{id}")
    public Teacher getTeacher(@PathVariable String id) {
        return teacherService.findById(id);
    }

    @GetMapping("/teachers/{id}/modules")
    public List<Element> getTeacherModules(@PathVariable String id) {
        return teacherService.findById(id).getElementModules();
    }

}
