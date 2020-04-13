package com.jgrouse.datasets.input;

import com.jgrouse.datasets.DataSetMetadataElement;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.NotNull;
import java.sql.JDBCType;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static com.jgrouse.util.Assert.notNull;

/**
 * Parse headers in format {@code [+]<identifier>:<type>[(<length>[,<precision>])]} <br>
 * For example <br>
 * <ul>
 * <li>someColumn:integer // column with type</li>
 * <li>userName:varchar(30) // not-nullable column</li>
 * <li>+salary:decimal(15,2) //for nullable column</li>
 * </ul>
 */
public class HeaderParsingInputDataSetMetadataElementFactory implements DataSetMetadataElementFactory {
    private static final Pattern IDENTIFIER_PATTERN = Pattern.compile("^[a-zA-Z_][a-zA-Z0-9_]*$");
    public static final int MAX_ELEMENTS_IN_PAIR = 2;


    @Override
    @NotNull
    public DataSetMetadataElement create(@NotNull final String headerName) {
        return new Parser(notNull(headerName, "headerName must be provided")).parse();
    }

    public static class HeaderParsingException extends RuntimeException {
        public HeaderParsingException(final String message) {
            super(message);
        }

        public HeaderParsingException(final String message, final Throwable cause) {
            super(message, cause);
        }
    }

    private final static class Parser {
        private final String header;
        private String buffer;
        private boolean nullable;
        private String name;
        private JDBCType jdbcType;
        private int length = -1;
        private int scale = -1;


        private Parser(final String header) {
            this.header = header;
            this.buffer = header;
        }

        private DataSetMetadataElement parse() {
            parseNullability();
            parseColumnName();
            parseTypeName();
            parseLength();
            parseScale();
            return new DataSetMetadataElement(name, jdbcType)
                    .setLength(length)
                    .setNullable(nullable)
                    .setScale(scale);
        }

        @SuppressWarnings("PMD.SimplifyStartsWith")
        private void parseNullability() {
            if (buffer.startsWith("+")) {
                nullable = true;
                buffer = buffer.substring(1);
            }
        }

        private void parseColumnName() {
            final String[] nameAndTail = split(buffer, ":");
            name = assertValidColumnName(nameAndTail[0]);
            buffer = nameAndTail.length > 1 ? nameAndTail[1] : "";
        }

        private String assertValidColumnName(final String name) {
            if (!IDENTIFIER_PATTERN.matcher(name).matches()) {
                throw new HeaderParsingException("Invalid column identifier \"" + name + "\" in " + header);
            }
            return name;
        }

        private String[] split(final String buffer, final String separator) {
            final String[] result = StringUtils.splitByWholeSeparatorPreserveAllTokens(buffer, separator);
            if (result.length > MAX_ELEMENTS_IN_PAIR) {
                throw new HeaderParsingException("Character '" + separator + "' should appear no more than once in " + header);
            }
            return result;
        }

        private void parseTypeName() {
            if (buffer.isEmpty()) {
                throw new HeaderParsingException("No types were specified for column " + name);
            }
            final String[] typeParts = split(buffer, "(");
            final String typeName = typeParts[0];
            jdbcType = validJdbcType(typeName);
            buffer = (typeParts.length > 1) ? typeParts[1] : "";
        }

        private JDBCType validJdbcType(final String typeName) {
            final Optional<JDBCType> matchedType = Stream.of(JDBCType.values())
                    .filter(type -> type.toString().equalsIgnoreCase(typeName))
                    .findAny();
            return matchedType
                    .orElseThrow(() -> new HeaderParsingException("Unknown JDBC Type '" + typeName + "' in " + header));
        }

        private void parseLength() {
            if (buffer.isEmpty()) {
                return;
            }
            if (!buffer.endsWith(")")) {
                throw new HeaderParsingException("Expecting ')' at the end of header " + header);
            }
            buffer = buffer.substring(0, buffer.length() - 1);
            final String[] lengthParts = split(buffer, ",");
            length = validNumber(lengthParts[0], "length");
            buffer = lengthParts.length > 1 ? lengthParts[1] : "";
        }

        private int validNumber(final String part, final String partName) {
            final String value = part.trim();
            if (StringUtils.isNumeric(value)) {
                try {
                    return Integer.parseInt(value);
                } catch (final NumberFormatException ex) {
                    throw new HeaderParsingException("Invalid " + partName + " '" + value + "' in header " + header, ex);
                }
            }
            throw new HeaderParsingException("Invalid " + partName + " '" + value + "' in header " + header);

        }

        private void parseScale() {
            if (buffer.isEmpty()) {
                return;
            }
            scale = validNumber(buffer, "scale");
        }
    }
}
