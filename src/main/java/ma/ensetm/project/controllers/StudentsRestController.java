package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Data;
import ma.ensetm.project.dtos.Student;
import ma.ensetm.project.entities.Speciality;
import ma.ensetm.project.services.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/deliberations")
public class StudentsRestController {
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

    private ArrayList<String> modulesAndElements = new ArrayList<>();
    private ArrayList<String> elements = new ArrayList<>();
    private ArrayList<String> coefficients = new ArrayList<>();
    private ArrayList<ArrayList<String>> studentLines = new ArrayList<>();
    private ArrayList<ArrayList<String>> lines = new ArrayList<>();
    private final Path root = Paths.get("uploads");


    @PostMapping("/import-students/{specialityId}")
    public Map<String, Object> importStudents(@PathVariable("specialityId") String specialityId, @RequestBody Map<String, Object> toBeImported) {
        Speciality speciality = specialityService.getById(Long.parseLong(specialityId));
        Map<String, Object> toBeReturned = new HashMap<>();
        Data output = new Data();


        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
        String name = "XlsFile" + dateFormat.format(new Date()) + ".xls";
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(toBeImported.get("data").toString().split("base64,")[1]);
            File file = new File(new File("").getAbsolutePath() + "/uploads/" + name);
            FileOutputStream fop = new FileOutputStream(file);
            fop.write(decodedBytes);
            fop.flush();
            fop.close();

            try {
                FileInputStream theSavedFile = new FileInputStream(file);
                HSSFWorkbook workbook = new HSSFWorkbook(theSavedFile);
                HSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();
                int rowCounter = 1;
                for (int i = 0; i < 41; i += 1) {
                    ArrayList<String> currentLine = new ArrayList<>();
                    studentLines.add(currentLine);
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = getCellValueAsString(cell);

                        if (rowCounter >= 18 && rowCounter < 59) {
                            studentLines.get(rowCounter - 18).add(cellValue);
                        }


                    }
                    rowCounter++;
                }
                theSavedFile.close();

                output = new Data();
                ArrayList<Student> students = new ArrayList<>();
                output.setStudents(students);
                for (ArrayList<String> line : studentLines) {
                    for (int i = 0; i < line.size(); i += 1) {
                        line.set(i, line.get(i).replace(",", "."));
                        line.set(i, line.get(i).replace(",", "."));
                        line.set(i, line.get(i).trim());
                        if (i == 0) {
                            line.set(i, new BigDecimal(line.get(i)).toPlainString());
                        }
                        if (isNumeric(line.get(i))) {
                            DecimalFormat decimalFormat = new DecimalFormat("0.###");
                            decimalFormat.setRoundingMode(RoundingMode.DOWN);
                            line.set(i, decimalFormat.format(Double.parseDouble(line.get(i))));
                        }
                    }
                }
                for (ArrayList<String> line : studentLines) {
                    line.removeIf(string -> string.trim().equals(""));
                }


                elements.removeIf(string -> string.trim().equals(""));
                elements.removeIf(string -> string.trim().equals("null"));

                for (ArrayList<String> studentLine : studentLines) {
                    if (studentLine.size() == 0) break;
                    Student student = new Student();
                    student.setNumero(studentLine.get(0));
                    student.setNom(studentLine.get(1));
                    student.setPrenom(studentLine.get(2));
                    student.setDateNaissance(studentLine.get(3));
                    student.setNoteSemestre(0f);
                    student.setResultatSemestre("");
                    student.setCodeSemestre(semesterService.getAll().get(0).getCode());
                    student.setTitreSemestre(semesterService.getAll().get(0).getTitre());
                    insertStudentIfNotExist(student, speciality);


                    student.setModules(new ArrayList<>());

                    output.getStudents().add(student);
                }

                file.delete();


            } catch (Exception e) {
                e.printStackTrace();
                toBeReturned.put("status", 0);
                toBeReturned.put("message", "The XLS file does not respect the accepted form, please upload another one !");
                return toBeReturned;
            }

            toBeReturned.put("status", 1);
            toBeReturned.put("data", output);
            return toBeReturned;
        } catch (IOException e) {
            toBeReturned.put("status", 0);
            toBeReturned.put("message", "The XLS file does not respect the accepted form, please upload another one !");
            return toBeReturned;
        }
    }

    @GetMapping("{specialityId}")
    public List<ma.ensetm.project.entities.Student> getAllBySpeciality(@PathVariable("specialityId") String specialityId) {
        Speciality speciality = specialityService.getById(Long.parseLong(specialityId));
        return studentService.findBySpeciality(speciality);
    }

    private void insertStudentIfNotExist(Student student, Speciality speciality) {
        ma.ensetm.project.entities.Student item = new ma.ensetm.project.entities.Student();
        item.setId(Long.parseLong(student.getNumero()));
        item.setNom(student.getNom());
        item.setPrenom(student.getPrenom());
        item.setNumero(student.getNumero());
        item.setDateNaissance(student.getDateNaissance());
        item.setSpeciality(speciality);
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
