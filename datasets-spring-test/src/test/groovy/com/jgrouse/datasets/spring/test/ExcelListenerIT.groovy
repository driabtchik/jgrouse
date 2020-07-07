package com.jgrouse.datasets.spring.test

import org.assertj.core.api.WithAssertions
import org.h2.jdbcx.JdbcDataSource
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig

import javax.sql.DataSource

@SpringJUnitConfig(classes = Config.class)
@Sql("/testSchema.sql")
class ExcelListenerIT implements WithAssertions {

    @Autowired
    private JdbcTemplate jdbcTemplate

    private static final List<Map<String, Object>> TABLE1_ROWS = [
            [
                    "F1": 42,
                    "F2": "foo"
            ], [
                    "F1": 44,
                    "F2": "bar"
            ]
    ]


    @Test
    @ExcelSource("/data/testWorkbook.xlsx")
    void withExcel() {
        List<Map<String, Object>> table1Rows = jdbcTemplate.queryForList("select F1, F2 from T1 order by F1")
        assert table1Rows == TABLE1_ROWS
    }


    @Configuration
    static class Config {
        @Bean
        DataSource dataSource() {
            return new JdbcDataSource().tap {
                url = "jdbc:h2:mem:ExcelListenerIT;DB_CLOSE_DELAY=-1"
            }
        }

        @Bean
        JdbcTemplate jdbcTemplate() {
            return new JdbcTemplate(dataSource())
        }
    }

}