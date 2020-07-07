package com.jgrouse.datasets.spring.test

import groovy.transform.CompileStatic
import org.assertj.core.api.WithAssertions
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.SqlConfig

import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.INFERRED
import static org.springframework.test.context.jdbc.SqlConfig.TransactionMode.ISOLATED

@CompileStatic
class MergedSqlConfigTest implements WithAssertions {
    private static final String CLASS_LEVEL_DATASOURCE_NAME = "classLevelDatasource"
    private static final String CLASS_LEVEL_TRANSACTION_MANAGER = 'class level transaction manager'
    private static final String METHOD_LEVEL_DATASOURCE_NAME = 'method level datasource'
    private static final String METHOD_LEVEL_TRANSACTION_MANAGER_NAME = 'method level transaction manager'

    @Test
    void foo() {
        fail('fix me')
    }

    @SqlConfig(
            dataSource = CLASS_LEVEL_DATASOURCE_NAME,
            transactionManager = CLASS_LEVEL_TRANSACTION_MANAGER,
            transactionMode = INFERRED
    )
    @SuppressWarnings("unused") //used via reflection
    static class FooBar {

        void withClassLevelConfig() {
            //no-op
        }

        @ExcelSource(
                value = "someWorkbookName.xlsx",
                config = @SqlConfig(
                        transactionMode = ISOLATED,
                        dataSource = METHOD_LEVEL_DATASOURCE_NAME,
                        transactionManager = METHOD_LEVEL_TRANSACTION_MANAGER_NAME
                )
        )
        void withMethodLevelConfig() {
            //no-op
        }
    }

}