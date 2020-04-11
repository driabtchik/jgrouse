package com.jgrouse.datasets.output.jdbc;

import com.jgrouse.datasets.input.InputDataSet;
import com.jgrouse.datasets.output.OutputDataSetGroup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.List;

import static com.jgrouse.util.jdbc.JdbcRuntimeException.asUnchecked;

public class JdbcOutputDataSetGroup implements OutputDataSetGroup {
    private final DataSource dataSource;

    public JdbcOutputDataSetGroup(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(List<InputDataSet> dataSets) {
        asUnchecked(() -> {
            try (Connection connection = dataSource.getConnection()) {

            }

        });


    }
}
