package com.jgrouse.datasets.input.poi;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;

public class DummtCreationHelper implements CreationHelper {
    @Override
    public RichTextString createRichTextString(final String text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public DataFormat createDataFormat() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Hyperlink createHyperlink(final HyperlinkType type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public FormulaEvaluator createFormulaEvaluator() {
        return new DummyFormulaEvaluator();
    }

    @Override
    public ExtendedColor createExtendedColor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public ClientAnchor createClientAnchor() {
        throw new UnsupportedOperationException();
    }

    @Override
    public AreaReference createAreaReference(final String reference) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AreaReference createAreaReference(final CellReference topLeft, final CellReference bottomRight) {
        throw new UnsupportedOperationException();
    }
}
