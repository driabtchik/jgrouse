package com.jgrouse.datasets.spring.test;

import org.springframework.test.context.jdbc.SqlConfig;

public class MergedSqlConfig {

    private final String datasourceName;
    private final String transactionManagerName;
    private final SqlConfig.TransactionMode transactionMode;

    public MergedSqlConfig(final String datasourceName, final String transactionManagerName, final SqlConfig.TransactionMode transactionMode) {
        this.datasourceName = datasourceName;
        this.transactionManagerName = transactionManagerName;
        this.transactionMode = transactionMode;
    }

    public MergedSqlConfig(final SqlConfig sqlConfig) {
        this.datasourceName = sqlConfig == null? null : sqlConfig.dataSource();
        this.transactionManagerName = sqlConfig == null? null : sqlConfig.transactionManager();
        this.transactionMode = sqlConfig == null? null : sqlConfig.transactionMode();
    }

    public MergedSqlConfig mergeWith(final MergedSqlConfig other) {
        return new MergedSqlConfig(
                other.datasourceName == null? this.datasourceName : other.datasourceName,
                other.transactionManagerName == null? this.transactionManagerName : other.transactionManagerName,
                other.transactionMode == null? this.transactionMode : other.transactionMode
        );
    }

    public String getDatasourceName() {
        return datasourceName;
    }

    public String getTransactionManagerName() {
        return transactionManagerName;
    }

    public SqlConfig.TransactionMode getTransactionMode() {
        return transactionMode;
    }
}
