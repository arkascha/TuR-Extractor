package org.rustygnome.tur.artefact;

import com.sun.istack.internal.NotNull;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.Iterator;

public class Writer {

    static final String DOCUMENT_FILE_PATH = "/home/arkascha/Projects/TuR-Extractor/work/artefacts/contact_form.xlsx";
    static final String DOCUMENT_SHEET_NAME = "Kontaktformular";

    private String filePath;
    private String sheetName;

    static public Writer getInstance() throws InstantiationException, IllegalAccessException {
        return Factory.getInstance(Writer.class).createArtefact();
    }

    public Writer() {
        this.filePath = DOCUMENT_FILE_PATH;
        this.sheetName = DOCUMENT_SHEET_NAME;
    }

    public Boolean write(@NotNull Values values)
            throws IOException {
        Boolean exported = false;

        XSSFWorkbook document = openDocument();
        XSSFSheet sheet = document.getSheet(sheetName);
        if (!rowExists(sheet, values)) {
            createRow(sheet, values);
            writeDocument(document, filePath);
            exported = true;
        }

        document.close();
        return exported;
    }

    private XSSFWorkbook openDocument()
            throws IOException {
        XSSFWorkbook document;

        if ((new File(filePath)).exists()) {
            FileInputStream file = new FileInputStream(new File(filePath));
            document = new XSSFWorkbook(file);
            file.close();
            if (document.getSheet(sheetName) == null) {
                createSheet(document, sheetName);
            }
        } else {
            document = new XSSFWorkbook();
            createSheet(document, sheetName);
        }

        return document;
    }

    private void createSheet(XSSFWorkbook document, String sheetName) {
        XSSFSheet sheet = document.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for (Values.Entry entry : Values.getTitles().entrySet()) {
            row.createCell(entry.getKey().getIndex()).setCellValue(entry.getValue());
        }
    }

    private void createRow(@NotNull XSSFSheet sheet, @NotNull Values values) {
        Row row = sheet.createRow(sheet.getLastRowNum() + 1);
        for (Values.Entry entry : values.entrySet()) {
            row.createCell(entry.getKey().getIndex()).setCellValue(entry.getValue());
        }
    }

    private boolean rowExists(@NotNull XSSFSheet sheet, @NotNull Values values)
            throws UnexpectedException {

        // check for the title row
        Iterator<Row> rowIterator = sheet.rowIterator();
        if ( ! rowIterator.hasNext()) {
            throw new UnexpectedException("Sheet does not have a title row");
        }

        // does the first row hold the column titles
        Row row = rowIterator.next();
        if ( ! rowHoldsValues(row, Values.getTitles())) {
            throw new UnexpectedException("Sheet does not hold the expected columns");
        }

        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            if (rowHoldsValues(row, values)) {
                return true;
            }
        }

        return false;
    }

    private boolean rowHoldsValues(@NotNull Row row, @NotNull Values values) {
        Iterator<Values.Entry> iterator = values.entrySet().iterator();
        while (iterator.hasNext()) {
            Values.Entry entry = iterator.next();
            Cell cell = row.getCell(entry.getKey().getIndex());
            if (cell == null || ! entry.getValue().equals(cell.getStringCellValue()))
                return false;
        }
        return true;
    }

    private void writeDocument(@NotNull XSSFWorkbook document, @NotNull String filePath)
            throws IOException {
        FileOutputStream stream = new FileOutputStream(new File(filePath), true);
        document.write(stream);
        stream.flush();
        stream.close();
    }
}
