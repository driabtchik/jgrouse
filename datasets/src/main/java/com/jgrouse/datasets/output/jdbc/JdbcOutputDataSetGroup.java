package com.jgrouse.datasets.output.jdbc;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.InputDataSet;
import com.jgrouse.datasets.output.OutputDataSetGroup;
import com.jgrouse.util.jdbc.JdbcRuntimeException;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import static com.jgrouse.util.jdbc.JdbcRuntimeException.unchecked;
import static java.util.stream.IntStream.range;

public class JdbcOutputDataSetGroup implements OutputDataSetGroup {

    private final DataSource dataSource;

    public JdbcOutputDataSetGroup(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T extends InputDataSet> void save(final List<T> dataSets) {

        try (Connection connection = dataSource.getConnection()) {
            dataSets.forEach(ids -> {
                try (InputDataSetSavingContext context = new InputDataSetSavingContext(connection, ids)) {
                    context.save();
                }
            });
        } catch (SQLException ex) {
            throw new JdbcRuntimeException(ex);
        }
    }

    private static final class InputDataSetSavingContext implements AutoCloseable {
        private final InputDataSet dataSet;
        private final DataSetMetadata metadata;
        private final PreparedStatement preparedStatement;

        private InputDataSetSavingContext(final Connection connection, final InputDataSet dataSet) {
            this.dataSet = dataSet;
            metadata = dataSet.getMetadata();
            preparedStatement = JdbcRuntimeException.asUnchecked(() -> connection.prepareStatement(createSql()));
        }

        private String createSql() {
            String fieldNames = metadata.stream().map(DataSetMetadataElement::getName).collect(Collectors.joining(","));
            return "insert into " + metadata.getName() + "(" + fieldNames + ") values (" +
                    StringUtils.repeat("?", ",", metadata.getElementsCount()) + ")";
        }

        private void save() {
            dataSet.stream().forEach(row -> {
                range(0, metadata.getElementsCount()).forEach(col -> {
                    @NotNull DataSetMetadataElement element = metadata.getElement(col);
                    unchecked(() -> preparedStatement.setObject(col + 1, row.get(col), element.getJdbcType().getVendorTypeNumber()));
                });
                unchecked(preparedStatement::addBatch);
            });
            unchecked(preparedStatement::executeBatch);
        }

        @Override
        public void close() {
            if (preparedStatement != null) {
                unchecked(preparedStatement::close);
            }
        }
    }
}
