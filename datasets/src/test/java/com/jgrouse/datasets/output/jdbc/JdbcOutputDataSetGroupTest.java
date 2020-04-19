package com.jgrouse.datasets.output.jdbc;

import com.jgrouse.datasets.DataSetMetadata;
import com.jgrouse.datasets.DataSetMetadataElement;
import com.jgrouse.datasets.input.InputDataSet;
import com.jgrouse.util.collections.MapBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.jgrouse.util.jdbc.JdbcRuntimeException.asUnchecked;
import static com.jgrouse.util.jdbc.JdbcRuntimeException.unchecked;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class JdbcOutputDataSetGroupTest {

    private final SingleConnectionTrackingResourcesDatabase dataSource = new SingleConnectionTrackingResourcesDatabase();
    private final JdbcOutputDataSetGroup jdbcOutputDataSetGroup = new JdbcOutputDataSetGroup(dataSource);
    private final InputDataSet dataset1 = mock(InputDataSet.class);
    private final InputDataSet dataset2 = mock(InputDataSet.class);

    private Connection connection;

    @BeforeEach
    void before() throws SQLException {
        dataSource.setURL("jdbc:h2:mem:JdbcOutputDataSetGroupTest");
        connection = dataSource.getConnection();
        executeSql("create table t1 (f1 integer, f2 varchar)");
        executeSql("create table t2 (f3 decimal)");

        when(dataset1.getMetadata()).thenReturn(new DataSetMetadata(asList(
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
    void after() throws SQLException {
        connection.close();
        dataSource.assertNoLeaks();
    }

    @Test
    void save_normal() {
        final List<InputDataSet> dataSets = asList(dataset1, dataset2);
        jdbcOutputDataSetGroup.save(dataSets);

        when(dataset1.stream()).thenReturn(
                Stream.of(
                        asList(42, "foo"),
                        asList(77, "bar")
                )
        );

        assertThat(loadFromSql("select f1, f2 from t1")).containsExactly(
                MapBuilder.<String, Object>from("f1", 42).map("f2", "foo").build(),
                MapBuilder.<String, Object>from("f1", 77).map("f2", "bar").build()
        );
    }


    private void executeSql(final String sql) {
        unchecked(() -> {
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
        unchecked(() -> {
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