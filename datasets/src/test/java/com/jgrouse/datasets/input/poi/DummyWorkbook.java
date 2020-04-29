package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;

import java.io.OutputStream;
import java.util.*;

import static org.apache.commons.lang3.Validate.notNull;

public class DummyWorkbook implements Workbook {
    private final List<Sheet> sheets;
    private final Map<String, Sheet> sheetsByName;
    private CreationHelper creationHelper = new DummtCreationHelper();

    public DummyWorkbook(final List<Sheet> sheets) {
        this.sheets = sheets;
        this.sheetsByName = new HashMap<>();
        this.sheets.forEach(sheet -> {
            ((DummySheet) sheet).fromWorkbook(this);
            sheetsByName.put(sheet.getSheetName(), sheet);
        });
    }

    public DummyWorkbook withCreationHelper(final CreationHelper creationHelper) {
        this.creationHelper = creationHelper;
        return this;
    }

    @Override
    public int getActiveSheetIndex() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setActiveSheet(final int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumberOfFontsAsInt() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font getFontAt(final int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFirstVisibleTab() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFirstVisibleTab(final int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetOrder(final String sheetName, final int pos) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSelectedTab(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetName(final int sheet, final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSheetName(final int sheet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSheetIndex(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getSheetIndex(final Sheet sheet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet createSheet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet createSheet(final String sheetName) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Sheet cloneSheet(final int sheetNum) {
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
    public Sheet getSheetAt(final int index) {
        return sheets.get(index);
    }

    @Override
    public Sheet getSheet(final String name) {
        return Objects.requireNonNull(sheetsByName.get(name), () -> "Sheet " + name + " was not found");
    }

    @Override
    public void removeSheetAt(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font createFont() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Font findFont(final boolean bold, final short color, final short fontHeight, final String name, final boolean italic, final boolean strikeout,
                         final short typeOffset, final byte underline) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings({"deprecation"})
    public short getNumberOfFonts() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public Font getFontAt(final short idx) {
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
    public CellStyle getCellStyleAt(final int idx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void write(final OutputStream stream) {
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
    public Name getName(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Name> getNames(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Name> getAllNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public Name getNameAt(final int nameIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Name createName() {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public int getNameIndex(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeName(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("deprecation")
    public void removeName(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeName(final Name name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int linkExternalWorkbook(final String name, final Workbook workbook) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintArea(final int sheetIndex, final String reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintArea(final int sheetIndex, final int startColumn, final int endColumn, final int startRow, final int endRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getPrintArea(final int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removePrintArea(final int sheetIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Row.MissingCellPolicy getMissingCellPolicy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMissingCellPolicy(final Row.MissingCellPolicy missingCellPolicy) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataFormat createDataFormat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addPicture(final byte[] pictureData, final int format) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends PictureData> getAllPictures() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CreationHelper getCreationHelper() {
        return notNull(creationHelper);
    }

    @Override
    public boolean isHidden() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHidden(final boolean hiddenFlag) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSheetHidden(final int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSheetVeryHidden(final int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetHidden(final int sheetIx, final boolean hidden) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SheetVisibility getSheetVisibility(final int sheetIx) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSheetVisibility(final int sheetIx, final SheetVisibility visibility) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addToolPack(final UDFFinder toolPack) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getForceFormulaRecalculation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setForceFormulaRecalculation(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SpreadsheetVersion getSpreadsheetVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addOlePackage(final byte[] oleData, final String label, final String fileName, final String command) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Sheet> iterator() {
        return sheets.iterator();
    }
}