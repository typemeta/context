package org.typemeta.context.properties;

import org.typemeta.context.injectors.byname.InjectorByName;

import java.util.*;

public abstract class PropertiesInjectors {
    public static final InjectorByName<Properties, String> STRING =
            InjectorByName.of((props, key, value) -> {
                props.setProperty(key, value);
                return props;
            });

    public static final InjectorByName<Properties, Optional<String>> OPT_STRING =
            STRING.optional();

    public static final InjectorByName<Properties, Boolean> BOOLEAN =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    public static final InjectorByName<Properties, Byte> BYTE =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    public static final InjectorByName<Properties, Character> CHAR =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Character>> OPT_CHAR =
            CHAR.optional();

    public static final InjectorByName<Properties, Double> DOUBLE =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Double>> OPT_DOUBLE =
            DOUBLE.optional();

    public static final InjectorByName<Properties, Float> FLOAT =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Float>> OPT_FLOAT =
            FLOAT.optional();

    public static final InjectorByName<Properties, Integer> INTEGER =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Integer>> OPT_INTEGER =
            INTEGER.optional();

    public static final InjectorByName<Properties, Long> LONG =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Long>> OPT_LONG =
            LONG.optional();

    public static final InjectorByName<Properties, Short> SHORT =
            STRING.premap(Object::toString);

    public static final InjectorByName<Properties, Optional<Short>> OPT_SHORT =
            SHORT.optional();
}
