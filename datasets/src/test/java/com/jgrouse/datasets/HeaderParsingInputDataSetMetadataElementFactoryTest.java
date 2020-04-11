package com.jgrouse.datasets;

import com.jgrouse.datasets.input.HeaderParsingInputDataSetMetadataElementFactory;
import com.jgrouse.datasets.input.HeaderParsingInputDataSetMetadataElementFactory.HeaderParsingException;
import org.junit.jupiter.api.Test;

import java.sql.JDBCType;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class HeaderParsingInputDataSetMetadataElementFactoryTest {

    private static final String HEADER_NAME = "column";
    private final HeaderParsingInputDataSetMetadataElementFactory
            factory = new HeaderParsingInputDataSetMetadataElementFactory();

    @Test
    public void testCreate_nullable() {
        assertThat(factory.create("+column:integer")).usingRecursiveComparison().isEqualTo(
                new DataSetMetadataElement(HEADER_NAME, JDBCType.INTEGER).setNullable(true)
        );
    }

    @Test
    public void testCreate_withSimpleDataType() {
        assertThat(factory.create("column:dAte")).usingRecursiveComparison().isEqualTo(
                new DataSetMetadataElement("column", JDBCType.DATE)
        );
    }

    @Test
    public void testCreate_withLength() {
        assertThat(factory.create("column:varchar(42)")).usingRecursiveComparison().isEqualTo(
                new DataSetMetadataElement("column", JDBCType.VARCHAR).setLength(42)
        );
    }

    @Test
    public void testCreate_withLengthAndScale() {
        assertThat(factory.create("+_col_Umn1:decimal(42,30)")).usingRecursiveComparison().isEqualTo(
                new DataSetMetadataElement("_col_Umn1", JDBCType.DECIMAL)
                        .setLength(42)
                        .setScale(30)
                        .setNullable(true)
        );
    }

    @Test
    public void testCreate_invalidCharactersInName() {
        assertThatThrownBy(() -> factory.create("54@435"))
                .isInstanceOf(HeaderParsingException.class)
                .hasMessageContaining("Invalid column identifier");
        assertThatThrownBy(() -> factory.create("_54@435")).isInstanceOf(HeaderParsingException.class);
        assertThatThrownBy(() -> factory.create("z%")).isInstanceOf(HeaderParsingException.class);
        assertThatThrownBy(() -> factory.create("z_%")).isInstanceOf(HeaderParsingException.class);
        assertThatThrownBy(() -> factory.create("+:VARCHAR")).isInstanceOf(HeaderParsingException.class);
    }

    @Test
    public void testCreate_noType() {
        assertThatThrownBy(() -> factory.create("foobar"))
                .isInstanceOf(HeaderParsingException.class)
                .hasMessageContaining("No types were specified");
    }

    @Test
    public void testCreate_failOnMultipleColons() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR:34"))
                .isInstanceOf(HeaderParsingException.class)
                .hasMessageContaining("Character ':'");
    }

    @Test
    public void testCreate_failOnMultipleParenthesis() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR((34"))
                .isInstanceOf(HeaderParsingException.class).hasMessageContaining("Character '('");
    }

    @Test
    public void testCreate_failOnMissingClosingParenthesis() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR(34x"))
                .isInstanceOf(HeaderParsingException.class)
                .hasMessageContaining("Expecting ')'");
    }

    @Test
    public void testCreate_failOnUnknownJdbcType() {
        assertThatThrownBy(() -> factory.create("col:FOOBAR"))
                .isInstanceOf(HeaderParsingException.class)
                .hasMessageContaining("Unknown JDBC Type");
    }

    @Test
    public void testCreate_failOnMultipleCommas() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR(34,,)"))
                .isInstanceOf(HeaderParsingException.class).hasMessageContaining("Character ','");
    }

    @Test
    public void testCreate_failOnInvalidLength() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR(foobar)"))
                .isInstanceOf(HeaderParsingException.class).hasMessageContaining("Invalid length ");
        assertThatThrownBy(() -> factory.create("col:VARCHAR(100000000000000000000000000000)"))
                .isInstanceOf(HeaderParsingException.class).hasMessageContaining("Invalid length ");
    }

    @Test
    public void testCreate_failOnInvalidScale() {
        assertThatThrownBy(() -> factory.create("col:VARCHAR(100,foobar)"))
                .isInstanceOf(HeaderParsingException.class).hasMessageContaining("Invalid scale ");
    }


}