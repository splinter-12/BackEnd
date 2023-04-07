package ma.ensetm.project.controllers;

import ma.ensetm.project.dtos.Data;
import ma.ensetm.project.dtos.Element;
import ma.ensetm.project.dtos.Module;
import ma.ensetm.project.dtos.Student;
import ma.ensetm.project.entities.Mark;
import ma.ensetm.project.services.*;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/deliberations")
public class S1s2RestController {
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

    @PostMapping("/import-s1s2")
    public Map<String, Object> uploadFileS1s2(@RequestBody Map<String, Object> toBeImported) {
        ArrayList<String> modulesAndElements = new ArrayList<>();
        ArrayList<String> elements = new ArrayList<>();
        ArrayList<String> coefficients = new ArrayList<>();
        ArrayList<ArrayList<String>> studentLines = new ArrayList<>();
        ArrayList<ArrayList<String>> lines = new ArrayList<>();
        final Path root = Paths.get("uploads");

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

                rowloop: while (rowIterator.hasNext()) {

                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    int cellCounter = 1;
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        String cellValue = getCellValueAsString(cell);

                        if (rowCounter == 14) {
                            elements.add(cellValue.trim());
                        }

                        if (rowCounter >= 18 && rowCounter < 59) {
                            if (cellCounter == 1) {
                                if(cellValue.length() > 6) {
                                    ArrayList<String> currentLine = new ArrayList<>();
                                    studentLines.add(currentLine);
                                }
                                else {
                                    break rowloop;
                                }
                            }
                            studentLines.get(rowCounter - 18).add(cellValue);

                        }
                        cellCounter += 1;
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
                    line.removeIf(Objects::isNull);
                }

                elements.removeIf(string -> string.trim().equals(""));
                elements.removeIf(string -> string.trim().equals("null"));


                //toCsv(studentLines);

                for (ArrayList<String> line : studentLines) {
                    for (String item : line) {
                    }
                }


                for (ArrayList<String> studentLine : studentLines) {
                    Student student = new Student();
                    student.setNumero(studentLine.get(0));
                    student.setNom(studentLine.get(1));
                    student.setPrenom(studentLine.get(2));
                    student.setDateNaissance(studentLine.get(3));
                    student.setNoteSemestre(0f);
                    student.setResultatSemestre("");
                    student.setCodeSemestre(semesterService.getAll().get(0).getCode());
                    student.setTitreSemestre(semesterService.getAll().get(0).getTitre());

                    Module module1 = new Module();
                    module1.setCode("");
                    module1.setTitre("");
                    module1.setNote(0f);
                    module1.setResultat("");
                    module1.setElements(new ArrayList<>());

                    Element element1 = new Element();
                    element1.setTitre(elements.get(0).split("-")[1].trim());
                    element1.setCode(elements.get(0).split("-")[0].trim());
                    element1.setNote(Float.parseFloat(studentLine.get(4)));
                    element1.setBareme(Float.parseFloat(studentLine.get(5)));
                    element1.setPonderation(elementService.findByCode(element1.getCode()).getPonderation());
                    module1.getElements().add(element1);


                    markService.save(new Mark(null,
                            element1.getNote(), "S2", elementService.findByCode(element1.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));

                    Element element2 = new Element();
                    element2.setTitre(elements.get(1).split("-")[1].trim());
                    element2.setCode(elements.get(1).split("-")[0].trim());
                    element2.setNote(Float.parseFloat(studentLine.get(6).replace(",", ".")));
                    element2.setBareme(Float.parseFloat(studentLine.get(7)));
                    element2.setPonderation(elementService.findByCode(element2.getCode()).getPonderation());
                    module1.getElements().add(element2);

                    markService.save(new Mark(null,
                            element2.getNote(), "S2", elementService.findByCode(element2.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module1.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element1.getCode(), element1.getTitre(), 0f, 0f, null, null)).getCode());
                    module1.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element1.getCode(), element1.getTitre(), 0f, 0f, null, null)).getTitre());
                    //

                    Module module2 = new Module();
                    module2.setCode("");
                    module2.setTitre("");
                    module2.setNote(0f);
                    module2.setResultat("");
                    module2.setElements(new ArrayList<>());


                    Element element3 = new Element();
                    element3.setTitre(elements.get(2).split("-")[1].trim());
                    element3.setCode(elements.get(2).split("-")[0].trim());
                    element3.setNote(Float.parseFloat(studentLine.get(8).replace(",", ".")));
                    element3.setBareme(Float.parseFloat(studentLine.get(9)));
                    element3.setPonderation(elementService.findByCode(element3.getCode()).getPonderation());
                    module2.getElements().add(element3);

                    markService.save(new Mark(null,
                            element3.getNote(), "S2", elementService.findByCode(element3.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));

                    Element element4 = new Element();
                    element4.setTitre(elements.get(3).split("-")[1].trim());
                    element4.setCode(elements.get(3).split("-")[0].trim());
                    element4.setNote(Float.parseFloat(studentLine.get(10).replace(",", ".").equals("ABI") ? "0" : studentLine.get(10).replace(",", ".")));
                    element4.setBareme(Float.parseFloat(studentLine.get(11).equals("null") ? "20" : "20"));
                    element4.setPonderation(elementService.findByCode(element4.getCode()).getPonderation());
                    module2.getElements().add(element4);

                    markService.save(new Mark(null,
                            element4.getNote(), "S2", elementService.findByCode(element4.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module2.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element3.getCode(), element3.getTitre(), 0f, 0f, null, null)).getCode());
                    module2.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element3.getCode(), element3.getTitre(), 0f, 0f, null, null)).getTitre());
                    //


                    Module module3 = new Module();
                    module3.setCode("");
                    module3.setTitre("");
                    module3.setNote(0f);
                    module3.setResultat("");
                    module3.setElements(new ArrayList<>());


                    Element element5 = new Element();
                    element5.setTitre(elements.get(4).split("-")[1].trim());
                    element5.setCode(elements.get(4).split("-")[0].trim());
                    element5.setNote(Float.parseFloat(studentLine.get(12).replace(",", ".")));
                    element5.setBareme(Float.parseFloat(studentLine.get(13)));
                    element5.setPonderation(elementService.findByCode(element5.getCode()).getPonderation());
                    module3.getElements().add(element5);

                    markService.save(new Mark(null,
                            element5.getNote(), "S2", elementService.findByCode(element5.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));

                    Element element6 = new Element();
                    element6.setTitre(elements.get(5).split("-")[1].trim());
                    element6.setCode(elements.get(5).split("-")[0].trim());
                    element6.setNote(Float.parseFloat(studentLine.get(14).replace(",", ".")));
                    element6.setBareme(Float.parseFloat(studentLine.get(15)));
                    element6.setPonderation(elementService.findByCode(element6.getCode()).getPonderation());
                    module3.getElements().add(element6);

                    markService.save(new Mark(null,
                            element6.getNote(), "S2", elementService.findByCode(element6.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module3.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element5.getCode(), element5.getTitre(), 0f, 0f, null, null)).getCode());
                    module3.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element5.getCode(), element5.getTitre(), 0f, 0f, null, null)).getTitre());
                    //


                    Module module4 = new Module();
                    module4.setCode("");
                    module4.setTitre("");
                    module4.setNote(0f);
                    module4.setResultat("");
                    module4.setElements(new ArrayList<>());


                    Element element7 = new Element();
                    element7.setTitre(elements.get(6).split("-")[1].trim());
                    element7.setCode(elements.get(6).split("-")[0].trim());
                    element7.setNote(Float.parseFloat(studentLine.get(16).replace(",", ".")));
                    element7.setBareme(Float.parseFloat(studentLine.get(17)));
                    element7.setPonderation(elementService.findByCode(element7.getCode()).getPonderation());
                    module4.getElements().add(element7);

                    markService.save(new Mark(null,
                            element7.getNote(), "S2", elementService.findByCode(element7.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    Element element8 = new Element();
                    element8.setTitre(elements.get(7).split("-")[1].trim());
                    element8.setCode(elements.get(7).split("-")[0].trim());
                    element8.setNote(Float.parseFloat(studentLine.get(18).replace(",", ".")));
                    element8.setBareme(Float.parseFloat(studentLine.get(19)));
                    element8.setPonderation(elementService.findByCode(element8.getCode()).getPonderation());
                    module4.getElements().add(element8);

                    markService.save(new Mark(null,
                            element8.getNote(), "S2", elementService.findByCode(element8.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));

                    //
                    module4.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element7.getCode(), element7.getTitre(), 0f, 0f, null, null)).getCode());
                    module4.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element7.getCode(), element7.getTitre(), 0f, 0f, null, null)).getTitre());
                    //


                    Module module5 = new Module();
                    module5.setCode("");
                    module5.setTitre("");
                    module5.setNote(0f);
                    module5.setResultat("");
                    module5.setElements(new ArrayList<>());

                    Element element9 = new Element();
                    element9.setTitre(elements.get(8).split("-")[1].trim());
                    element9.setCode(elements.get(8).split("-")[0].trim());
                    element9.setNote(Float.parseFloat(studentLine.get(20).replace(",", ".")));
                    element9.setBareme(Float.parseFloat(studentLine.get(21)));
                    element9.setPonderation(elementService.findByCode(element9.getCode()).getPonderation());
                    module5.getElements().add(element9);

                    markService.save(new Mark(null,
                            element9.getNote(), "S2", elementService.findByCode(element9.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));

                    Element element10 = new Element();
                    element10.setTitre(elements.get(9).split("-")[1].trim());
                    element10.setCode(elements.get(9).split("-")[0].trim());
                    element10.setNote(Float.parseFloat(studentLine.get(22).replace(",", ".")));
                    element10.setBareme(Float.parseFloat(studentLine.get(23)));
                    element10.setPonderation(elementService.findByCode(element10.getCode()).getPonderation());
                    module5.getElements().add(element10);

                    markService.save(new Mark(null,
                            element10.getNote(), "S2", elementService.findByCode(element10.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module5.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element9.getCode(), element9.getTitre(), 0f, 0f, null, null)).getCode());
                    module5.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element9.getCode(), element9.getTitre(), 0f, 0f, null, null)).getTitre());
                    //


                    Module module6 = new Module();
                    module6.setCode("");
                    module6.setTitre("");
                    module6.setNote(0f);
                    module6.setResultat("");
                    module6.setElements(new ArrayList<>());

                    Element element11 = new Element();
                    element11.setTitre(elements.get(10).split("-")[1].trim());
                    element11.setCode(elements.get(10).split("-")[0].trim());
                    element11.setNote(Float.parseFloat(studentLine.get(24).replace(",", ".")));
                    element11.setBareme(Float.parseFloat(studentLine.get(25)));
                    element11.setPonderation(elementService.findByCode(element11.getCode()).getPonderation());
                    module6.getElements().add(element11);

                    markService.save(new Mark(null,
                            element11.getNote(), "S2", elementService.findByCode(element11.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    Element element12 = new Element();
                    element12.setTitre(elements.get(11).split("-")[1].trim());
                    element12.setCode(elements.get(11).split("-")[0].trim());
                    element12.setNote(Float.parseFloat(studentLine.get(26).replace(",", ".")));
                    element12.setBareme(Float.parseFloat(studentLine.get(27)));
                    element12.setPonderation(elementService.findByCode(element12.getCode()).getPonderation());
                    module6.getElements().add(element12);

                    markService.save(new Mark(null,
                            element12.getNote(), "S2", elementService.findByCode(element12.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module6.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element11.getCode(), element11.getTitre(), 0f, 0f, null, null)).getCode());
                    module6.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element11.getCode(), element11.getTitre(), 0f, 0f, null, null)).getTitre());
                    //

                    Module module7 = new Module();
                    module7.setCode("");
                    module7.setTitre("");
                    module7.setNote(0f);
                    module7.setResultat("");
                    module7.setElements(new ArrayList<>());

                    Element element13 = new Element();
                    element13.setTitre(elements.get(12).split("-")[1].trim());
                    element13.setCode(elements.get(12).split("-")[0].trim());
                    element13.setNote(Float.parseFloat(studentLine.get(28).replace(",", ".")));
                    element13.setBareme(Float.parseFloat(studentLine.get(29)));
                    element13.setPonderation(elementService.findByCode(element13.getCode()).getPonderation());
                    module7.getElements().add(element13);

                    markService.save(new Mark(null,
                            element13.getNote(), "S2", elementService.findByCode(element13.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    Element element14 = new Element();
                    element14.setTitre(elements.get(13).split("-")[1].trim());
                    element14.setCode(elements.get(13).split("-")[0].trim());
                    element14.setNote(Float.parseFloat(studentLine.get(30).replace(",", ".")));
                    element14.setBareme(Float.parseFloat(studentLine.get(31)));
                    element14.setPonderation(elementService.findByCode(element14.getCode()).getPonderation());
                    module7.getElements().add(element14);

                    markService.save(new Mark(null,
                            element14.getNote(), "S2", elementService.findByCode(element14.getCode()),
                            studentService.findById(Long.parseLong(studentLine.get(0)))));


                    //
                    module7.setCode(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element13.getCode(), element13.getTitre(), 0f, 0f, null, null)).getCode());
                    module7.setTitre(moduleService.findByElement(new ma.ensetm.project.
                            entities.Element(null, element13.getCode(), element13.getTitre(), 0f, 0f, null, null)).getTitre());
                    //

                    student.setModules(new ArrayList<>());
                    student.getModules().add(module1);
                    student.getModules().add(module2);
                    student.getModules().add(module3);
                    student.getModules().add(module4);
                    student.getModules().add(module5);
                    student.getModules().add(module6);
                    student.getModules().add(module7);

                    output.getStudents().add(student);
                }

                //file.delete();


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

    @PostMapping("/export-s1s2")
    public Map<String, Object> exportForS1s2(@RequestBody Data output, @RequestParam(required = false, defaultValue = "true") String avecElement) {

        boolean genererAvecElements = !avecElement.toLowerCase(Locale.ROOT).equals("false");
        int initalRow = 17;
        int initalCol = 4;

        FileInputStream theSavedFile = null;
        try {
            theSavedFile = new FileInputStream(new File(new File("").getAbsolutePath() + "/templates/TemplateS1S2.xls"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(theSavedFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HSSFSheet sheet = workbook.getSheetAt(0);
        sheet.setGridsPrinted(true);

        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);

        CellStyle wrap = workbook.createCellStyle();
        wrap.setWrapText(true);
        wrap.setVerticalAlignment(VerticalAlignment.CENTER);
        wrap.setAlignment(HorizontalAlignment.CENTER);

        List<Student> lignes = output.getStudents();
        List<Module> elementsModules = new ArrayList<>();

        int headStart = 4;

        List<Module> modules = output.getStudents().get(0).getModules();

        Row headRow = sheet.createRow(13);
        Row sessionRow = sheet.createRow(14);
        Row coefRow = sheet.createRow(15);
        Cell cell01 = coefRow.createCell(0);
        cell01.setCellValue("Etudiant");
        cell01.setCellStyle(style);

        Cell cell02 = coefRow.createCell(3);
        cell02.setCellValue("Admissibilité");
        cell02.setCellStyle(style);

        Row noteAndResltRow = sheet.createRow(16);

        headRow.setHeight((short) 1000);

        Cell cell1 = noteAndResltRow.createCell(0);
        cell1.setCellValue("Numero");
        cell1.setCellStyle(style);

        Cell cell2 = noteAndResltRow.createCell(1);
        cell2.setCellValue("Nom");
        cell2.setCellStyle(style);


        Cell cell3 = noteAndResltRow.createCell(2);
        cell3.setCellValue("Prenom");
        cell3.setCellStyle(style);

        Cell cell4 = noteAndResltRow.createCell(3);
        cell4.setCellValue("Naissance");
        cell4.setCellStyle(style);


        for (int i = 0; i < modules.size(); i++) {
            int startmerge = headStart;


            List<Element> elements = modules.get(i).getElements();
            if (genererAvecElements) {
                for (int j = 0; j < elements.size(); j++) {
                    startmerge = headStart;

                    Cell note = noteAndResltRow.createCell(headStart);
                    note.setCellValue("Note");
                    note.setCellStyle(style);

                    Cell cell03 = noteAndResltRow.createCell(headStart + 1);
                    cell03.setCellValue("Bareme");
                    cell03.setCellStyle(style);

                    Cell cell04 = sessionRow.createCell(headStart);
                    cell04.setCellValue(2);
                    cell04.setCellStyle(style);

                    Cell cell05 = sessionRow.createCell(headStart + 1);
                    cell05.setCellValue(2);
                    cell05.setCellStyle(style);

                    Cell elemCell = headRow.createCell(headStart++);
                    elemCell.setCellStyle(wrap);
                    elemCell.setCellValue(elements.get(j).getCode() + " -\n " + elements.get(j).getTitre());

                    elemCell.setCellStyle(style);
                    headStart++;
                    sheet.addMergedRegion(new CellRangeAddress(13, 13, startmerge, startmerge + 1));
                }
            }
        }

        for (int i = 0; i < lignes.size(); i++) {
            int currentCellIndex = initalCol;

            Row row = sheet.createRow(initalRow + i);

            Cell cell06 = row.createCell(0);
            cell06.setCellValue(String.valueOf(lignes.get(i).getNumero()));
            cell06.setCellStyle(style);

            Cell cell07 = row.createCell(1);
            cell07.setCellValue(lignes.get(i).getNom());
            cell07.setCellStyle(style);
            sheet.setColumnWidth(cell07.getColumnIndex(), 5000);


            Cell cell08 = row.createCell(2);
            cell08.setCellValue(lignes.get(i).getPrenom());
            cell08.setCellStyle(style);
            sheet.setColumnWidth(cell08.getColumnIndex(), 5000);


            Cell cell09 = row.createCell(3);
            cell09.setCellValue(lignes.get(i).getDateNaissance());
            cell09.setCellStyle(style);

            Cell cellNoteSemestre = row.createCell(4);
            cellNoteSemestre.setCellValue(String.valueOf(lignes.get(i).getNoteSemestre()));
            cellNoteSemestre.setCellStyle(style);

            Cell cellResultatSemestre = row.createCell(5);
            cellResultatSemestre.setCellValue(lignes.get(i).getResultatSemestre());
            cellResultatSemestre.setCellStyle(style);

            List<Module> etudiantModules = lignes.get(i).getModules();

            for (Module etudiantModule : etudiantModules) {

                if (genererAvecElements) {
                    List<Element> etudiantElementsModules = etudiantModule.getElements();

                    for (Element etudiantElementsModule : etudiantElementsModules) {

                        Cell cellNoteElement = row.createCell(currentCellIndex++);
                        cellNoteElement.setCellValue(String.valueOf(etudiantElementsModule.getNote()));

                        Cell cellBaremeElement = row.createCell(currentCellIndex++);
                        cellBaremeElement.setCellValue(20);

                        cellBaremeElement.setCellStyle(style);
                        cellNoteElement.setCellStyle(style);
                    }
                }
            }

        }

        Map<String, Object> data = new HashMap<>();

        try {
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
            String name = "S1S2-" + dateFormat.format(new Date()) + ".xls";
            try (FileOutputStream out = new FileOutputStream(new File("files/" + name))) {
                workbook.write(out);
                workbook.close();
                data.put("status", 1);
                data.put("name", name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
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
}
