package com.jgrouse.datasets.output.jdbc;

import com.jgrouse.datasets.input.InputDataSet;
import com.jgrouse.datasets.output.OutputDataSetGroup;

import javax.sql.DataSource;
import java.util.List;

public class JdbcOutputDataSetGroup implements OutputDataSetGroup {

    @SuppressWarnings({"PMD.SingularField", "PMD.UnusedFormalParameter", "PMD.UncommentedEmptyConstructor"})
    //work in progress
    public JdbcOutputDataSetGroup(final DataSource dataSource) {
    }

    @Override
    public void save(final List<InputDataSet> dataSets) {
        //TODO: implement me
    }
}
