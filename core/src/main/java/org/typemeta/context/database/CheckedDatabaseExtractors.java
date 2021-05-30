package org.typemeta.context.database;

import org.typemeta.context.extractors.byname.*;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * A set of extraction functions and combinator functions.
 */
public abstract class CheckedDatabaseExtractors {

    /**
     * A combinator function to convert a {@link ExtractorByName.Checked} into one for {@link Optional} values.
     * @param extr      the extractor function for the value type
     * @param <T>       the value type
     * @return          the extractor function for the optional value
     */
    public static <T> ExtractorByName.Checked<ResultSet, Optional<T>, SQLException> optional(
            ExtractorByName.Checked<ResultSet, T, SQLException> extr
    ) {
        return (ResultSet rs, String name) -> {
            final T value = extr.extract(rs, name);
            if (rs.wasNull()) {
                return Optional.empty();
            } else {
                return Optional.of(value);
            }
        };
    }

    /**
     * An {@code ExtractorByName.Checked} instance for {@link boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Boolean, SQLException> BOOLEAN =
            ResultSet::getBoolean;

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Boolean>, SQLException> OPT_BOOLEAN =
            optional(BOOLEAN);

    /**
     * An {@code ExtractorByName.Checked} instance for {@link byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Byte, SQLException> BYTE =
            ResultSet::getByte;

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Byte>, SQLException> OPT_BYTE =
            optional(BYTE);

    /**
     * A {@code ExtractorByName.Checked} instance for double values.
     */
    public static final DoubleExtractorByName.Checked<ResultSet, SQLException> DOUBLE =
            ResultSet::getDouble;

    /**
     * A {@code ExtractorByName.Checked} instance for optional double values.
     */
    public static final OptDoubleExtractorByName.Checked<ResultSet, SQLException> OPT_DOUBLE =
            (rs, name) -> {
                final double value = DOUBLE.extractDouble(rs, name);
                if (rs.wasNull()) {
                    return OptionalDouble.empty();
                } else {
                    return OptionalDouble.of(value);
                }
            };

    /**
     * An {@code ExtractorByName.Checked} instance for {@code float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Float, SQLException> FLOAT =
            ResultSet::getFloat;

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Float>, SQLException> OPT_FLOAT =
            optional(FLOAT);

    /**
     * A {@code ExtractorByName} instance for integer values.
     */
    public static final IntExtractorByName.Checked<ResultSet, SQLException> INTEGER =
            ResultSet::getInt;

    /**
     * A {@code ExtractorByName.Checked} instance for optional integer values.
     */
    public static final OptIntExtractorByName.Checked<ResultSet, SQLException> OPT_INTEGER =
            (rs, name) -> {
                final int value = INTEGER.extractInt(rs, name);
                if (rs.wasNull()) {
                    return OptionalInt.empty();
                } else {
                    return OptionalInt.of(value);
                }
            };

    /**
     * A {@code ExtractorByName.Checked} instance for long values.
     */
    public static final LongExtractorByName.Checked<ResultSet, SQLException> LONG =
            ResultSet::getLong;

    /**
     * A {@code ExtractorByName.Checked} instance for optional long values.
     */
    public static final OptLongExtractorByName.Checked<ResultSet, SQLException> OPT_LONG =
            (rs, name) -> {
                final long value = LONG.extractLong(rs, name);
                if (rs.wasNull()) {
                    return OptionalLong.empty();
                } else {
                    return OptionalLong.of(value);
                }
            };

    /**
     * A {@code ExtractorByName.Checked} instance for {@code short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Short, SQLException> SHORT =
            ResultSet::getShort;

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Short>, SQLException> OPT_SHORT =
            optional(SHORT);

    /**
     * A {@code ExtractorByName.Checked} instance for {@code string} values.
     */
    public static final ExtractorByName.Checked<ResultSet, String, SQLException> STRING =
            ResultSet::getString;

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code string} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<String>, SQLException> OPT_STRING =
            optional(STRING);

    /**
     * A {@code ExtractorByName.Checked} instance for optional {@code string} values.
     * This converter will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<String>, SQLException> OPT_NONEMPTY_STRING =
            optional(STRING)
                    .map(oi -> oi.flatMap(s -> s.isEmpty() ? Optional.empty() : Optional.of(s)));

    /**
     * An extractor for {@link Date} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Date, SQLException> SQLDATE =
            ResultSet::getDate;

    /**
     * An extractor for optional {@code Date} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Date>, SQLException> OPT_SQLDATE =
            optional(SQLDATE);

    /**
     * An extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalDate, SQLException> LOCALDATE =
            SQLDATE.map(Date::toLocalDate);

    /**
     * An extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalDate>, SQLException> OPT_LOCALDATE =
            optional(SQLDATE)
                    .map(od -> od.map(Date::toLocalDate));

    /**
     * An extractor for {@link Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Time, SQLException> SQLTIME =
            ResultSet::getTime;

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Time>, SQLException> OPT_SQLTIME =
            optional(SQLTIME);

    /**
     * An extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalTime, SQLException> LOCALTIME =
            SQLTIME.map(Time::toLocalTime);

    /**
     * An extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalTime>, SQLException> OPT_LOCALTIME =
            optional(SQLTIME)
                    .map(od -> od.map(Time::toLocalTime));

    /**
     * An extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Timestamp, SQLException> SQLTIMESTAMP =
            ResultSet::getTimestamp;

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Timestamp>, SQLException> OPT_SQLTIMESTAMP =
            optional(SQLTIMESTAMP);

    /**
     * An extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalDateTime, SQLException> LOCALDATETIME =
            SQLTIMESTAMP.map(Timestamp::toInstant)
                    .map(inst -> LocalDateTime.ofInstant(inst, ZoneId.systemDefault()));

    /**
     * An extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalDateTime>, SQLException> OPT_LOCALDATETIME =
            optional(SQLTIMESTAMP)
                    .map(ots -> ots.map(Timestamp::toInstant)
                            .map(inst -> LocalDateTime.ofInstant(inst, ZoneId.systemDefault()))
                    );
}
