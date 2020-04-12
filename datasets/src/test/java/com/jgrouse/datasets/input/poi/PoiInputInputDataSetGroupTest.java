package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.DataSetMetadataFactory;
import com.jgrouse.util.io.IoRuntimeException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.sql.JDBCType;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class PoiInputInputDataSetGroupTest {

    private boolean createWorkbookInvoked;

    private final DataSetMetadata metadata =
            new DataSetMetadata(Collections.singletonList(new DataSetMetadataElement("col1", JDBCType.BIGINT))
                    , "dataSetName");

    private final DataSetMetadataFactory<PoiSheetInputDataSet> datasetMetadataFactory = (ds, name) -> metadata;
    private final CellValueExtractorFactory cellValueExtractorFactory = mock(CellValueExtractorFactory.class);


    @Test
    public void testGet_testIoLifecycle() throws IOException {


        final InputStream inputStream = mock(InputStream.class);

        final Sheet sheet1 = new DummySheet("sheet1", Collections.singletonList(createHeaderRow("foo", "bar")));
        final Sheet sheet2 = new DummySheet("sheet2", Collections.singletonList(createHeaderRow("baz", "moo")));
        final DummyWorkbook workbook = new DummyWorkbook(Arrays.asList(sheet1, sheet2));

        final PoiInputDataSetGroup dataSetGroup =
                new PoiInputDataSetGroup(() -> inputStream, datasetMetadataFactory, cellValueExtractorFactory) {
                    @Override
                    protected Workbook createWorkbook(final InputStream argStream) {
                        assertThat(argStream).isSameAs(inputStream);
                        assertThat(createWorkbookInvoked).isFalse();
                        createWorkbookInvoked = true;
                        return workbook;
                    }
                };
        assertThat(createWorkbookInvoked).isFalse();
        IoRuntimeException.unchecked(() -> verify(inputStream, times(0)).close());

        @NotNull final PoiSheetInputDataSet dataSetSheet2 = dataSetGroup.get("sheet2");
        assertThat(dataSetSheet2.sheet).isSameAs(sheet2);
        assertThat(createWorkbookInvoked).isTrue();
        verify(inputStream).close();
        @NotNull final PoiSheetInputDataSet dataSetSheet1 = dataSetGroup.get("sheet1");
        assertThat(dataSetSheet1.sheet).isSameAs(sheet1);
        verifyNoMoreInteractions(inputStream);
    }

    @Test
    public void testGet_withRealWorksheet() {
        final PoiInputDataSetGroup poiDataSetGroup =
                new PoiInputDataSetGroup(this::getSpreadsheetStream, datasetMetadataFactory, cellValueExtractorFactory);
        final PoiSheetInputDataSet dataSet = poiDataSetGroup.get("table1");
        assertThat(dataSet).isNotNull();
    }

    private InputStream getSpreadsheetStream() {
        return this.getClass().getResourceAsStream("/com/jgrouse/datasets/input/poi/PoiDatasetFactoryTest.xlsx");
    }

    private Row createHeaderRow(final String... headers) {
        return new DummyRow(
                Stream.of(headers)
                        .map(h -> new DummyCell(CellType.STRING)
                                .withValue(h)
                        ).collect(Collectors.toList()));
    }

    @Test
    public void testConstructor_withException() throws IOException {
        final InputStream inputStream = mock(InputStream.class);
        final PoiInputDataSetGroup factory =
                new PoiInputDataSetGroup(() -> inputStream, datasetMetadataFactory, cellValueExtractorFactory) {
                    @Override
                    protected Workbook createWorkbook(final InputStream argStream) {
                        throw new UnsupportedOperationException("for test");
                    }
                };
        assertThatThrownBy(() -> factory.get("foobar")).hasMessageContaining("for test");
        verify(inputStream).close();
    }


}