package org.typemeta.context.database;

import org.typemeta.context.extractors.byname.*;

import java.sql.*;
import java.time.*;
import java.util.Optional;

/**
 * A set of database value extractor combinator functions.
 */
public abstract class DatabaseExtractors {

    /**
     * A combinator function to convert a {@link ExtractorByName} into one for {@link Optional} values.
     * @param extr      the extractor function for the value type
     * @param <T>       the value type
     * @return          the extractor function for the optional value
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
     * An {@code ExtractorByName} instance for {@code boolean} values.
     */
    public static final ExtractorByName<ResultSet, Boolean> BOOLEAN =
            CheckedDatabaseExtractors.BOOLEAN.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code boolean} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Boolean>> OPT_BOOLEAN =
            CheckedDatabaseExtractors.OPT_BOOLEAN.unchecked();

    /**
     * An {@code ExtractorByName} instance for {@code byte} values.
     */
    public static final ExtractorByName<ResultSet, Byte> BYTE =
            CheckedDatabaseExtractors.BYTE.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code byte} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Byte>> OPT_BYTE =
            CheckedDatabaseExtractors.OPT_BYTE.unchecked();

    /**
     * A {@code ExtractorByName} instance for {@code double} values.
     */
    public static final DoubleExtractorByName<ResultSet> DOUBLE =
            CheckedDatabaseExtractors.DOUBLE.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code double} values.
     */
    public static final OptDoubleExtractorByName<ResultSet> OPT_DOUBLE =
            CheckedDatabaseExtractors.OPT_DOUBLE.unchecked();

    /**
     * An {@code ExtractorByName} instance for {@code float} values.
     */
    public static final ExtractorByName<ResultSet, Float> FLOAT =
            CheckedDatabaseExtractors.FLOAT.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code float} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Float>> OPT_FLOAT =
            CheckedDatabaseExtractors.OPT_FLOAT.unchecked();

    /**
     * A {@code ExtractorByName} instance for {@code int} values.
     */
    public static final IntExtractorByName<ResultSet> INTEGER =
            CheckedDatabaseExtractors.INTEGER.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code int} values.
     */
    public static final OptIntExtractorByName<ResultSet> OPT_INTEGER =
            CheckedDatabaseExtractors.OPT_INTEGER.unchecked();

    /**
     * A {@code ExtractorByName} instance for {@code long} values.
     */
    public static final LongExtractorByName<ResultSet> LONG =
            CheckedDatabaseExtractors.LONG.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code long} values.
     */
    public static final OptLongExtractorByName<ResultSet> OPT_LONG =
            CheckedDatabaseExtractors.OPT_LONG.unchecked();

    /**
     * A {@code ExtractorByName} instance for {@code short} values.
     */
    public static final ExtractorByName<ResultSet, Short> SHORT =
            CheckedDatabaseExtractors.SHORT.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code short} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Short>> OPT_SHORT =
            CheckedDatabaseExtractors.OPT_SHORT.unchecked();

    /**
     * A {@code ExtractorByName} instance for {@code string} values.
     */
    public static final ExtractorByName<ResultSet, String> STRING =
            CheckedDatabaseExtractors.STRING.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code string} values.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_STRING =
            CheckedDatabaseExtractors.OPT_STRING.unchecked();

    /**
     * A {@code ExtractorByName} instance for optional {@code string} values.
     * This converter will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_NONEMPTY_STRING =
            CheckedDatabaseExtractors.OPT_NONEMPTY_STRING.unchecked();

    /**
     * An extractor for {@link Date} values.
     */
    public static final ExtractorByName<ResultSet, Date> SQLDATE =
            CheckedDatabaseExtractors.SQLDATE.unchecked();

    /**
     * An extractor for optional {@code Date} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Date>> OPT_SQLDATE =
            CheckedDatabaseExtractors.OPT_SQLDATE.unchecked();


    /**
     * An extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, LocalDate> LOCALDATE =
            CheckedDatabaseExtractors.LOCALDATE.unchecked();

    /**
     * An extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDate>> OPT_LOCALDATE =
            CheckedDatabaseExtractors.OPT_LOCALDATE.unchecked();

    /**
     * An extractor for {@link Time} values.
     */
    public static final ExtractorByName<ResultSet, Time> SQLTIME =
            CheckedDatabaseExtractors.SQLTIME.unchecked();

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Time>> OPT_SQLTIME =
            CheckedDatabaseExtractors.OPT_SQLTIME.unchecked();

    /**
     * An extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalTime> LOCALTIME =
            CheckedDatabaseExtractors.LOCALTIME.unchecked();

    /**
     * An extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalTime>> OPT_LOCALTIME =
            CheckedDatabaseExtractors.OPT_LOCALTIME.unchecked();

    /**
     * An extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName<ResultSet, Timestamp> SQLTIMESTAMP =
            CheckedDatabaseExtractors.SQLTIMESTAMP.unchecked();

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            CheckedDatabaseExtractors.OPT_SQLTIMESTAMP.unchecked();

    /**
     * An extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalDateTime> LOCALDATETIME =
            CheckedDatabaseExtractors.LOCALDATETIME.unchecked();

    /**
     * An extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            CheckedDatabaseExtractors.OPT_LOCALDATETIME.unchecked();
}
