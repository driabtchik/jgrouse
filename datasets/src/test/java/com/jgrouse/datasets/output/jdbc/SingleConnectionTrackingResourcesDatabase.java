package com.jgrouse.datasets.output.jdbc;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SingleConnectionTrackingResourcesDatabase extends JdbcDataSource {
    private final List<PreparedStatement> knownPreparedStatements = new ArrayList<>();
    private Connection knownConnection;
    private int openCount;

    @Override
    public Connection getConnection() throws SQLException {
        return configureConnection();
    }

    private Connection configureConnection() throws SQLException {
        if (knownConnection == null) {
            knownConnection = spy(super.getConnection());
            doAnswer(x -> {
                openCount--;
                if (openCount == 0) {
                    x.callRealMethod();
                    knownConnection = null;
                }
                return null;
            }).when(knownConnection).close();
            when(knownConnection.prepareStatement(anyString()))
                    .thenAnswer(x -> configurePreparedStatement(spy((PreparedStatement) x.callRealMethod())));
        }
        openCount++;
        return knownConnection;
    }

    private PreparedStatement configurePreparedStatement(final PreparedStatement preparedStatement) throws SQLException {
        doAnswer(x -> {
            knownPreparedStatements.remove(preparedStatement);
            return x.callRealMethod();
        }).when(preparedStatement).close();
        return preparedStatement;
    }

    public void assertNoLeaks() {
        assertThat(knownPreparedStatements).isEmpty();
        assertThat(knownConnection).isNull();
    }

}
