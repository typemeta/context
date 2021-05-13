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
            DatabaseInjectorsEx.BOOLEAN.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Boolean>> OPT_BOOLEAN =
            DatabaseInjectorsEx.OPT_BOOLEAN.unchecked();

    public static final InjectByIndex<PreparedStatement, Byte> BYTE =
            DatabaseInjectorsEx.BYTE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Byte>> OPT_BYTE =
            DatabaseInjectorsEx.OPT_BYTE.unchecked();

    public static final InjectByIndex<PreparedStatement, Double> DOUBLE =
            DatabaseInjectorsEx.DOUBLE.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalDouble> OPT_DOUBLE =
            DatabaseInjectorsEx.OPT_DOUBLE.unchecked();

    public static final InjectByIndex<PreparedStatement, Float> FLOAT =
            DatabaseInjectorsEx.FLOAT.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Float>> OPT_FLOAT =
            DatabaseInjectorsEx.OPT_FLOAT.unchecked();

    public static final InjectByIndex<PreparedStatement, Integer> INTEGER =
            DatabaseInjectorsEx.INTEGER.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalInt> OPT_INTEGER =
            DatabaseInjectorsEx.OPT_INTEGER.unchecked();

    public static final InjectByIndex<PreparedStatement, Long> LONG =
            DatabaseInjectorsEx.LONG.unchecked();

    public static final InjectByIndex<PreparedStatement, OptionalLong> OPT_LONG =
            DatabaseInjectorsEx.OPT_LONG.unchecked();

    public static final InjectByIndex<PreparedStatement, Short> SHORT =
            DatabaseInjectorsEx.SHORT.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Short>> OPT_SHORT =
            DatabaseInjectorsEx.OPT_SHORT.unchecked();

    public static final InjectByIndex<PreparedStatement, String> STRING =
            DatabaseInjectorsEx.STRING.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<String>> OPT_STRING =
            DatabaseInjectorsEx.OPT_STRING.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<String>> OPT_EMPTY_STRING =
            (ctx, n, optVal) ->
                    optVal.map(String::isEmpty).orElse(false) ?
                            STRING.inject(ctx, n, optVal.get()) :
                            ctx;

    public static final InjectByIndex<PreparedStatement, Date> SQLDATE =
            DatabaseInjectorsEx.SQLDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Date>> OPT_SQLDATE =
            DatabaseInjectorsEx.OPT_SQLDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalDate> LOCALDATE =
            DatabaseInjectorsEx.LOCALDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalDate>> OPT_LOCALDATE =
            DatabaseInjectorsEx.OPT_LOCALDATE.unchecked();

    public static final InjectByIndex<PreparedStatement, Time> SQLTIME =
            DatabaseInjectorsEx.SQLTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Time>> OPT_SQLTIME =
            DatabaseInjectorsEx.OPT_SQLTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalTime> LOCALTIME =
            DatabaseInjectorsEx.LOCALTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalTime>> OPT_LOCALTIME =
            DatabaseInjectorsEx.OPT_LOCALTIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Timestamp> SQLTIMESTAMP =
            DatabaseInjectorsEx.SQLTIMESTAMP.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<Timestamp>> OPT_SQLTIMESTAMP =
            DatabaseInjectorsEx.OPT_SQLTIMESTAMP.unchecked();

    public static final InjectByIndex<PreparedStatement, LocalDateTime> LOCALDATETIME =
            DatabaseInjectorsEx.LOCALDATETIME.unchecked();

    public static final InjectByIndex<PreparedStatement, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            DatabaseInjectorsEx.OPT_LOCALDATETIME.unchecked();

}
