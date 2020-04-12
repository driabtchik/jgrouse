package com.jgrouse.datasets.input.poi;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;

import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

import static com.jgrouse.util.Assert.notEmpty;
import static com.jgrouse.util.Assert.notNull;

public class DelegatingMetadataBasedCellValueExtractor implements CellValueExtractor {
    public static final String NULL_VALUE_TOKEN = "<NULL>";

    private final String nullValueToken;
    private final DataSetMetadata metadata;
    private final Collection<PoiTypeConverter<?>> converters;

    public DelegatingMetadataBasedCellValueExtractor(@NotNull final String nullValueToken,
                                                     @NotNull final DataSetMetadata metadata,
                                                     @NotNull final Collection<PoiTypeConverter<?>> converters) {
        this.nullValueToken = notNull(nullValueToken, "nullValueToken must be provided");
        this.metadata = notNull(metadata, "DataSet metadata must be provided");
        this.converters = notEmpty(converters, "type converters must be not empty");
    }

    public DelegatingMetadataBasedCellValueExtractor(@NotNull final DataSetMetadata metadata,
                                                     @NotNull final Collection<PoiTypeConverter<?>> converters) {
        this(NULL_VALUE_TOKEN, metadata, converters);
    }

    @Override
    public Object getCellValue(final Cell cell) {
        notNull(cell, "cell must be not null");
        final DataSetMetadataElement metadataElement = metadata.getElement(cell.getColumnIndex());
        if (cell.getCellType() == CellType.ERROR) {
            throw new PoiParsingException("Cell is an ERROR " + getErrorDetails(cell, metadataElement));
        }
        if (isNullValue(cell)) {
            return handleNullValue(cell, metadataElement);
        }

        final Optional<PoiTypeConverter<?>> matchedConverter = converters.stream()
                .filter(converter -> converter.canConvert(cell.getCellType(), metadataElement.getJdbcType()))
                .findFirst();

        return matchedConverter
                .orElseThrow(() -> new PoiParsingException("Cannot find converter for column " + metadataElement.getName() +
                        getErrorDetails(cell, metadataElement)
                ))
                .convert(cell, metadataElement);

    }

    @SuppressWarnings("SameReturnValue") // intended to return null or throw exception
    private Object handleNullValue(final Cell cell, final DataSetMetadataElement metadataElement) {
        if (metadataElement.isNullable()) {
            return null;
        }
        throw new PoiParsingException("Column declared as not-nullable, but the cell value is <NULL>/blank"
                + getErrorDetails(cell, metadataElement));
    }

    protected boolean isNullValue(final Cell cell) {
        return cell.getCellType() == CellType.STRING && nullValueToken.equals(cell.getStringCellValue());
    }

    private String getErrorDetails(final Cell cell, final DataSetMetadataElement metadataElement) {
        return ": \n\t sheet=" + cell.getRow().getSheet().getSheetName()
                + "\n\t row=" + cell.getRow().getRowNum()
                + "\n\t col=" + cell.getColumnIndex()
                + "\n\t cellType=" + cell.getCellType()
                + "\n\t jdbcType=" + metadataElement.getJdbcType();
    }
}
