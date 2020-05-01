package com.jgrouse.datasets.input.poi;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.assertj.core.api.WithAssertions;
import org.junit.jupiter.api.Test;

import java.sql.Date;
import java.util.Calendar;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SuppressWarnings("deprecation")
class CellProxyTest implements WithAssertions {

    private final Cell delegate = mock(Cell.class);
    private final CellProxy cellProxy = new CellProxy(delegate, CellType.STRING);

    @Test
    void overriddenMethods() {
        assertThat(cellProxy.getCellType()).isEqualTo(CellType.STRING);
        assertThat(cellProxy.getCellTypeEnum()).isEqualTo(CellType.STRING);
    }

    @Test
    void delegationSetters() {
        assertSetterThrowsException(Cell::setCellComment, mock(Comment.class));
        assertSetterThrowsException(Cell::setCellErrorValue, (byte) 2);
        assertSetterThrowsException(Cell::setCellType, CellType.NUMERIC);
        assertSetterThrowsException(Cell::setCellFormula, "formula");
        assertSetterThrowsException(Cell::setCellStyle, mock(CellStyle.class));
        assertSetterThrowsException(Cell::setCellValue, 42.0);
        assertSetterThrowsException(Cell::setCellValue, Date.valueOf("2020-04-30"));
        assertSetterThrowsException(Cell::setCellValue, mock(Calendar.class));
        assertSetterThrowsException(Cell::setCellValue, mock(RichTextString.class));
        assertSetterThrowsException(Cell::setCellValue, "foobar");
        assertSetterThrowsException(Cell::setCellValue, true);
        assertSetterThrowsException(Cell::setHyperlink, mock(Hyperlink.class));
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(cellProxy::setAsActiveCell);
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(cellProxy::removeCellComment);
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(cellProxy::removeHyperlink);
    }

    private <T> void assertSetterThrowsException(final BiConsumer<Cell, T> consumer, final T value) {
        assertThatExceptionOfType(UnsupportedOperationException.class).isThrownBy(() -> consumer.accept(cellProxy, value));
    }


    @Test
    void delegationGetters() {
        assertGetterDelegate(Cell::getAddress, new CellAddress(1, 1));
        assertGetterDelegate(Cell::getArrayFormulaRange, new CellRangeAddress(1, 1, 1, 1));
        assertGetterDelegate(Cell::getBooleanCellValue, true);
        assertGetterDelegate(Cell::getCachedFormulaResultType, CellType.NUMERIC);
        assertGetterDelegate(Cell::getCellComment, mock(Comment.class));
        assertGetterDelegate(Cell::getCellStyle, mock(CellStyle.class));
        assertGetterDelegate(Cell::getCellFormula, "formula");
        assertGetterDelegate(Cell::getColumnIndex, 42);
        assertGetterDelegate(Cell::getDateCellValue, Date.valueOf("2020-04-30"));
        assertGetterDelegate(Cell::getErrorCellValue, (byte) 3);
        assertGetterDelegate(Cell::getHyperlink, mock(Hyperlink.class));
        assertGetterDelegate(Cell::getNumericCellValue, 42.3);
        assertGetterDelegate(Cell::getStringCellValue, "cellValue");
        assertGetterDelegate(Cell::getRichStringCellValue, mock(RichTextString.class));
        assertGetterDelegate(Cell::getRow, mock(Row.class));
        assertGetterDelegate(Cell::getRowIndex, -33);
        assertGetterDelegate(Cell::getSheet, mock(Sheet.class));
        assertGetterDelegate(Cell::getCachedFormulaResultTypeEnum, CellType.NUMERIC);
        assertGetterDelegate(Cell::isPartOfArrayFormulaGroup, true);
    }

    private <T> void assertGetterDelegate(final Function<Cell, T> invoker, final T value) {
        when(invoker.apply(delegate)).thenReturn(value);
        assertThat(invoker.apply(cellProxy)).isEqualTo(value);
    }
}