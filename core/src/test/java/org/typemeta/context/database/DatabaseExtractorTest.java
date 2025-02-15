package org.typemeta.context.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.extractors.byname.ExtractorByName;

import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.typemeta.context.database.DatabaseMeta.*;

@Disabled
public class DatabaseExtractorTest {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseExtractorTest.class);

    private static final Map<Class<?>, ExtractorByName<ResultSet, ?>> extractors;

    static {
        extractors = new HashMap<>();
        extractors.put(Boolean.class, ResultSetExtractors.BOOLEAN);
        extractors.put(Byte.class, ResultSetExtractors.BYTE);
        extractors.put(Date.class, ResultSetExtractors.SQLDATE);
        extractors.put(Double.class, ResultSetExtractors.DOUBLE);
        extractors.put(Float.class, ResultSetExtractors.FLOAT);
        extractors.put(Integer.class, ResultSetExtractors.INTEGER);
        extractors.put(LocalDate.class, ResultSetExtractors.LOCALDATE);
        extractors.put(LocalDateTime.class, ResultSetExtractors.LOCALDATETIME);
        extractors.put(LocalTime.class, ResultSetExtractors.LOCALTIME);
        extractors.put(Long.class, ResultSetExtractors.LONG);
        extractors.put(Short.class, ResultSetExtractors.SHORT);
        extractors.put(String.class, ResultSetExtractors.STRING);
        extractors.put(Time.class, ResultSetExtractors.SQLTIME);
    }

    private static final Map<Class<?>, ExtractorByName<ResultSet, ?>>  optExtractors;

    static {
        optExtractors = new HashMap<>();
        optExtractors.put(Boolean.class, ResultSetExtractors.OPT_BOOLEAN);
        optExtractors.put(Byte.class, ResultSetExtractors.OPT_BYTE);
        optExtractors.put(Date.class, ResultSetExtractors.OPT_SQLDATE);
        optExtractors.put(Double.class, ResultSetExtractors.OPT_DOUBLE);
        optExtractors.put(Float.class, ResultSetExtractors.OPT_FLOAT);
        optExtractors.put(Integer.class, ResultSetExtractors.OPT_INTEGER);
        optExtractors.put(LocalDate.class, ResultSetExtractors.OPT_LOCALDATE);
        optExtractors.put(LocalDateTime.class, ResultSetExtractors.OPT_LOCALDATETIME);
        optExtractors.put(LocalTime.class, ResultSetExtractors.OPT_LOCALTIME);
        optExtractors.put(Long.class, ResultSetExtractors.OPT_LONG);
        optExtractors.put(Short.class, ResultSetExtractors.OPT_SHORT);
        optExtractors.put(String.class, ResultSetExtractors.OPT_STRING);
        optExtractors.put(Time.class, ResultSetExtractors.OPT_SQLTIME);
    }

    private static Connection testDbConn;

    static void loadScript(String path) {
        logger.info("Loading script " + path);
        SqlUtils.loadMultiResource(path)
                .forEach(sql -> {
                    try (final Statement stmt = testDbConn.createStatement()) {
                        stmt.execute(sql);
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                });
    }

    @BeforeAll
    public static void setupDatabase() throws Exception {
        Class.forName(DERBY_DRIVER).getDeclaredConstructor().newInstance();

        logger.info("Connecting to embedded database");
        testDbConn = DriverManager.getConnection(JDBC_CONN_URL);

        loadScript("create.sql");
        loadScript("data.sql");
    }

    @AfterAll
    public static void shutdown() {
        if (testDbConn != null) {
            loadScript("cleanup.sql");
        }
    }

    private static Number getOptionalValue(Object optional) {
        if (optional instanceof Optional) {
            return (Number)((Optional<?>)optional).orElse(null);
        } else if (optional instanceof OptionalInt) {
            final OptionalInt optInt = ((OptionalInt)optional);
            return optInt.isPresent() ? optInt.getAsInt() : null;
        } else if (optional instanceof OptionalDouble) {
            final OptionalDouble optDbl = ((OptionalDouble)optional);
            return optDbl.isPresent() ? optDbl.getAsDouble() : null;
        } else if (optional instanceof OptionalLong) {
            final OptionalLong optLng = ((OptionalLong)optional);
            return optLng.isPresent() ? optLng.getAsLong() : null;
        } else {
            throw new RuntimeException("Value is not an optional : " + optional);
        }
    }

    @Test
    public void testNullable() throws SQLException {
        final Double[] prevValues = new Double[TABLE_COLUMNS.length];

        for (int i = 0; i < 10; ++i) {
            final ResultSet rs = testDbConn.createStatement()
                    .executeQuery("SELECT * FROM test_null");

            while (rs.next()) {
                for (int nCol = 0; nCol < TABLE_COLUMNS.length; ++nCol) {
                    final Column column = TABLE_COLUMNS[nCol];
                    final List<Class<?>> javaTypes = sqlTypeMap.get(column.type);
                    if (i < javaTypes.size()) {
                        final Class<?> javaType = javaTypes.get(i);
                        if (javaTypes.equals(NUMERIC_TYPES)) {
                            if (prevValues[nCol] == null) {
                                final Object value = optExtractors.get(javaType).bind(column.name).extract(rs);
                                final Number num = getOptionalValue(value);
                                if (num != null) {
                                    prevValues[nCol] = num.doubleValue();
                                }
                            } else {
                                if ((prevValues[nCol] < NUMERIC_MAX[i])) {
                                    final Object value = optExtractors.get(javaType).bind(column.name).extract(rs);
                                    assertNotNull(value);
                                }
                            }
                        } else {
                            final ExtractorByName<ResultSet, ?> a = optExtractors.get(javaType);
                            final Extractor<ResultSet, ?> b = a.bind(column.name);
                            b.extract(rs);
                            final Object value = optExtractors.get(javaType).bind(column.name).extract(rs);
                            assertNotNull(value);
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testNonNullable() throws SQLException {
        final Double[] prevValues = new Double[TABLE_COLUMNS.length];

        for (int i = 0; i < 10; ++i) {
            final ResultSet rs = testDbConn.createStatement()
                    .executeQuery("SELECT * FROM test_notnull");

            while (rs.next()) {
                for (int nCol = 0; nCol < TABLE_COLUMNS.length; ++nCol) {
                    final Column column = TABLE_COLUMNS[nCol];
                    final List<Class<?>> javaTypes = sqlTypeMap.get(column.type);
                    if (i < javaTypes.size()) {
                        final Class<?> javaType = javaTypes.get(i);
                        if (javaTypes.equals(NUMERIC_TYPES)) {
                            if (prevValues[nCol] == null) {
                                final Number num = (Number)extractors.get(javaType).bind(column.name).extract(rs);
                                if (num != null) {
                                    prevValues[nCol] = num.doubleValue();
                                }
                            } else {
                                if (prevValues[nCol] < NUMERIC_MAX[i]) {
                                    final Object value = extractors.get(javaType).bind(column.name).extract(rs);
                                    assertNotNull(value);
                                }
                            }
                        } else {
                            final Object value = extractors.get(javaType).bind(column.name).extract(rs);
                            assertNotNull(value);
                        }
                    }
                }
            }
        }
    }
}

