package org.typemeta.context.properties;

import org.typemeta.context.injectors.byname.InjectorByName;

import java.time.LocalDate;
import java.util.*;

/**
 * A set injectors for injecting values into {@link Properties} objects.
 */
public abstract class PropertiesInjectors {

    private PropertiesInjectors() {}

    /**
     * A {@code Properties} injector for {@link String} values.
     */
    public static final InjectorByName<Properties, String> STRING =
            InjectorByName.ofSideEffect(Properties::setProperty);

    /**
     * A {@code Properties} injector for optional {@code String} values.
     */
    public static final InjectorByName<Properties, Optional<String>> OPT_STRING =
            STRING.optional();

    /**
     * A {@code Properties} injector for {@link Boolean} values.
     */
    public static final InjectorByName<Properties, Boolean> BOOLEAN =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@code Boolean} values.
     */
    public static final InjectorByName<Properties, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    /**
     * A {@code Properties} injector for {@link Byte} values.
     */
    public static final InjectorByName<Properties, Byte> BYTE =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@code Byte} values.
     */
    public static final InjectorByName<Properties, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    /**
     * A {@code Properties} injector for {@link Character} values.
     */
    public static final InjectorByName<Properties, Character> CHAR =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@code Character} values.
     */
    public static final InjectorByName<Properties, Optional<Character>> OPT_CHAR =
            CHAR.optional();

    /**
     * A {@code Properties} injector for {@link Double} values.
     */
    public static final InjectorByName<Properties, Double> DOUBLE =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@link OptionalDouble} values.
     */
    public static final InjectorByName<Properties, OptionalDouble> OPT_DOUBLE =
            (ctx, name, optVal) ->
                    optVal.isPresent() ?
                            STRING.inject(ctx, name, Double.toString(optVal.getAsDouble())) :
                            ctx;

    /**
     * A {@code Properties} injector for {@link Float} values.
     */
    public static final InjectorByName<Properties, Float> FLOAT =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@code Float} values.
     */
    public static final InjectorByName<Properties, Optional<Float>> OPT_FLOAT =
            FLOAT.optional();

    /**
     * A {@code Properties} injector for {@link Integer} values.
     */
    public static final InjectorByName<Properties, Integer> INTEGER =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@link OptionalInt} values.
     */
    public static final InjectorByName<Properties, OptionalInt> OPT_INTEGER =
            (ctx, name, optVal) ->
                    optVal.isPresent() ?
                            STRING.inject(ctx, name, Integer.toString(optVal.getAsInt())) :
                            ctx;

    /**
     * A {@code Properties} injector for {@link Long} values.
     */
    public static final InjectorByName<Properties, Long> LONG =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@link OptionalLong} values.
     */
    public static final InjectorByName<Properties, OptionalLong> OPT_LONG =
            (ctx, name, optVal) ->
                    optVal.isPresent() ?
                            STRING.inject(ctx, name, Double.toString(optVal.getAsLong())) :
                            ctx;

    /**
     * A {@code Properties} injector for {@link Short} values.
     */
    public static final InjectorByName<Properties, Short> SHORT =
            STRING.premap(Object::toString);

    /**
     * A {@code Properties} injector for optional {@code Short} values.
     */
    public static final InjectorByName<Properties, Optional<Short>> OPT_SHORT =
            SHORT.optional();

    /**
     * A {@code Properties} injector for {@link LocalDate} values.
     */
    public static final InjectorByName<Properties, LocalDate> LOCALDATE =
            STRING.premap(LocalDate::toString);

    /**
     * A {@code Properties} injector for optional {@code LocalDate} values.
     */
    public static final InjectorByName<Properties, Optional<LocalDate>> OPT_LOCALDATE =
            LOCALDATE.optional();
}
