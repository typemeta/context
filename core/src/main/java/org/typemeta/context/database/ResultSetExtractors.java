package org.typemeta.context.database;

import org.typemeta.context.extractors.byname.*;

import java.sql.*;
import java.sql.Date;
import java.time.*;
import java.util.*;

/**
 * A set of extractors for extracting values from database {@link ResultSet} objects.
 * These extractors catch any {@link SQLException} exceptions and rethrow as as an unchecked exception.
 */
public abstract class ResultSetExtractors {

    private ResultSetExtractors() {}

    /**
     * Convert a {@link ResultSet} extractor into one that extracts an {@link Optional} value.
     * @param extr      the extractor function for the value type
     * @param <T>       the value type
     * @return          the optional value extractor
     */
    public static <T> ExtractorByName<ResultSet, Optional<T>> optional(ExtractorByName<ResultSet, T> extr) {
        return ExtractorByName.Checked.<ResultSet, Optional<T>, SQLException>of((ResultSet rs, String name) -> {
            final T value = extr.extract(rs, name);
            if (rs.wasNull()) {
                return Optional.empty();
            } else {
                return Optional.of(value);
            }
        }).unchecked();
    }

    /**
     * A {@code ResultSet} extractor for {@link Boolean} values.
     */
    public static final ExtractorByName<ResultSet, Boolean> BOOLEAN =
            CheckedDatabaseExtractors.BOOLEAN.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Boolean} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Boolean>> OPT_BOOLEAN =
            CheckedDatabaseExtractors.OPT_BOOLEAN.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Byte} values.
     */
    public static final ExtractorByName<ResultSet, Byte> BYTE =
            CheckedDatabaseExtractors.BYTE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Byte} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Byte>> OPT_BYTE =
            CheckedDatabaseExtractors.OPT_BYTE.unchecked();

    /**
     * A {@code ResultSet} extractor for double values.
     */
    public static final DoubleExtractorByName<ResultSet> DOUBLE =
            CheckedDatabaseExtractors.DOUBLE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional double values.
     */
    public static final ExtractorByName<ResultSet, OptionalDouble> OPT_DOUBLE =
            CheckedDatabaseExtractors.OPT_DOUBLE.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Float} values.
     */
    public static final ExtractorByName<ResultSet, Float> FLOAT =
            CheckedDatabaseExtractors.FLOAT.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Float} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Float>> OPT_FLOAT =
            CheckedDatabaseExtractors.OPT_FLOAT.unchecked();

    /**
     * A {@code ResultSet} extractor for integer values.
     */
    public static final IntExtractorByName<ResultSet> INTEGER =
            CheckedDatabaseExtractors.INTEGER.unchecked();

    /**
     * A {@code ResultSet} extractor for optional integer values.
     */
    public static final ExtractorByName<ResultSet, OptionalInt> OPT_INTEGER =
            CheckedDatabaseExtractors.OPT_INTEGER.unchecked();

    /**
     * A {@code ResultSet} extractor for long values.
     */
    public static final LongExtractorByName<ResultSet> LONG =
            CheckedDatabaseExtractors.LONG.unchecked();

    /**
     * A {@code ResultSet} extractor for optional long values.
     */
    public static final ExtractorByName<ResultSet, OptionalLong> OPT_LONG =
            CheckedDatabaseExtractors.OPT_LONG.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Short} values.
     */
    public static final ExtractorByName<ResultSet, Short> SHORT =
            CheckedDatabaseExtractors.SHORT.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Short} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Short>> OPT_SHORT =
            CheckedDatabaseExtractors.OPT_SHORT.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link String} values.
     */
    public static final ExtractorByName<ResultSet, String> STRING =
            CheckedDatabaseExtractors.STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_STRING =
            CheckedDatabaseExtractors.OPT_STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     * This extractor will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_NONEMPTY_STRING =
            CheckedDatabaseExtractors.OPT_NONEMPTY_STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Date} values.
     */
    public static final ExtractorByName<ResultSet, Date> SQLDATE =
            CheckedDatabaseExtractors.SQLDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Date} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Date>> OPT_SQLDATE =
            CheckedDatabaseExtractors.OPT_SQLDATE.unchecked();


    /**
     * A {@code ResultSet} extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, LocalDate> LOCALDATE =
            CheckedDatabaseExtractors.LOCALDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDate>> OPT_LOCALDATE =
            CheckedDatabaseExtractors.OPT_LOCALDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Time} values.
     */
    public static final ExtractorByName<ResultSet, Time> SQLTIME =
            CheckedDatabaseExtractors.SQLTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Time>> OPT_SQLTIME =
            CheckedDatabaseExtractors.OPT_SQLTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalTime> LOCALTIME =
            CheckedDatabaseExtractors.LOCALTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalTime>> OPT_LOCALTIME =
            CheckedDatabaseExtractors.OPT_LOCALTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName<ResultSet, Timestamp> SQLTIMESTAMP =
            CheckedDatabaseExtractors.SQLTIMESTAMP.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            CheckedDatabaseExtractors.OPT_SQLTIMESTAMP.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalDateTime> LOCALDATETIME =
            CheckedDatabaseExtractors.LOCALDATETIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            CheckedDatabaseExtractors.OPT_LOCALDATETIME.unchecked();
}
