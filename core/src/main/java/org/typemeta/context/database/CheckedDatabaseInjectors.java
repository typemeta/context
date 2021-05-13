package org.typemeta.context.database;

import org.typemeta.context.injectors.byindex.*;

import java.sql.Date;
import java.sql.*;
import java.time.*;
import java.util.*;

public abstract class CheckedDatabaseInjectors {

    public static <T> InjectByIndex.Checked<PreparedStatement, Optional<T>, SQLException> optional(
            InjectByIndex.Checked<PreparedStatement, T, SQLException> injr
    ) {
        return (ps, n, optValue) -> injr.inject(ps, n, optValue.orElse(null));
    }

    public static <T> InjectByIndex.Checked<PreparedStatement, OptionalDouble, SQLException> optional(
            DoubleInjectByIndex.Checked<PreparedStatement, SQLException> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsDouble());
            } else {
                ps.setNull(n, Types.DOUBLE);
                return ps;
            }
        };
    }

    public static <T> InjectByIndex.Checked<PreparedStatement, OptionalInt, SQLException> optional(
            IntInjectByIndex.Checked<PreparedStatement, SQLException> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsInt());
            } else {
                ps.setNull(n, Types.INTEGER);
                return ps;
            }
        };
    }

    public static <T> InjectByIndex.Checked<PreparedStatement, OptionalLong, SQLException> optional(
            LongInjectByIndex.Checked<PreparedStatement, SQLException> injr
    ) {
        return (ps, n, optValue) -> {
            if (optValue.isPresent()) {
                return injr.inject(ps, n, optValue.getAsLong());
            } else {
                ps.setNull(n, Types.BIGINT);
                return ps;
            }
        };
    }

    public static final InjectByIndex.Checked<PreparedStatement, Boolean, SQLException> BOOLEAN =
            (PreparedStatement ps, int n, Boolean value) -> {
                if (value != null) {
                    ps.setBoolean(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Boolean>, SQLException> OPT_BOOLEAN =
            optional(BOOLEAN);

    public static final InjectByIndex.Checked<PreparedStatement, Byte, SQLException> BYTE =
            (PreparedStatement ps, int n, Byte value) -> {
                if (value != null) {
                    ps.setByte(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Byte>, SQLException> OPT_BYTE =
            optional(BYTE);

    public static final DoubleInjectByIndex.Checked<PreparedStatement, SQLException> DOUBLE =
            (PreparedStatement ps, int n, double value) -> {
                ps.setDouble(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, OptionalDouble, SQLException> OPT_DOUBLE =
            optional(DOUBLE);

    public static final InjectByIndex.Checked<PreparedStatement, Float, SQLException> FLOAT =
            (PreparedStatement ps, int n, Float value) -> {
                if (value != null) {
                    ps.setFloat(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Float>, SQLException> OPT_FLOAT =
            optional(FLOAT);

    public static final IntInjectByIndex.Checked<PreparedStatement, SQLException> INTEGER =
            (PreparedStatement ps, int n, int value) -> {
                ps.setInt(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, OptionalInt, SQLException> OPT_INTEGER =
            optional(INTEGER);

    public static final LongInjectByIndex.Checked<PreparedStatement, SQLException> LONG =
            (PreparedStatement ps, int n, long value) -> {
                ps.setLong(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, OptionalLong, SQLException> OPT_LONG =
            optional(LONG);

    public static final InjectByIndex.Checked<PreparedStatement, Short, SQLException> SHORT =
            (PreparedStatement ps, int n, Short value) -> {
                if (value != null) {
                    ps.setShort(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Short>, SQLException> OPT_SHORT =
            optional(SHORT);

    public static final InjectByIndex.Checked<PreparedStatement, String, SQLException> STRING =
            (PreparedStatement ps, int n, String value) -> {
                ps.setString(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<String>, SQLException> OPT_STRING =
            optional(STRING);

    public static final InjectByIndex.Checked<PreparedStatement, Date, SQLException> SQLDATE =
            (PreparedStatement ps, int n, Date value) -> {
                ps.setDate(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Date>, SQLException> OPT_SQLDATE =
            optional(SQLDATE);

    public static final InjectByIndex.Checked<PreparedStatement, LocalDate, SQLException> LOCALDATE =
            SQLDATE.premap(Date::valueOf);

    public static final InjectByIndex.Checked<PreparedStatement, Optional<LocalDate>, SQLException> OPT_LOCALDATE =
            optional(SQLDATE).premap(od -> od.map(Date::valueOf));

    public static final InjectByIndex.Checked<PreparedStatement, Time, SQLException> SQLTIME =
            (PreparedStatement ps, int n, Time value) -> {
                ps.setTime(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Time>, SQLException> OPT_SQLTIME =
            optional(SQLTIME);

    public static final InjectByIndex.Checked<PreparedStatement, LocalTime, SQLException> LOCALTIME =
            SQLTIME.premap(Time::valueOf);

    public static final InjectByIndex.Checked<PreparedStatement, Optional<LocalTime>, SQLException> OPT_LOCALTIME =
            optional(SQLTIME).premap(od -> od.map(Time::valueOf));

    public static final InjectByIndex.Checked<PreparedStatement, Timestamp, SQLException> SQLTIMESTAMP =
            (PreparedStatement ps, int n, Timestamp value) -> {
                ps.setTimestamp(n, value);
                return ps;
            };

    public static final InjectByIndex.Checked<PreparedStatement, Optional<Timestamp>, SQLException> OPT_SQLTIMESTAMP =
            optional(SQLTIMESTAMP);

    public static final InjectByIndex.Checked<PreparedStatement, LocalDateTime, SQLException> LOCALDATETIME =
            SQLTIMESTAMP.premap(Timestamp::valueOf);

    public static final InjectByIndex.Checked<PreparedStatement, Optional<LocalDateTime>, SQLException> OPT_LOCALDATETIME =
            optional(SQLTIMESTAMP).premap(od -> od.map(Timestamp::valueOf));
}
