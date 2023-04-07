package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Student;
import ma.ensetm.project.entities.Module;
import ma.ensetm.project.entities.Semester;
import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.services.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/deliberations/modules")
public class ModulesRestController {
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

    @PostMapping("")
    public Module add(@RequestBody Map<String, Object> map) {
        Speciality speciality = specialityService.getById(Long.parseLong(map.get("speciality").toString()));
        Semester semester = semesterService.getByTitleContains(map.get("semester").toString());
        Module module = new Module();
        module.setCode(map.get("code").toString());
        module.setTitre(map.get("titre").toString());
        module.setSemester(semester);
        module.setSpeciality(speciality);
        return moduleService.save(module);
    }

    @GetMapping("")
    public List<Module> getAll() {
        return moduleService.getAll();
    }

    @GetMapping("{specialityId}/{semesterTitle}")
    public List<Module> getAllBySpecialityAndSemester(@PathVariable("specialityId") String specialityId, @PathVariable("semesterTitle") String semesterTitle) {
        Speciality speciality = specialityService.getById(Long.parseLong(specialityId));
        Semester semester = semesterService.getByTitleContains(semesterTitle);
        return moduleService.findBySpecialityAndSemester(speciality, semester);
    }


    @PutMapping("{id}")
    public Module update(@PathVariable("id") long id, @RequestBody Module module) {
        module.setId(id);
        moduleService.update(module);
        return module;
    }

    @DeleteMapping("{id}")
    public Map<String, Object> deleteOne(@PathVariable("id") long id) {
        Map<String, Object> data = new HashMap<>();
        moduleService.delete(id);
        data.put("status", 1);
        return data;
    }

    private void insertStudentIfNotExist(Student student) {
        ma.ensetm.project.entities.Student item = new ma.ensetm.project.entities.Student();
        item.setId(Long.parseLong(student.getNumero()));
        item.setNom(student.getNom());
        item.setPrenom(student.getPrenom());
        item.setNumero(student.getNumero());
        item.setDateNaissance(student.getDateNaissance());
        item.setSpeciality(specialityService.getAll().get(0));
        studentService.save(item);
    }

    public static void toCsv(ArrayList<ArrayList<String>> array) throws IOException {
        FileWriter writer = new FileWriter("/Users/yassine/Desktop/list.csv");
        for (ArrayList<String> line : array) {
            String collect = line.stream().collect(Collectors.joining(",")) + "\n";
            writer.write(collect);
        }
        writer.close();
    }

    public boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public void changeCellBackgroundColor(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();
        if (cellStyle == null) {
            cellStyle = cell.getSheet().getWorkbook().createCellStyle();
        }
        cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cell.setCellStyle(cellStyle);
    }

    public String getCellValueAsString(Cell cell) {
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue()).trim();
        } else if (cell.getCellType() == CellType.FORMULA) {
            if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                return String.valueOf(cell.getNumericCellValue()).trim();
            } else if (cell.getCachedFormulaResultType() == CellType.STRING) {
                return cell.getStringCellValue().trim();
            }
        }
        return "null";
    }

    public Map<String, String> getSheetInformation(HSSFSheet sheet) {
        Map<String, String> sheetInformation = new HashMap<>();
        Iterator<Row> rowIterator = sheet.iterator();
        int rowCounter = 1;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            int cellCounter = 1;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = getCellValueAsString(cell);

                if (rowCounter == 3 && cellCounter == 7) {
                    sheetInformation.put("year", cellValue);
                }


                cellCounter += 1;
            }
            rowCounter++;
        }
        return sheetInformation;
    }

    public String keep3Digits(String mark) {
        float grade = Float.parseFloat(mark);
        BigDecimal bigDecimal = new BigDecimal(grade);
        BigDecimal decimal = bigDecimal.setScale(3, RoundingMode.DOWN);
        return String.valueOf(decimal.floatValue());
    }
}
