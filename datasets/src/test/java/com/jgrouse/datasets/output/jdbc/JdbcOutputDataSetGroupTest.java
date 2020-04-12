package com.jgrouse.datasets.output.jdbc;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.InputDataSet;
import com.jgrouse.util.jdbc.JdbcRuntimeException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.jgrouse.util.jdbc.JdbcRuntimeException.asUnchecked;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JdbcOutputDataSetGroupTest {

    private final TrackingResourcesDatabase dataSource = new TrackingResourcesDatabase();
    private final JdbcOutputDataSetGroup jdbcOutputDataSetGroup = new JdbcOutputDataSetGroup(dataSource);
    private final InputDataSet dataset1 = mock(InputDataSet.class);
    private final InputDataSet dataset2 = mock(InputDataSet.class);

    @BeforeEach
    void before() {
        dataSource.setURL("jdbc:h2:mem:JdbcOutputDataSetGroupTest");
        executeSql("create table t1 (f1 integer, f2 varchar)");
        executeSql("create table t2 (f3 decimal)");

        when(dataset1.getMetadata()).thenReturn(new DataSetMetadata(Arrays.asList(
                new DataSetMetadataElement("f1", JDBCType.INTEGER),
                new DataSetMetadataElement("f2", JDBCType.VARCHAR)
        ), "T1"));

        when(dataset2.getMetadata()).thenReturn(
                new DataSetMetadata(Collections.singletonList(
                        new DataSetMetadataElement("f3", JDBCType.DECIMAL)
                ), "T2")
        );
    }

    @AfterEach
    void after() {
        dataSource.assertNoLeaks();
    }

    @Test
    void save_normal() {
        final List<InputDataSet> dataSets = Arrays.asList(dataset1, dataset2);
        jdbcOutputDataSetGroup.save(dataSets);
    }


    private void executeSql(final String sql) {
        JdbcRuntimeException.unchecked(() -> {
                    try (final Connection connection = dataSource.getConnection()) {
                        try (final PreparedStatement ps = connection.prepareStatement(sql)) {
                            ps.execute();
                        }
                    }
                }
        );
    }

    private List<Map<String, Object>> loadFromSql(final String sql) {
        final List<Map<String, Object>> result = new ArrayList<>();
        JdbcRuntimeException.unchecked(() -> {
            try (final Connection connection = dataSource.getConnection()) {
                try (final PreparedStatement ps = connection.prepareStatement(sql)) {
                    try (final ResultSet rs = ps.executeQuery()) {
                        final List<String> columnNames = getColumnNames(rs);
                        while (rs.next()) {
                            final Map<String, Object> row = columnNames.stream().collect(Collectors.toMap(c -> c,
                                    c -> asUnchecked(() -> rs.getObject(c))));
                            result.add(row);
                        }
                    }
                }
            }
        });
        return result;
    }

    private List<String> getColumnNames(final ResultSet rs) throws SQLException {
        final ResultSetMetaData meta = rs.getMetaData();
        return IntStream.range(0, meta.getColumnCount())
                .mapToObj(index -> asUnchecked(() -> meta.getColumnName(index + 1)))
                .collect(Collectors.toList());
    }

}