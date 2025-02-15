package org.typemeta.context.database;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.injectors.Injector;
import org.typemeta.context.injectors.byindex.InjectorByIndex;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.typemeta.context.database.DatabaseMeta.*;

public class DatabaseInjectorTest {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseInjectorTest.class);

    private static final Map<Class<?>, InjectorByIndex<PreparedStatement, ?>> injectors;

    static {
        injectors = new HashMap<>();
        injectors.put(Boolean.class, PreparedStatementInjectors.BOOLEAN);
        injectors.put(Byte.class, PreparedStatementInjectors.BYTE);
        injectors.put(Date.class, PreparedStatementInjectors.SQLDATE);
        injectors.put(Double.class, PreparedStatementInjectors.DOUBLE);
        injectors.put(Float.class, PreparedStatementInjectors.FLOAT);
        injectors.put(Integer.class, PreparedStatementInjectors.INTEGER);
        injectors.put(LocalDate.class, PreparedStatementInjectors.LOCALDATE);
        injectors.put(LocalDateTime.class, PreparedStatementInjectors.LOCALDATETIME);
        injectors.put(LocalTime.class, PreparedStatementInjectors.LOCALTIME);
        injectors.put(Long.class, PreparedStatementInjectors.LONG);
        injectors.put(Short.class, PreparedStatementInjectors.SHORT);
        injectors.put(String.class, PreparedStatementInjectors.STRING);
        injectors.put(Time.class, PreparedStatementInjectors.SQLTIME);
    }

    private static final Map<Class<?>, InjectorByIndex<PreparedStatement, ?>> optInjectors;

    static {
        optInjectors = new HashMap<>();
        optInjectors.put(Boolean.class, PreparedStatementInjectors.OPT_BOOLEAN);
        optInjectors.put(Byte.class, PreparedStatementInjectors.OPT_BYTE);
        optInjectors.put(Date.class, PreparedStatementInjectors.OPT_SQLDATE);
        optInjectors.put(Double.class, PreparedStatementInjectors.OPT_DOUBLE);
        optInjectors.put(Float.class, PreparedStatementInjectors.OPT_FLOAT);
        optInjectors.put(Integer.class, PreparedStatementInjectors.OPT_INTEGER);
        optInjectors.put(LocalDate.class, PreparedStatementInjectors.OPT_LOCALDATE);
        optInjectors.put(LocalDateTime.class, PreparedStatementInjectors.OPT_LOCALDATETIME);
        optInjectors.put(LocalTime.class, PreparedStatementInjectors.OPT_LOCALTIME);
        optInjectors.put(Long.class, PreparedStatementInjectors.OPT_LONG);
        optInjectors.put(Short.class, PreparedStatementInjectors.OPT_SHORT);
        optInjectors.put(String.class, PreparedStatementInjectors.OPT_STRING);
        optInjectors.put(Time.class, PreparedStatementInjectors.OPT_SQLTIME);
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
    }

    @AfterAll
    public static void shutdown() {
        if (testDbConn != null) {
            loadScript("cleanup.sql");
        }
    }

    @Test
    public void testNullable() throws SQLException {
        roundTrip(TableType.NULLABLE, RECORD1_VALUES, RECORD1_INJECTOR, RECORD1_EXTRACTOR);
        roundTrip(TableType.NULLABLE, RECORD2_VALUES, RECORD2_INJECTOR, RECORD2_EXTRACTOR);
        roundTrip(TableType.NULLABLE, OPTRECORD1_VALUES, OPTRECORD1_INJECTOR, OPTRECORD1_EXTRACTOR);
        roundTrip(TableType.NULLABLE, OPTRECORD2_VALUES, OPTRECORD2_INJECTOR, OPTRECORD2_EXTRACTOR);
    }

    @Test
    public void testNonNullable() throws SQLException {
        roundTrip(TableType.NOTNULLABLE, RECORD1_VALUES, RECORD1_INJECTOR, RECORD1_EXTRACTOR);
        roundTrip(TableType.NOTNULLABLE, RECORD2_VALUES, RECORD2_INJECTOR, RECORD2_EXTRACTOR);
        roundTrip(TableType.NOTNULLABLE, dropFirst(OPTRECORD1_VALUES), OPTRECORD1_INJECTOR, OPTRECORD1_EXTRACTOR);
        roundTrip(TableType.NOTNULLABLE, dropFirst(OPTRECORD2_VALUES), OPTRECORD2_INJECTOR, OPTRECORD2_EXTRACTOR);
    }

    private <T> T[] dropFirst(T[] values) {
        return Arrays.copyOfRange(values, 1, values.length);
    }

    private enum TableType {
        NULLABLE {
            @Override
            String insertScript() {
                return "insert_null.sql";
            }

            @Override
            String tableName() {
                return "test_null";
            }
        },
        NOTNULLABLE {
            @Override
            String insertScript() {
                return "insert_notnull.sql";
            }

            @Override
            String tableName() {
                return "test_notnull";
            }
        };

        abstract String insertScript();
        abstract String tableName();
    }

    private <T> void roundTrip(
            TableType tableType,
            T[] values,
            Injector<PreparedStatement, T> injector,
            Extractor<ResultSet, T> extractor
    ) throws SQLException {
        try (final PreparedStatement ps = testDbConn.prepareStatement(SqlUtils.loadSingleResource(tableType.insertScript()))) {
            for(T rec : values) {
                injector.inject(ps, rec);
                ps.execute();
            }

            final ResultSet rs = testDbConn.createStatement()
                    .executeQuery("SELECT * FROM " + tableType.tableName());

            for(T rec : values) {
                rs.next();
                final T dbRec = extractor.extract(rs);
                assertEquals(rec, dbRec);
            }

            rs.close();
        }

        testDbConn.createStatement().execute("DELETE FROM " + tableType.tableName());
    }
}

