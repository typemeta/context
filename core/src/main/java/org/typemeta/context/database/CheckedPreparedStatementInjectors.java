package org.typemeta.context.database;

import org.typemeta.context.injectors.byindex.DoubleInjectorByIndex;
import org.typemeta.context.injectors.byindex.InjectorByIndex;
import org.typemeta.context.injectors.byindex.IntInjectorByIndex;
import org.typemeta.context.injectors.byindex.LongInjectorByIndex;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * A set of injectors for injecting values into database {@link PreparedStatement} objects.
 * These injectors may throw {@link SQLException} exceptions.
 */
public abstract class CheckedPreparedStatementInjectors {

    private CheckedPreparedStatementInjectors() {}

    /**
     * Convert a {@link PreparedStatement} injector into one that injects an {@link Optional} value.
     * @param injr      the injector
     * @param <T>       the injected value type
     * @return          the optional value injector
     */
    public static <T> InjectorByIndex.Checked<PreparedStatement, Optional<T>, SQLException> optional(
            InjectorByIndex.Checked<PreparedStatement, T, SQLException> injr
    ) {
        return (ps, n, optValue) -> injr.inject(ps, n, optValue.orElse(null));
    }

    /**
     * A {@code PreparedStatement} injector for {@link Boolean} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Boolean, SQLException> BOOLEAN =
            (PreparedStatement ps, int n, Boolean value) -> {
                if (value != null) {
                    ps.setBoolean(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Boolean} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Boolean>, SQLException> OPT_BOOLEAN =
            optional(BOOLEAN);

    /**
     * A {@code PreparedStatement} injector for {@link Byte} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Byte, SQLException> BYTE =
            (PreparedStatement ps, int n, Byte value) -> {
                if (value != null) {
                    ps.setByte(n, value);
                } else {
                    ps.setNull(n, Types.TINYINT);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Byte} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Byte>, SQLException> OPT_BYTE =
            optional(BYTE);

    /**
     * A {@code PreparedStatement} injector for double values.
     */
    public static final DoubleInjectorByIndex.Checked<PreparedStatement, SQLException> DOUBLE =
            (PreparedStatement ps, int n, double value) -> {
                ps.setDouble(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for {@link OptionalDouble} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, OptionalDouble, SQLException> OPT_DOUBLE =
            (PreparedStatement ps, int n, OptionalDouble optValue) -> {
                if (optValue.isPresent()) {
                    ps.setDouble(n, optValue.getAsDouble());
                } else {
                    ps.setNull(n, Types.DOUBLE);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for {@link Float} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Float, SQLException> FLOAT =
            (PreparedStatement ps, int n, Float value) -> {
                if (value != null) {
                    ps.setFloat(n, value);
                } else {
                    ps.setNull(n, Types.BOOLEAN);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Float} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Float>, SQLException> OPT_FLOAT =
            optional(FLOAT);

    /**
     * A {@code PreparedStatement} injector for integer values.
     */
    public static final IntInjectorByIndex.Checked<PreparedStatement, SQLException> INTEGER =
            (PreparedStatement ps, int n, int value) -> {
                ps.setInt(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for {@link OptionalInt} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, OptionalInt, SQLException> OPT_INTEGER =
            (PreparedStatement ps, int n, OptionalInt optValue) -> {
                if (optValue.isPresent()) {
                    ps.setInt(n, optValue.getAsInt());
                } else {
                    ps.setNull(n, Types.DOUBLE);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for long values.
     */
    public static final LongInjectorByIndex.Checked<PreparedStatement, SQLException> LONG =
            (PreparedStatement ps, int n, long value) -> {
                ps.setLong(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for {@link OptionalLong} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, OptionalLong, SQLException> OPT_LONG =
            (PreparedStatement ps, int n, OptionalLong optValue) -> {
                if (optValue.isPresent()) {
                    ps.setDouble(n, optValue.getAsLong());
                } else {
                    ps.setNull(n, Types.DOUBLE);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for {@link Short} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Short, SQLException> SHORT =
            (PreparedStatement ps, int n, Short value) -> {
                if (value != null) {
                    ps.setShort(n, value);
                } else {
                    ps.setNull(n, Types.SMALLINT);
                }
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Short} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Short>, SQLException> OPT_SHORT =
            optional(SHORT);

    /**
     * A {@code PreparedStatement} injector for {@link String} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, String, SQLException> STRING =
            (PreparedStatement ps, int n, String value) -> {
                ps.setString(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code String} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<String>, SQLException> OPT_STRING =
            optional(STRING);

    /**
     * A {@code PreparedStatement} injector for optional {@code String} values.
     *  This injector will convert empty strings to an empty optional value.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<String>, SQLException> OPT_EMPTY_STRING =
            (ctx, n, optVal) ->
                    optVal.map(String::isEmpty).orElse(false) ?
                            STRING.inject(ctx, n, optVal.get()) :
                            ctx;

    /**
     * A {@code PreparedStatement} injector for {@link Date} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Date, SQLException> SQLDATE =
            (PreparedStatement ps, int n, Date value) -> {
                ps.setDate(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Date} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Date>, SQLException> OPT_SQLDATE =
            optional(SQLDATE);

    /**
     * A {@code PreparedStatement} injector for {@link LocalDate} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, LocalDate, SQLException> LOCALDATE =
            SQLDATE.premap(Date::valueOf);

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalDate} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<LocalDate>, SQLException> OPT_LOCALDATE =
            optional(SQLDATE).premap(od -> od.map(Date::valueOf));

    /**
     * A {@code PreparedStatement} injector for {@link Time} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Time, SQLException> SQLTIME =
            (PreparedStatement ps, int n, Time value) -> {
                ps.setTime(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Time} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Time>, SQLException> OPT_SQLTIME =
            optional(SQLTIME);

    /**
     * A {@code PreparedStatement} injector for {@link LocalTime} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, LocalTime, SQLException> LOCALTIME =
            SQLTIME.premap(Time::valueOf);

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalTime} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<LocalTime>, SQLException> OPT_LOCALTIME =
            optional(SQLTIME).premap(od -> od.map(Time::valueOf));

    /**
     * A {@code PreparedStatement} injector for {@link Timestamp} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Timestamp, SQLException> SQLTIMESTAMP =
            (PreparedStatement ps, int n, Timestamp value) -> {
                ps.setTimestamp(n, value);
                return ps;
            };

    /**
     * A {@code PreparedStatement} injector for optional {@code Timestamp} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<Timestamp>, SQLException> OPT_SQLTIMESTAMP =
            optional(SQLTIMESTAMP);

    /**
     * A {@code PreparedStatement} injector for {@link LocalDateTime} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, LocalDateTime, SQLException> LOCALDATETIME =
            SQLTIMESTAMP.premap(Timestamp::valueOf);

    /**
     * A {@code PreparedStatement} injector for optional {@code LocalDateTime} values.
     */
    public static final InjectorByIndex.Checked<PreparedStatement, Optional<LocalDateTime>, SQLException> OPT_LOCALDATETIME =
            optional(SQLTIMESTAMP).premap(od -> od.map(Timestamp::valueOf));
}
