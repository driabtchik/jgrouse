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

    public DummySheet(final String name, final List<Row> rows) {
        this.name = name;
        this.rows = rows;
        int rowNum = 0;
        for (final Row row : rows) {
            if (row != null) {
                ((DummyRow) row).fromSheet(this).fromRowNum(rowNum);
            }
            rowNum++;
        }
    }

    @SuppressWarnings("UnusedReturnValue")
    public DummySheet fromWorkbook(final Workbook workbook) {
        this.workbook = workbook;
        return this;
    }

    @Override
    public Row createRow(final int rowNum) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRow(final Row row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Row getRow(final int rowNum) {
        return rows.get(rowNum);
    }

    @Override
    public void shiftColumns(final int startColumn, final int endColumn, final int n) {
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
    public void setColumnHidden(final int columnIndex, final boolean hidden) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isColumnHidden(final int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRightToLeft() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRightToLeft(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnWidth(final int columnIndex, final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnWidth(final int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getColumnWidthInPixels(final int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getDefaultColumnWidth() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultColumnWidth(final int width) {
        throw new UnsupportedOperationException();
    }

    @Override
    public short getDefaultRowHeight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultRowHeight(final short height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getDefaultRowHeightInPoints() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultRowHeightInPoints(final float height) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellStyle getColumnStyle(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addMergedRegion(final CellRangeAddress region) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int addMergedRegionUnsafe(final CellRangeAddress region) {
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
    public void setHorizontallyCenter(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getVerticallyCenter() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setVerticallyCenter(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMergedRegion(final int index) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeMergedRegions(final Collection<Integer> indices) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getNumMergedRegions() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getMergedRegion(final int index) {
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
    public void setForceFormulaRecalculation(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayZeros() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayZeros(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getAutobreaks() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAutobreaks(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getDisplayGuts() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayGuts(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getFitToPage() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setFitToPage(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRowSumsBelow() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowSumsBelow(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getRowSumsRight() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowSumsRight(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrintGridlines() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintGridlines(final boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isPrintRowAndColumnHeadings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setPrintRowAndColumnHeadings(final boolean show) {
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
    public double getMargin(final short margin) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setMargin(final short margin, final double size) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getProtect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void protectSheet(final String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getScenarioProtect() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setZoom(final int scale) {
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
    public void showInPane(final int topRow, final int leftCol) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftRows(final int startRow, final int endRow, final int n) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void shiftRows(final int startRow, final int endRow, final int n, final boolean copyRowHeight, final boolean resetOriginalRowHeight) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFreezePane(final int colSplit, final int rowSplit, final int leftmostColumn, final int topRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createFreezePane(final int colSplit, final int rowSplit) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void createSplitPane(final int xSplitPos, final int ySplitPos, final int leftmostColumn, final int topRow, final int activePane) {
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
    public void setDisplayGridlines(final boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayFormulas() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayFormulas(final boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isDisplayRowColHeadings() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDisplayRowColHeadings(final boolean show) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowBreak(final int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isRowBroken(final int row) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeRowBreak(final int row) {
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
    public void setColumnBreak(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isColumnBroken(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeColumnBreak(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setColumnGroupCollapsed(final int columnNumber, final boolean collapsed) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void groupColumn(final int fromColumn, final int toColumn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ungroupColumn(final int fromColumn, final int toColumn) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void groupRow(final int fromRow, final int toRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void ungroupRow(final int fromRow, final int toRow) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRowGroupCollapsed(final int row, final boolean collapse) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDefaultColumnStyle(final int column, final CellStyle style) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void autoSizeColumn(final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void autoSizeColumn(final int column, final boolean useMergedCells) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Comment getCellComment(final CellAddress ref) {
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
    public void setSelected(final boolean value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRange<? extends Cell> setArrayFormula(final String formula, final CellRangeAddress range) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRange<? extends Cell> removeArrayFormula(final Cell cell) {
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
    public void addValidationData(final DataValidation dataValidation) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AutoFilter setAutoFilter(final CellRangeAddress range) {
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
    public void setRepeatingRows(final CellRangeAddress rowRangeRef) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellRangeAddress getRepeatingColumns() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setRepeatingColumns(final CellRangeAddress columnRangeRef) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getColumnOutlineLevel(final int columnIndex) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink(final int row, final int column) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink getHyperlink(final CellAddress address) {
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
    public void setActiveCell(final CellAddress address) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<Row> iterator() {
        return rows.iterator();
    }
}