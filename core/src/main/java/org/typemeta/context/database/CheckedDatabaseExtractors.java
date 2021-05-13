package org.typemeta.context.database;

import org.typemeta.context.database.DatabaseExtractors.*;
import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.extractors.byname.*;
import org.typemeta.context.utils.Exceptions;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

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
     * An {@code NamedExtractorEx} instance for {@link boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Boolean, SQLException> BOOLEAN =
            ResultSet::getBoolean;

    /**
     * A {@code NamedExtractorEx} instance for optional {@code boolean} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Boolean>, SQLException> OPT_BOOLEAN =
            optional(BOOLEAN);

    /**
     * An {@code NamedExtractorEx} instance for {@link byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Byte, SQLException> BYTE =
            ResultSet::getByte;

    /**
     * A {@code NamedExtractorEx} instance for optional {@code byte} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Byte>, SQLException> OPT_BYTE =
            optional(BYTE);

    /**
     * A {@code NamedExtractorEx} instance for {@code double} values.
     */
    public static final DoubleExtractorByName.Checked<ResultSet, SQLException> DOUBLE =
            ResultSet::getDouble;

    /**
     * A {@code NamedExtractorEx} for optional {@code double} values.
     */
    public interface OptDoubleExtractByNameEx extends ExtractorByName.Checked<ResultSet, OptionalDouble, SQLException> {
        static OptDoubleExtractByNameEx of(OptDoubleExtractByNameEx extr) {
            return  extr;
        }

        default <U> ExtractorByName.Checked<ResultSet, Optional<U>, SQLException> map(DoubleFunction<U> f) {
            return (rs, name) -> {
                final OptionalDouble od = extract(rs, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsDouble()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default Extractor.Checked<ResultSet, OptionalDouble, SQLException> bind(String name) {
            return rs -> extract(rs, name);
        }

        @Override
        default OptDoubleExtractorByName unchecked() {
            return (rs, name) -> {
                try {
                    return extract(rs, name);
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }

    /**
     * A {@code NamedExtractorEx} instance for optional {@code double} values.
     */
    public static final OptDoubleExtractByNameEx OPT_DOUBLE =
            (rs, name) -> {
                final double value = DOUBLE.extractDouble(rs, name);
                if (rs.wasNull()) {
                    return OptionalDouble.empty();
                } else {
                    return OptionalDouble.of(value);
                }
            };

    /**
     * An {@code NamedExtractorEx} instance for {@code float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Float, SQLException> FLOAT =
            ResultSet::getFloat;

    /**
     * A {@code NamedExtractorEx} instance for optional {@code float} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Float>, SQLException> OPT_FLOAT =
            optional(FLOAT);

    /**
     * A {@code ExtractorByName} instance for {@code int} values.
     */
    public static final IntExtractorByName.Checked<ResultSet, SQLException> INTEGER =
            ResultSet::getInt;

    /**
     * A {@code NamedExtractorEx} for optional {@code int} values.
     */
    public interface OptIntExtractByNameEx extends ExtractorByName.Checked<ResultSet, OptionalInt, SQLException> {
        static OptIntExtractByNameEx of(OptIntExtractByNameEx extr) {
            return  extr;
        }

        default <U> ExtractorByName.Checked<ResultSet, Optional<U>, SQLException> map(IntFunction<U> f) {
            return (rs, name) -> {
                final OptionalInt od = extract(rs, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsInt()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default Extractor.Checked<ResultSet, OptionalInt, SQLException> bind(String name) {
            return rs -> extract(rs, name);
        }

        @Override
        default OptIntExtractorByName unchecked() {
            return (rs, name) -> {
                try {
                    return extract(rs, name);
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }

    /**
     * A {@code NamedExtractorEx} instance for optional {@code int} values.
     */
    public static final OptIntExtractByNameEx OPT_INTEGER =
            (rs, name) -> {
                final int value = INTEGER.extractInt(rs, name);
                if (rs.wasNull()) {
                    return OptionalInt.empty();
                } else {
                    return OptionalInt.of(value);
                }
            };

    /**
     * A {@code NamedExtractorEx} instance for {@code long} values.
     */
    public static final LongExtractorByName.Checked<ResultSet, SQLException> LONG =
            ResultSet::getLong;

    /**
     * A {@code NamedExtractorEx} for optional {@code long} values.
     */
    public interface OptLongExtractByNameEx extends ExtractorByName.Checked<ResultSet, OptionalLong, SQLException> {
        static OptLongExtractByNameEx of(OptLongExtractByNameEx extr) {
            return  extr;
        }

        default <U> ExtractorByName.Checked<ResultSet, Optional<U>, SQLException> map(LongFunction<U> f) {
            return (rs, name) -> {
                final OptionalLong od = extract(rs, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsLong()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default Extractor.Checked<ResultSet, OptionalLong, SQLException> bind(String name) {
            return rs -> extract(rs, name);
        }

        @Override
        default OptLongExtractorByName unchecked() {
            return (rs, name) -> {
                try {
                    return extract(rs, name);
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }

    /**
     * A {@code NamedExtractorEx} instance for optional {@code long} values.
     */
    public static final OptLongExtractByNameEx OPT_LONG =
            (rs, name) -> {
                final long value = LONG.extractLong(rs, name);
                if (rs.wasNull()) {
                    return OptionalLong.empty();
                } else {
                    return OptionalLong.of(value);
                }
            };

    /**
     * A {@code NamedExtractorEx} instance for {@code short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Short, SQLException> SHORT =
            ResultSet::getShort;

    /**
     * A {@code NamedExtractorEx} instance for optional {@code short} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<Short>, SQLException> OPT_SHORT =
            optional(SHORT);

    /**
     * A {@code NamedExtractorEx} instance for {@code string} values.
     */
    public static final ExtractorByName.Checked<ResultSet, String, SQLException> STRING =
            ResultSet::getString;

    /**
     * A {@code NamedExtractorEx} instance for optional {@code string} values.
     */
    public static final ExtractorByName.Checked<ResultSet, Optional<String>, SQLException> OPT_STRING =
            optional(STRING);

    /**
     * A {@code NamedExtractorEx} instance for optional {@code string} values.
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
