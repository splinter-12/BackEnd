package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Student;
import ma.ensetm.project.entities.Element;
import ma.ensetm.project.entities.Module;
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
@RequestMapping("/deliberations/elements")
public class ElementsRestController {
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
    public Element add(@RequestBody Map<String, Object> map) {
        Module  module= moduleService.findById(Long.parseLong(map.get("module").toString()));
        Element element = new Element();
        element.setCode(map.get("code").toString());
        element.setTitre(map.get("titre").toString());
        element.setModule(module);
        element.setBareme(Float.parseFloat(map.get("bareme").toString()));
        element.setPonderation(Float.parseFloat(map.get("ponderation").toString()));
        return elementService.save(element);
    }

    @GetMapping("")
    public List<Module> getAll() {
        return moduleService.getAll();
    }

    @GetMapping("ofModule/{moduleId}")
    public List<Element> getAllByModule(@PathVariable("moduleId") String moduleId) {
        Module  module= moduleService.findById(Long.parseLong(moduleId));
        return elementService.findByModule(module);
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
        elementService.delete(id);
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
