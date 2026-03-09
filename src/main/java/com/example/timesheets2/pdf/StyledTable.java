package com.example.timesheets2.pdf;

import org.openpdf.text.Phrase;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfPTable;

import java.awt.*;

public class StyledTable {
    static final Color lightBlue = new Color(100, 149, 227);
    int rows;
    int currentRow = 1;
    RowType currentRowType;
    PdfPTable table;

    public StyledTable(float[] floats) {
        table = new PdfPTable(floats);
        table.setHeaderRows(1);
        rows = floats.length;
        currentRowType = RowType.HEADER;
    }

    public void addCell(String text) {
        addCell(new Phrase(text));
    }

    public void addCell(Phrase phrase) {
        var cell = new PdfPCell(phrase);
        cell.setPaddingBottom(4.0f);
        switch (currentRowType) {
            case HEADER, FOOTER -> cell.setBackgroundColor(lightBlue);
        }
        table.addCell(cell);
        currentRow += 1;
        if (currentRow > rows) {
            currentRow = 1;
            table.completeRow();
        }
    }

    public void endHeader() {
        if (currentRowType == RowType.HEADER) currentRowType = RowType.NORMAL;
    }

    public void startFooter() {
        if (currentRowType == RowType.NORMAL) currentRowType = RowType.FOOTER;
    }

    public PdfPTable toPdfPTable() {
        return table;
    }

    enum RowType {
        HEADER, NORMAL, FOOTER
    }

}
