package org.typemeta.context.database;

import org.typemeta.context.injectors.byindex.*;
import org.typemeta.context.utils.Exceptions;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * A set of injectors for injecting values into database {@link PreparedStatement} objects.
 * These functions catch any {@link SQLException} exceptions and rethrow as as an unchecked exception.
 */
public abstract class PreparedStatementInjectors {

    private PreparedStatementInjectors() {}

    /**
     * Convert a {@link PreparedStatement} injector into one that injects an {@link Optional} value.
     * @param injr      the injector
     * @param <T>       the injected value type
     * @return          the optional value injector
     */
    public static <T> InjectorByIndex<PreparedStatement, Optional<T>> optional(
            InjectorByIndex<PreparedStatement, T> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.get());
            } else {
                try {
                    ps.setNull(n, ps.getParameterMetaData().getParameterType(n));
                    return ps;
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            }
        };
    }

    /**
     * Convert a {@code PreparedStatement} injector for double values
     * into one that injects an {@link OptionalDouble} value.
     * @param injr      the injector
     * @return          the optional double injector
     */
    public static InjectorByIndex<PreparedStatement, OptionalDouble> optional(
            DoubleInjectorByIndex<PreparedStatement> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsDouble());
            } else {
                try {
                    ps.setNull(n, Types.DOUBLE);
                    return ps;
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            }
        };
    }

    /**
     * Convert a {@code PreparedStatement} injector for integer values
     * into one that injects an {@link OptionalInt} value.
     * @param injr      the injector
     * @return          the optional integer injector
     */
    public static InjectorByIndex<PreparedStatement, OptionalInt> optional(
            IntInjectorByIndex<PreparedStatement> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsInt());
            } else {
                try {
                    ps.setNull(n, Types.INTEGER);
                    return ps;
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            }
        };
    }

    /**
     * Convert a {@code PreparedStatement} injectorfor long values
     * into one that injects an {@link OptionalLong} value.
     * @param injr      the injector
     * @return          the optional long injector
     */
    public static InjectorByIndex<PreparedStatement, OptionalLong> optional(
            LongInjectorByIndex<PreparedStatement> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsLong());
            } else {
                try {
                    ps.setNull(n, Types.BIGINT);
                    return ps;
                } catch (SQLException ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            }
        };
    }

    /**
     * A {@code PreparedStatement} injector for {@link Boolean} values.
     */
    public static final InjectorByIndex<PreparedStatement, Boolean> BOOLEAN =
            CheckedPreparedStatementInjectors.BOOLEAN.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Boolean} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Boolean>> OPT_BOOLEAN =
            CheckedPreparedStatementInjectors.OPT_BOOLEAN.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Byte} values.
     */
    public static final InjectorByIndex<PreparedStatement, Byte> BYTE =
            CheckedPreparedStatementInjectors.BYTE.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Byte} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Byte>> OPT_BYTE =
            CheckedPreparedStatementInjectors.OPT_BYTE.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Double} values.
     */
    public static final InjectorByIndex<PreparedStatement, Double> DOUBLE =
            CheckedPreparedStatementInjectors.DOUBLE.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link OptionalDouble} values.
     */
    public static final InjectorByIndex<PreparedStatement, OptionalDouble> OPT_DOUBLE =
            CheckedPreparedStatementInjectors.OPT_DOUBLE.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Float} values.
     */
    public static final InjectorByIndex<PreparedStatement, Float> FLOAT =
            CheckedPreparedStatementInjectors.FLOAT.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Float} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Float>> OPT_FLOAT =
            CheckedPreparedStatementInjectors.OPT_FLOAT.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Integer} values.
     */
    public static final InjectorByIndex<PreparedStatement, Integer> INTEGER =
            CheckedPreparedStatementInjectors.INTEGER.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link OptionalInt} values.
     */
    public static final InjectorByIndex<PreparedStatement, OptionalInt> OPT_INTEGER =
            CheckedPreparedStatementInjectors.OPT_INTEGER.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Long} values.
     */
    public static final InjectorByIndex<PreparedStatement, Long> LONG =
            CheckedPreparedStatementInjectors.LONG.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link OptionalLong} values.
     */
    public static final InjectorByIndex<PreparedStatement, OptionalLong> OPT_LONG =
            CheckedPreparedStatementInjectors.OPT_LONG.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Short} values.
     */
    public static final InjectorByIndex<PreparedStatement, Short> SHORT =
            CheckedPreparedStatementInjectors.SHORT.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Short} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Short>> OPT_SHORT =
            CheckedPreparedStatementInjectors.OPT_SHORT.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link String} values.
     */
    public static final InjectorByIndex<PreparedStatement, String> STRING =
            CheckedPreparedStatementInjectors.STRING.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code String} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<String>> OPT_STRING =
            CheckedPreparedStatementInjectors.OPT_STRING.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code String} values.
     *  This injector will convert empty strings to an empty optional value.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<String>> OPT_EMPTY_STRING =
            (ctx, n, optVal) ->
                    optVal.map(String::isEmpty).orElse(false) ?
                            STRING.inject(ctx, n, optVal.get()) :
                            ctx;

    /**
     * A {@code PreparedStatement} injector for {@link Date} values.
     */
    public static final InjectorByIndex<PreparedStatement, Date> SQLDATE =
            CheckedPreparedStatementInjectors.SQLDATE.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Date} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Date>> OPT_SQLDATE =
            CheckedPreparedStatementInjectors.OPT_SQLDATE.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link LocalDate} values.
     */
    public static final InjectorByIndex<PreparedStatement, LocalDate> LOCALDATE =
            CheckedPreparedStatementInjectors.LOCALDATE.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalDate} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<LocalDate>> OPT_LOCALDATE =
            CheckedPreparedStatementInjectors.OPT_LOCALDATE.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Time} values.
     */
    public static final InjectorByIndex<PreparedStatement, Time> SQLTIME =
            CheckedPreparedStatementInjectors.SQLTIME.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Time} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Time>> OPT_SQLTIME =
            CheckedPreparedStatementInjectors.OPT_SQLTIME.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link LocalTime} values.
     */
    public static final InjectorByIndex<PreparedStatement, LocalTime> LOCALTIME =
            CheckedPreparedStatementInjectors.LOCALTIME.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalTime} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<LocalTime>> OPT_LOCALTIME =
            CheckedPreparedStatementInjectors.OPT_LOCALTIME.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link Timestamp} values.
     */
    public static final InjectorByIndex<PreparedStatement, Timestamp> SQLTIMESTAMP =
            CheckedPreparedStatementInjectors.SQLTIMESTAMP.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code Timestamp} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            CheckedPreparedStatementInjectors.OPT_SQLTIMESTAMP.unchecked();

    /**
     * A {@code PreparedStatement} injector for {@link LocalDateTime} values.
     */
    public static final InjectorByIndex<PreparedStatement, LocalDateTime> LOCALDATETIME =
            CheckedPreparedStatementInjectors.LOCALDATETIME.unchecked();

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalDateTime} values.
     */
    public static final InjectorByIndex<PreparedStatement, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            CheckedPreparedStatementInjectors.OPT_LOCALDATETIME.unchecked();
}
