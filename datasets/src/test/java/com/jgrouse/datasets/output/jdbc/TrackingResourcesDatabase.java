package com.jgrouse.datasets.output.jdbc;

import org.h2.jdbcx.JdbcDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TrackingResourcesDatabase extends JdbcDataSource {
    private final List<Connection> knownConnections = new ArrayList<>();
    private final List<PreparedStatement> knownPreparedStatements = new ArrayList<>();

    @Override
    public Connection getConnection() throws SQLException {
        final Connection connection = configureConnection();
        knownConnections.add(connection);
        return connection;
    }

    private Connection configureConnection() throws SQLException {
        final Connection connection = spy(super.getConnection());
        doAnswer(x -> {
            knownConnections.remove(connection);
            return x.callRealMethod();
        }).when(connection).close();

        when(connection.prepareStatement(anyString()))
                .thenAnswer(x -> configurePreparedStatement(spy((PreparedStatement) x.callRealMethod())));

        return connection;
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
        assertThat(knownConnections).isEmpty();
    }

}
