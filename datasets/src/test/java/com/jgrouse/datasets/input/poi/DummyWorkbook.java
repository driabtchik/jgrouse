package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;

import java.io.OutputStream;
import java.util.*;

public class DummyWorkbook implements Workbook {
    private final List<Sheet> sheets;
    private final Map<String, Sheet> sheetsByName;

    public DummyWorkbook(List<Sheet> sheets) {
        this.sheets = sheets;
        this.sheetsByName = new HashMap<>();
        this.sheets.forEach(sheet -> {
            ((DummySheet) sheet).fromWorkbook(this);
            sheetsByName.put(sheet.getSheetName(), sheet);
        });
    }

    @Override
    public int getActiveSheetIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setActiveSheet(int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfFontsAsInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font getFontAt(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFirstVisibleTab() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFirstVisibleTab(int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetOrder(String sheetName, int pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSelectedTab(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetName(int sheet, String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSheetName(int sheet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSheetIndex(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSheetIndex(Sheet sheet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet createSheet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet createSheet(String sheetName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet cloneSheet(int sheetNum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Sheet> sheetIterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfSheets() {
        return sheets.size();
    }

    @Override
    public Sheet getSheetAt(int index) {
        return sheets.get(index);
    }

    @Override
    public Sheet getSheet(String name) {
        return Objects.requireNonNull(sheetsByName.get(name), () -> "Sheet " + name + " was not found");
    }

    @Override
    public void removeSheetAt(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font createFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font findFont(boolean bold, short color, short fontHeight, String name, boolean italic, boolean strikeout,
                         short typeOffset, byte underline) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getNumberOfFonts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font getFontAt(short idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle createCellStyle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumCellStyles() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getCellStyleAt(int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(OutputStream stream) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Name> getNames(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Name> getAllNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name getNameAt(int nameIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name createName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNameIndex(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeName(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeName(Name name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int linkExternalWorkbook(String name, Workbook workbook) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintArea(int sheetIndex, String reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintArea(int sheetIndex, int startColumn, int endColumn, int startRow, int endRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPrintArea(int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePrintArea(int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Row.MissingCellPolicy getMissingCellPolicy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMissingCellPolicy(Row.MissingCellPolicy missingCellPolicy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataFormat createDataFormat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addPicture(byte[] pictureData, int format) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends PictureData> getAllPictures() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreationHelper getCreationHelper() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHidden() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHidden(boolean hiddenFlag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSheetHidden(int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSheetVeryHidden(int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetHidden(int sheetIx, boolean hidden) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SheetVisibility getSheetVisibility(int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetVisibility(int sheetIx, SheetVisibility visibility) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToolPack(UDFFinder toolPack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getForceFormulaRecalculation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setForceFormulaRecalculation(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetVersion getSpreadsheetVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addOlePackage(byte[] oleData, String label, String fileName, String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Sheet> iterator() {
        return sheets.iterator();
    }
}