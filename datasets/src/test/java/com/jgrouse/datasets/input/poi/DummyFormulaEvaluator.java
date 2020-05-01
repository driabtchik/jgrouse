package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;

import java.util.Map;

@SuppressWarnings("deprecated")
public class DummyFormulaEvaluator implements FormulaEvaluator {
    @Override
    public void clearAllCachedResultValues() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifySetFormula(final Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifyDeleteCell(final Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void notifyUpdateCell(final Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void evaluateAll() {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellValue evaluate(final Cell cell) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CellType evaluateFormulaCell(final Cell cell) {
        return CellType.STRING;
    }

    @Override
    @SuppressWarnings("deprecation")
    public CellType evaluateFormulaCellEnum(final Cell cell) {
        return CellType.STRING;
    }

    @Override
    public Cell evaluateInCell(final Cell cell) {
        return null;
    }

    @Override
    public void setupReferencedWorkbooks(final Map<String, FormulaEvaluator> workbooks) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setIgnoreMissingWorkbooks(final boolean ignore) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setDebugEvaluationOutputForNextEval(final boolean value) {
        throw new UnsupportedOperationException();
    }
}
