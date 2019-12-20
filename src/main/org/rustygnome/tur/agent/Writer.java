package org.rustygnome.tur.agent;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.rustygnome.tur.Command;
import org.rustygnome.tur.domain.Values;
import org.rustygnome.tur.factory.Factored;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class Writer
        extends Factored<Writer> {

    static final String TAG = Writer.class.getSimpleName();

    static public Writer getInstance() {
        return (Writer) Factored.getInstance(Writer.class);
    }

    public Writer() {
        super();
    }

    public boolean write(Values values) {
        Logger.getInstance().logDebug(TAG, "writing values");

        if (values != null && Command.hasOption("output")) {
            return writeToXlsx(values);
        }
        return false;
    }

    private boolean writeToXlsx(@NotNull Values values) {
        Logger.getInstance().logDebug(TAG, "writing into spreadsheet document");

        boolean exported = false;

        String outputPath = Command.getOptionValue("output");
        String sheetName = Command.getOptionValue("sheet", "Kontakte");
        XSSFWorkbook document = openDocument(outputPath, sheetName);
        XSSFSheet sheet = document.getSheet(sheetName);

        if (!rowExists(sheet, values)) {
            createRow(sheet, values);
            writeDocument(document, outputPath);
            Logger.getInstance().logDebug(TAG, "wrote into spreadsheet document");
            exported = true;
        }
        try {
            document.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Logger.getInstance().logDebug(TAG, "values " + (exported ? "have" : "have not") + " been exported");
        return exported;
    }

    private XSSFWorkbook openDocument(@NotNull String outputPath, String sheetName) {
        Logger.getInstance().logDebug(TAG, "opening spreadsheet document");

        XSSFWorkbook document;

        if ((new File(outputPath)).exists()) {
            FileInputStream file;

            try {
                file = new FileInputStream(new File(outputPath));
                document = new XSSFWorkbook(file);
                file.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if (document.getSheet(sheetName) == null) {
                createSheet(document, sheetName);
            }
        } else {
            document = new XSSFWorkbook();
            createSheet(document, sheetName);
        }

        Logger.getInstance().logDebug(TAG, "opened spreadsheet document");
        return document;
    }

    private void createSheet(XSSFWorkbook document, String sheetName) {
        Logger.getInstance().logDebug(TAG, "creating new sheet in spreadsheet document");

        XSSFSheet sheet = document.createSheet(sheetName);
        Row row = sheet.createRow(0);
        for (Values.Entry entry : Values.getTitles().entrySet()) {
            row.createCell(entry.getKey().getIndex()).setCellValue(entry.getValue());
        }
    }

    private void createRow(@NotNull XSSFSheet sheet, @NotNull Values values) {
        Logger.getInstance().logDebug(TAG, "creating new row in spreadsheet document");

        Row row = sheet.createRow(sheet.getPhysicalNumberOfRows());
        for (Values.Entry entry : values.entrySet()) {
            row.createCell(entry.getKey().getIndex()).setCellValue(entry.getValue());
        }
    }

    private boolean rowExists(@NotNull XSSFSheet sheet, @NotNull Values values) {

        // check for the title row
        Iterator<Row> rowIterator = sheet.rowIterator();
        if (rowIterator == null || ! rowIterator.hasNext()) {
            throw new RuntimeException("Sheet does not have a rows");
        }

        // does the first row hold the column titles
        Row row = rowIterator.next();
        if ( ! rowHoldsValues(row, Values.getTitles())) {
            throw new RuntimeException("Sheet does not hold the expected columns");
        }

        // check all other rows for a match of _all_ cell values
        while (rowIterator.hasNext()) {
            row = rowIterator.next();
            if (rowHoldsValues(row, values)) {
                return true;
            }
        }

        return false;
    }

    private boolean rowHoldsValues(@NotNull Row row, @NotNull Values values) {
        for (Values.Entry entry : values.entrySet()) {
            Cell cell = row.getCell(entry.getKey().getIndex());
            if (cell == null || !entry.getValue().equals(cell.getStringCellValue())) {
                return false;
            }
        }
        return true;
    }

    private void writeDocument(@NotNull XSSFWorkbook document, @NotNull String filePath) {
        FileOutputStream stream = null;

        try {
            Logger.getInstance().logDebug(TAG, "writing spreadsheet document");

            stream = new FileOutputStream(new File(filePath), false);
            document.write(stream);
            stream.flush();
            stream.close();

            Logger.getInstance().logDebug(TAG, "wrote spreadsheet document");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
