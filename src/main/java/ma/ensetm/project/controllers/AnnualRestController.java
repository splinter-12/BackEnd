package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Data;
import ma.ensetm.project.services.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/deliberations")
public class AnnualRestController {
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


    @PostMapping("/test-import")
    public Map<String, Object> testImporting(@RequestBody Map<String, Object> toBeImported) {
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

                for (int i = 0; i < 401; i += 1) {
                    ArrayList<String> line = new ArrayList<>();
                    lines.add(line);
                }

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();

                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = "";
                        //TODO:
                        lines.get(rowCounter - 1).add(cellValue);


                    }
                    rowCounter++;
                }
                theSavedFile.close();

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
