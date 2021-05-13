package org.typemeta.context.database;

import org.typemeta.context.extractors.*;
import org.typemeta.context.extractors.byname.*;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;
import java.util.function.*;

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
     * An {@code NamedExtractor} instance for {@code boolean} values.
     */
    public static final ExtractorByName<ResultSet, Boolean> BOOLEAN =
            DatabaseExtractorsEx.BOOLEAN.unchecked();

    /**
     * A {@code NamedExtractor} instance for optional {@code boolean} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Boolean>> OPT_BOOLEAN =
            DatabaseExtractorsEx.OPT_BOOLEAN.unchecked();

    /**
     * An {@code NamedExtractor} instance for {@code byte} values.
     */
    public static final ExtractorByName<ResultSet, Byte> BYTE =
            DatabaseExtractorsEx.BYTE.unchecked();


    /**
     * A {@code NamedExtractor} instance for optional {@code byte} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Byte>> OPT_BYTE =
            DatabaseExtractorsEx.OPT_BYTE.unchecked();

    /**
     * A {@code NamedExtractor} instance for {@code double} values.
     */
    public static final DoubleExtractorByName<ResultSet> DOUBLE =
            DatabaseExtractorsEx.DOUBLE.unchecked();

    /**
     * A {@code NamedExtractor} for optional {@code double} values.
     */
    public interface OptDoubleExtractorByName extends ExtractorByName<ResultSet, OptionalDouble> {
        static OptDoubleExtractorByName of(OptDoubleExtractorByName extr) {
            return extr;
        }

        /**
         * Create an extractor that applies this extractor first and then maps a function over the
         * extracted optional value.
         * @param f         the function to be mapped over the optional
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByName<ResultSet, Optional<U>> mapOpt(DoubleFunction<U> f) {
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
        default Extractor<ResultSet, OptionalDouble> bind(String name) {
            return rs -> extract(rs, name);
        }
    }

    /**
     * A {@code NamedExtractor} instance for optional {@code double} values.
     */
    public static final OptDoubleExtractorByName OPT_DOUBLE =
            DatabaseExtractorsEx.OPT_DOUBLE.unchecked();

    /**
     * An {@code NamedExtractor} instance for {@code float} values.
     */
    public static final ExtractorByName<ResultSet, Float> FLOAT =
            DatabaseExtractorsEx.FLOAT.unchecked();

    /**
     * A {@code NamedExtractor} instance for optional {@code float} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Float>> OPT_FLOAT =
            DatabaseExtractorsEx.OPT_FLOAT.unchecked();

    /**
     * A {@code NamedExtractor} instance for {@code int} values.
     */
    public static final IntExtractorByName<ResultSet> INTEGER =
            DatabaseExtractorsEx.INTEGER.unchecked();

    /**
     * A {@code NamedExtractor} for optional {@code int} values.
     */
    public interface OptIntExtractorByName extends ExtractorByName<ResultSet, OptionalInt> {
        static OptIntExtractorByName of(OptIntExtractorByName extr) {
            return extr;
        }

        /**
         * Create an extractor that applies this extractor first and then maps a function over the
         * extracted optional value.
         * @param f         the function to be mapped over the optional
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByName<ResultSet, Optional<U>> mapOpt(IntFunction<U> f) {
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
        default Extractor<ResultSet, OptionalInt> bind(String name) {
            return rs -> extract(rs, name);
        }
    }

    /**
     * A {@code NamedExtractor} instance for optional {@code int} values.
     */
    public static final OptIntExtractorByName OPT_INTEGER =
            DatabaseExtractorsEx.OPT_INTEGER.unchecked();

    /**
     * A {@code NamedExtractor} instance for {@code long} values.
     */
    public static final LongExtractorByName<ResultSet> LONG =
            DatabaseExtractorsEx.LONG.unchecked();

    /**
     * A {@code NamedExtractor} for optional {@code long} values.
     */
    public interface OptLongExtractorByName extends ExtractorByName<ResultSet, OptionalLong> {
        static OptLongExtractorByName of(OptLongExtractorByName extr) {
            return extr;
        }

        /**
         * Create an extractor that applies this extractor first and then maps a function over the
         * extracted optional value.
         * @param f         the function to be mapped over the optional
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByName<ResultSet, Optional<U>> mapOpt(LongFunction<U> f) {
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
        default Extractor<ResultSet, OptionalLong> bind(String name) {
            return rs -> extract(rs, name);
        }
    }

    /**
     * A {@code NamedExtractor} instance for optional {@code long} values.
     */
    public static final OptLongExtractorByName OPT_LONG =
            DatabaseExtractorsEx.OPT_LONG.unchecked();

    /**
     * A {@code NamedExtractor} instance for {@code short} values.
     */
    public static final ExtractorByName<ResultSet, Short> SHORT =
            DatabaseExtractorsEx.SHORT.unchecked();

    /**
     * A {@code NamedExtractor} instance for optional {@code short} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Short>> OPT_SHORT =
            DatabaseExtractorsEx.OPT_SHORT.unchecked();

    /**
     * A {@code NamedExtractor} instance for {@code string} values.
     */
    public static final ExtractorByName<ResultSet, String> STRING =
            DatabaseExtractorsEx.STRING.unchecked();

    /**
     * A {@code NamedExtractor} instance for optional {@code string} values.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_STRING =
            DatabaseExtractorsEx.OPT_STRING.unchecked();

    /**
     * A {@code NamedExtractor} instance for optional {@code string} values.
     * This converter will convert empty strings to an empty optional value.
     */
    public static final ExtractorByName<ResultSet, Optional<String>> OPT_NONEMPTY_STRING =
            DatabaseExtractorsEx.OPT_NONEMPTY_STRING.unchecked();

    /**
     * An extractor for {@link Date} values.
     */
    public static final ExtractorByName<ResultSet, Date> SQLDATE =
            DatabaseExtractorsEx.SQLDATE.unchecked();

    /**
     * An extractor for optional {@code Date} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Date>> OPT_SQLDATE =
            DatabaseExtractorsEx.OPT_SQLDATE.unchecked();


    /**
     * An extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, LocalDate> LOCALDATE =
            DatabaseExtractorsEx.LOCALDATE.unchecked();

    /**
     * An extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDate>> OPT_LOCALDATE =
            DatabaseExtractorsEx.OPT_LOCALDATE.unchecked();

    /**
     * An extractor for {@link Time} values.
     */
    public static final ExtractorByName<ResultSet, Time> SQLTIME =
            DatabaseExtractorsEx.SQLTIME.unchecked();

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Time>> OPT_SQLTIME =
            DatabaseExtractorsEx.OPT_SQLTIME.unchecked();

    /**
     * An extractor for {@link LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalTime> LOCALTIME =
            DatabaseExtractorsEx.LOCALTIME.unchecked();

    /**
     * An extractor for optional {@code LocalTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalTime>> OPT_LOCALTIME =
            DatabaseExtractorsEx.OPT_LOCALTIME.unchecked();

    /**
     * An extractor for {@link Timestamp} values.
     */
    public static final ExtractorByName<ResultSet, Timestamp> SQLTIMESTAMP =
            DatabaseExtractorsEx.SQLTIMESTAMP.unchecked();

    /**
     * An extractor for optional {@code Time} values.
     */
    public static final ExtractorByName<ResultSet, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            DatabaseExtractorsEx.OPT_SQLTIMESTAMP.unchecked();

    /**
     * An extractor for {@link LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, LocalDateTime> LOCALDATETIME =
            DatabaseExtractorsEx.LOCALDATETIME.unchecked();

    /**
     * An extractor for optional {@code LocalDateTime} values.
     */
    public static final ExtractorByName<ResultSet, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            DatabaseExtractorsEx.OPT_LOCALDATETIME.unchecked();
}
