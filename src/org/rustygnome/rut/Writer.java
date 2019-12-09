package org.rustygnome.rut;

import com.sun.istack.internal.NotNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class Writer {

    static final String DOCUMENT_FILE_PATH = "/home/arkascha/Projects/RuT-Extractor/work/artefacts/contact_form.xlsx";
    static final String DOCUMENT_SHEET_NAME = "Kontaktformular";

    private String filePath;
    private String sheetName;

    static public Writer getInstance() throws InstantiationException, IllegalAccessException {
        return Factory.getInstance(Writer.class).create();
    }

    public Writer() {
        this.filePath = DOCUMENT_FILE_PATH;
        this.sheetName = DOCUMENT_SHEET_NAME;
    }

    public boolean write(@NotNull Values values) throws IOException {
        XSSFWorkbook document = openDocument();
        XSSFSheet sheet = document.getSheet(sheetName);

        boolean written = createRow(sheet, values);
        writeDocument(document, filePath);
        return written;
    }

    private XSSFWorkbook openDocument() {
        boolean freshDocument = !(new File(filePath)).exists();

        XSSFWorkbook document = new XSSFWorkbook();
        if (freshDocument || document.getSheet(sheetName) == null) {
            XSSFSheet sheet = document.createSheet(sheetName);
            createRow(sheet, Values.getTitles());
        }

        return document;
    }

    private boolean createRow(@NotNull XSSFSheet sheet, @NotNull Values values) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);

        if (!rowExists(sheet, values)) {
            int cellNum = 0;
            for (Values.Entry entry : values.entrySet()) {
                row.createCell(cellNum++).setCellValue(entry.getValue());
            }
            return true;
        }
        return false;
    }

    private boolean rowExists(@NotNull XSSFSheet sheet, @NotNull Values values) {
        String[] strings = values.toArray();

        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            boolean allCellsInRowMatch = true;
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                if (!cell.getStringCellValue().equals(strings[cell.getColumnIndex()])) {
                    allCellsInRowMatch = false;
                    break;
                }
            }
            if (allCellsInRowMatch) {
                return true;
            }
        }
        return false;
    }

    private void writeDocument(@NotNull XSSFWorkbook document, @NotNull String filePath) throws IOException {
        FileOutputStream stream = new FileOutputStream(new File(filePath), true);
        document.write(stream);
        stream.close();
    }
}
