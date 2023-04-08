package ma.ensetm.project.controllers;

import lombok.AllArgsConstructor;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Teacher;
import ma.ensetm.project.services.TeacherService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
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
    @PostMapping("/import-teachers")
    public ResponseEntity<String> importTeachers(@RequestParam("file") MultipartFile file) {
        try {
            // kan9raw le fichier Excel et kan3mlo extraction des données
            List<Teacher> teachers = readExcel(file);

            // kan Stockagew les données dans la bdd
            teacherService.saveAll(teachers);

            // Retourne le nombre d'enregistrements importés
            return ResponseEntity.ok("Imported " + teachers.size() + " teachers successfully");
        } catch (IOException e) {
            // Retourne une erreur s'il y a une exception IO
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error importing teachers");
        }
    }


    private List<Teacher> readExcel(MultipartFile file) throws IOException {
        List<Teacher> teachers = new ArrayList<>();

        XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
        XSSFSheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {
            Teacher teacher = new Teacher();
            XSSFRow row = worksheet.getRow(i);

            teacher.setCin(row.getCell(0).getStringCellValue());
            teacher.setNom(row.getCell(1).getStringCellValue());
            teacher.setPrenom(row.getCell(2).getStringCellValue());

            teachers.add(teacher);
        }

        return teachers;
    }

}
