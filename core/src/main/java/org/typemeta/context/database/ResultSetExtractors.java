package org.typemeta.context.database;

import org.typemeta.context.extractors.byname.DoubleExtractorByName;
import org.typemeta.context.extractors.byname.ExtractorByName;
import org.typemeta.context.extractors.byname.IntExtractorByName;
import org.typemeta.context.extractors.byname.LongExtractorByName;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * A set of extractors for extracting values from database {@link ResultSet} objects.
 * These extractors catch any {@link SQLException} exceptions and rethrow as an unchecked exception.
 */
public abstract class ResultSetExtractors {

    private ResultSetExtractors() {}

    /**
     * Convert a {@code ResultSet} extractor into one that extracts an {@link Optional} value.
     * @param extr      the extractor function for the value type
     * @param <T>       the value type
     * @return          the optional value extractor
     */
    public static <T> ExtractorByName<ResultSet, Optional<T>> optional(ExtractorByName<ResultSet, T> extr) {
        return ExtractorByName.Checked.<ResultSet, Optional<T>, SQLException>of((rs, name) -> {
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
            CheckedResultSetExtractors.BOOLEAN.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Boolean} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Boolean>> OPT_BOOLEAN =
            CheckedResultSetExtractors.OPT_BOOLEAN.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Byte} values.
     */
    public static final ExtractorByName<ResultSet, Byte> BYTE =
            CheckedResultSetExtractors.BYTE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Byte} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Byte>> OPT_BYTE =
            CheckedResultSetExtractors.OPT_BYTE.unchecked();

    /**
     * A {@code ResultSet} extractor for double values.
     */
    public static final DoubleExtractorByName<ResultSet> DOUBLE =
            CheckedResultSetExtractors.DOUBLE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional double values.
     */
    public static final ExtractorByName<ResultSet, OptionalDouble> OPT_DOUBLE =
            CheckedResultSetExtractors.OPT_DOUBLE.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Float} values.
     */
    public static final ExtractorByName<ResultSet, Float> FLOAT =
            CheckedResultSetExtractors.FLOAT.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Float} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Float>> OPT_FLOAT =
            CheckedResultSetExtractors.OPT_FLOAT.unchecked();

    /**
     * A {@code ResultSet} extractor for integer values.
     */
    public static final IntExtractorByName<ResultSet> INTEGER =
            CheckedResultSetExtractors.INTEGER.unchecked();

    /**
     * A {@code ResultSet} extractor for optional integer values.
     */
    public static final ExtractorByName<ResultSet, OptionalInt> OPT_INTEGER =
            CheckedResultSetExtractors.OPT_INTEGER.unchecked();

    /**
     * A {@code ResultSet} extractor for long values.
     */
    public static final LongExtractorByName<ResultSet> LONG =
            CheckedResultSetExtractors.LONG.unchecked();

    /**
     * A {@code ResultSet} extractor for optional long values.
     */
    public static final ExtractorByName<ResultSet, OptionalLong> OPT_LONG =
            CheckedResultSetExtractors.OPT_LONG.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Short} values.
     */
    public static final ExtractorByName<ResultSet, Short> SHORT =
            CheckedResultSetExtractors.SHORT.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Short} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Short>> OPT_SHORT =
            CheckedResultSetExtractors.OPT_SHORT.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link String} values.
     */
    public static final ExtractorByName<ResultSet, String> STRING =
            CheckedResultSetExtractors.STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_STRING =
            CheckedResultSetExtractors.OPT_STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     * This extractor will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_NONEMPTY_STRING =
            CheckedResultSetExtractors.OPT_NONEMPTY_STRING.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Date} values.
     */
    public static final ExtractorByName<ResultSet, Date> SQLDATE =
            CheckedResultSetExtractors.SQLDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Date} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Date>> OPT_SQLDATE =
            CheckedResultSetExtractors.OPT_SQLDATE.unchecked();


    /**
     * A {@code ResultSet} extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, LocalDate> LOCALDATE =
            CheckedResultSetExtractors.LOCALDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDate>> OPT_LOCALDATE =
            CheckedResultSetExtractors.OPT_LOCALDATE.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Time} values.
     */
    public static final ExtractorByName<ResultSet, Time> SQLTIME =
            CheckedResultSetExtractors.SQLTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Time>> OPT_SQLTIME =
            CheckedResultSetExtractors.OPT_SQLTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalTime> LOCALTIME =
            CheckedResultSetExtractors.LOCALTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalTime>> OPT_LOCALTIME =
            CheckedResultSetExtractors.OPT_LOCALTIME.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName<ResultSet, Timestamp> SQLTIMESTAMP =
            CheckedResultSetExtractors.SQLTIMESTAMP.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            CheckedResultSetExtractors.OPT_SQLTIMESTAMP.unchecked();

    /**
     * A {@code ResultSet} extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalDateTime> LOCALDATETIME =
            CheckedResultSetExtractors.LOCALDATETIME.unchecked();

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            CheckedResultSetExtractors.OPT_LOCALDATETIME.unchecked();
}
