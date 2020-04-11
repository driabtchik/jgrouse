package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.PaneInformation;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DummySheet implements Sheet {
    private final String name;
    private final List<Row> rows;
    private Workbook workbook;

    public DummySheet(String name, List<Row> rows) {
        this.name = name;
        this.rows = rows;
        int rowNum = 0;
        for (Row row : rows) {
            if (row != null) {
                ((DummyRow) row).fromSheet(this).fromRowNum(rowNum);
            }
            rowNum++;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public DummySheet fromWorkbook(Workbook workbook) {
        this.workbook = workbook;
        return this;
    }

    @Override
    public Row createRow(int rowNum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRow(Row row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Row getRow(int rowNum) {
        return rows.get(rowNum);
    }

    @Override
    public void shiftColumns(int startColumn, int endColumn, int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPhysicalNumberOfRows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getFirstRowNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getLastRowNum() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnHidden(int columnIndex, boolean hidden) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isColumnHidden(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRightToLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRightToLeft(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnWidth(int columnIndex, int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnWidth(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getColumnWidthInPixels(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDefaultColumnWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultColumnWidth(int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getDefaultRowHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultRowHeight(short height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getDefaultRowHeightInPoints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultRowHeightInPoints(float height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getColumnStyle(int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addMergedRegion(CellRangeAddress region) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addMergedRegionUnsafe(CellRangeAddress region) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validateMergedRegions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getHorizontallyCenter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHorizontallyCenter(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getVerticallyCenter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVerticallyCenter(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMergedRegion(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMergedRegions(Collection<Integer> indices) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumMergedRegions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getMergedRegion(int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<CellRangeAddress> getMergedRegions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Row> rowIterator() {
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
    public boolean isDisplayZeros() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayZeros(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAutobreaks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutobreaks(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getDisplayGuts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayGuts(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getFitToPage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFitToPage(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRowSumsBelow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowSumsBelow(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRowSumsRight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowSumsRight(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrintGridlines() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintGridlines(boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrintRowAndColumnHeadings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintRowAndColumnHeadings(boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PrintSetup getPrintSetup() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Header getHeader() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Footer getFooter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public double getMargin(short margin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMargin(short margin, double size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getProtect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void protectSheet(String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getScenarioProtect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setZoom(int scale) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getTopRow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getLeftCol() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void showInPane(int topRow, int leftCol) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftRows(int startRow, int endRow, int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftRows(int startRow, int endRow, int n, boolean copyRowHeight, boolean resetOriginalRowHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFreezePane(int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFreezePane(int colSplit, int rowSplit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createSplitPane(int xSplitPos, int ySplitPos, int leftmostColumn, int topRow, int activePane) {
        throw new UnsupportedOperationException();
    }

    @Override
    public PaneInformation getPaneInformation() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayGridlines() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayGridlines(boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayFormulas() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayFormulas(boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayRowColHeadings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayRowColHeadings(boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowBreak(int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRowBroken(int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRowBreak(int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getRowBreaks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int[] getColumnBreaks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnBreak(int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isColumnBroken(int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeColumnBreak(int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnGroupCollapsed(int columnNumber, boolean collapsed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void groupColumn(int fromColumn, int toColumn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ungroupColumn(int fromColumn, int toColumn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void groupRow(int fromRow, int toRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ungroupRow(int fromRow, int toRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowGroupCollapsed(int row, boolean collapse) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultColumnStyle(int column, CellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void autoSizeColumn(int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void autoSizeColumn(int column, boolean useMergedCells) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comment getCellComment(CellAddress ref) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<CellAddress, ? extends Comment> getCellComments() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Drawing<?> getDrawingPatriarch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Drawing<?> createDrawingPatriarch() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Workbook getWorkbook() {
        return workbook;
    }

    @Override
    public String getSheetName() {
        return name;
    }

    @Override
    public boolean isSelected() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setSelected(boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRange<? extends Cell> setArrayFormula(String formula, CellRangeAddress range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRange<? extends Cell> removeArrayFormula(Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataValidationHelper getDataValidationHelper() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends DataValidation> getDataValidations() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void addValidationData(DataValidation dataValidation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AutoFilter setAutoFilter(CellRangeAddress range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public SheetConditionalFormatting getSheetConditionalFormatting() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getRepeatingRows() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRepeatingRows(CellRangeAddress rowRangeRef) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getRepeatingColumns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRepeatingColumns(CellRangeAddress columnRangeRef) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnOutlineLevel(int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink(int row, int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink(CellAddress address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<? extends Hyperlink> getHyperlinkList() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellAddress getActiveCell() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setActiveCell(CellAddress address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }
}