package org.typemeta.context.database;

import org.typemeta.context.injectors.byindex.*;
import org.typemeta.context.utils.Exceptions;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;

/**
 * A set of database injection combinator functions.
 */
public abstract class DatabaseInjectors {

    public static <T> InjectByIndex<PreparedStatement, Optional<T>> optional(
            InjectByIndex<PreparedStatement, T> injr
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

    public static <T> InjectByIndex<PreparedStatement, OptionalDouble> optional(
            DoubleInjectByIndex<PreparedStatement> injr
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

    public static <T> InjectByIndex<PreparedStatement, OptionalInt> optional(
            IntInjectByIndex<PreparedStatement> injr
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

    public static <T> InjectByIndex<PreparedStatement, OptionalLong> optional(
            LongInjectByIndex<PreparedStatement> injr
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

    public static final InjectByIndex<PreparedStatement, Boolean> BOOLEAN =
            CheckedDatabaseInjectors.BOOLEAN.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Boolean>> OPT_BOOLEAN =
            CheckedDatabaseInjectors.OPT_BOOLEAN.unchecked();

    public static final InjectByIndex<PreparedStatement, Byte> BYTE =
            CheckedDatabaseInjectors.BYTE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Byte>> OPT_BYTE =
            CheckedDatabaseInjectors.OPT_BYTE.unchecked();

    public static final InjectByIndex<PreparedStatement, Double> DOUBLE =
            CheckedDatabaseInjectors.DOUBLE.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalDouble> OPT_DOUBLE =
            CheckedDatabaseInjectors.OPT_DOUBLE.unchecked();

    public static final InjectByIndex<PreparedStatement, Float> FLOAT =
            CheckedDatabaseInjectors.FLOAT.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Float>> OPT_FLOAT =
            CheckedDatabaseInjectors.OPT_FLOAT.unchecked();

    public static final InjectByIndex<PreparedStatement, Integer> INTEGER =
            CheckedDatabaseInjectors.INTEGER.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalInt> OPT_INTEGER =
            CheckedDatabaseInjectors.OPT_INTEGER.unchecked();

    public static final InjectByIndex<PreparedStatement, Long> LONG =
            CheckedDatabaseInjectors.LONG.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalLong> OPT_LONG =
            CheckedDatabaseInjectors.OPT_LONG.unchecked();

    public static final InjectByIndex<PreparedStatement, Short> SHORT =
            CheckedDatabaseInjectors.SHORT.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Short>> OPT_SHORT =
            CheckedDatabaseInjectors.OPT_SHORT.unchecked();

    public static final InjectByIndex<PreparedStatement, String> STRING =
            CheckedDatabaseInjectors.STRING.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<String>> OPT_STRING =
            CheckedDatabaseInjectors.OPT_STRING.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<String>> OPT_EMPTY_STRING =
            (ctx, n, optVal) ->
                    optVal.map(String::isEmpty).orElse(false) ?
                            STRING.inject(ctx, n, optVal.get()) :
                            ctx;

    public static final InjectByIndex<PreparedStatement, Date> SQLDATE =
            CheckedDatabaseInjectors.SQLDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Date>> OPT_SQLDATE =
            CheckedDatabaseInjectors.OPT_SQLDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalDate> LOCALDATE =
            CheckedDatabaseInjectors.LOCALDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalDate>> OPT_LOCALDATE =
            CheckedDatabaseInjectors.OPT_LOCALDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Time> SQLTIME =
            CheckedDatabaseInjectors.SQLTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Time>> OPT_SQLTIME =
            CheckedDatabaseInjectors.OPT_SQLTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalTime> LOCALTIME =
            CheckedDatabaseInjectors.LOCALTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalTime>> OPT_LOCALTIME =
            CheckedDatabaseInjectors.OPT_LOCALTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Timestamp> SQLTIMESTAMP =
            CheckedDatabaseInjectors.SQLTIMESTAMP.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            CheckedDatabaseInjectors.OPT_SQLTIMESTAMP.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalDateTime> LOCALDATETIME =
            CheckedDatabaseInjectors.LOCALDATETIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            CheckedDatabaseInjectors.OPT_LOCALDATETIME.unchecked();

}
