package org.typemeta.context.database;

import org.typemeta.context.extractors.byname.*;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * A set of extractors for extracting values from database {@link ResultSet} objects.
 * These extractors may throw {@link SQLException} exceptions.
 */
public abstract class CheckedDatabaseExtractors {

    /**
     * Convert a {@code ResultSet} extractor extractor into one for {@link Optional} values.
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
     * A {@code ResultSet} extractor for {@link Boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Boolean, SQLException> BOOLEAN =
            ResultSet::getBoolean;

    /**
     * A {@code ResultSet} extractor for optional {@code Boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Boolean>, SQLException> OPT_BOOLEAN =
            optional(BOOLEAN);

    /**
     *A {@code ResultSet} extractor for {@link Byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Byte, SQLException> BYTE =
            ResultSet::getByte;

    /**
     * A {@code ResultSet} extractor for optional {@code Byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Byte>, SQLException> OPT_BYTE =
            optional(BYTE);

    /**
     * A {@code ResultSet} extractor for double values.
     */
    public static final DoubleExtractorByName.Checked<ResultSet, SQLException> DOUBLE =
            ResultSet::getDouble;

    /**
     * A {@code ResultSet} extractor for optional double values.
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
     * A {@code ResultSet} extractor for {@code Float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Float, SQLException> FLOAT =
            ResultSet::getFloat;

    /**
     * A {@code ResultSet} extractor for optional {@code Float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Float>, SQLException> OPT_FLOAT =
            optional(FLOAT);

    /**
     * A {@code ExtractorByName} instance for integer values.
     */
    public static final IntExtractorByName.Checked<ResultSet, SQLException> INTEGER =
            ResultSet::getInt;

    /**
     * A {@code ResultSet} extractor for optional integer values.
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
     * A {@code ResultSet} extractor for long values.
     */
    public static final LongExtractorByName.Checked<ResultSet, SQLException> LONG =
            ResultSet::getLong;

    /**
     * A {@code ResultSet} extractor for optional long values.
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
     * A {@code ResultSet} extractor for {@code Short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Short, SQLException> SHORT =
            ResultSet::getShort;

    /**
     * A {@code ResultSet} extractor for optional {@code Short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Short>, SQLException> OPT_SHORT =
            optional(SHORT);

    /**
     * A {@code ResultSet} extractor for {@link String} values.
     */
    public static final ExtractorByName.Checked<ResultSet, String, SQLException> STRING =
            ResultSet::getString;

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<String>, SQLException> OPT_STRING =
            optional(STRING);

    /**
     * A {@code ResultSet} extractor for optional {@code String} values.
     * This extractor will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<String>, SQLException> OPT_NONEMPTY_STRING =
            optional(STRING)
                    .map(oi -> oi.flatMap(s -> s.isEmpty() ? Optional.empty() : Optional.of(s)));

    /**
     * A {@code ResultSet} extractor for {@link Date} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Date, SQLException> SQLDATE =
            ResultSet::getDate;

    /**
     * A {@code ResultSet} extractor for optional {@code Date} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Date>, SQLException> OPT_SQLDATE =
            optional(SQLDATE);

    /**
     * A {@code ResultSet} extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalDate, SQLException> LOCALDATE =
            SQLDATE.map(Date::toLocalDate);

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalDate>, SQLException> OPT_LOCALDATE =
            optional(SQLDATE)
                    .map(od -> od.map(Date::toLocalDate));

    /**
     * A {@code ResultSet} extractor for {@link Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Time, SQLException> SQLTIME =
            ResultSet::getTime;

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Time>, SQLException> OPT_SQLTIME =
            optional(SQLTIME);

    /**
     * A {@code ResultSet} extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalTime, SQLException> LOCALTIME =
            SQLTIME.map(Time::toLocalTime);

    /**
     * A {@code ResultSet} extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalTime>, SQLException> OPT_LOCALTIME =
            optional(SQLTIME)
                    .map(od -> od.map(Time::toLocalTime));

    /**
     * A {@code ResultSet} extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Timestamp, SQLException> SQLTIMESTAMP =
            ResultSet::getTimestamp;

    /**
     * A {@code ResultSet} extractor for optional {@code Time} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Timestamp>, SQLException> OPT_SQLTIMESTAMP =
            optional(SQLTIMESTAMP);

    /**
     * A {@code ResultSet} extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, LocalDateTime, SQLException> LOCALDATETIME =
            SQLTIMESTAMP.map(Timestamp::toInstant)
                    .map(inst -> LocalDateTime.ofInstant(inst, ZoneId.systemDefault()));

    /**
     * A {@code ResultSet} extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<LocalDateTime>, SQLException> OPT_LOCALDATETIME =
            optional(SQLTIMESTAMP)
                    .map(ots -> ots.map(Timestamp::toInstant)
                            .map(inst -> LocalDateTime.ofInstant(inst, ZoneId.systemDefault()))
                    );
}
