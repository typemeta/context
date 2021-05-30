package org.typemeta.context.properties;

import org.typemeta.context.extractors.byname.ExtractorByName;

import java.util.*;

public abstract class PropertiesExtractors {
    public static final ExtractorByName<Properties, String> STRING =
            ExtractorByName.of(Properties::getProperty);

    public static final ExtractorByName<Properties, Optional<String>> OPT_STRING =
            STRING.optional();

    public static final ExtractorByName<Properties, Boolean> BOOLEAN =
            STRING.map(s -> s == null ? null : Boolean.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    public static final ExtractorByName<Properties, Byte> BYTE =
            STRING.map(s -> s == null ? null : Byte.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    public static final ExtractorByName<Properties, Character> CHAR =
            STRING.map(s -> s == null || s.isEmpty() ? null : Character.valueOf(s.charAt(0)));

    public static final ExtractorByName<Properties, Optional<Character>> OPT_CHAR =
            CHAR.optional();

    public static final ExtractorByName<Properties, Double> DOUBLE =
            STRING.map(s -> s == null ? null : Double.valueOf(s));

    public static final ExtractorByName<Properties, OptionalDouble> OPT_DOUBLE =
            STRING.map(s ->
                    s == null ? OptionalDouble.empty() : OptionalDouble.of(Double.parseDouble(s))
            );

    public static final ExtractorByName<Properties, Float> FLOAT =
            STRING.map(s -> s == null ? null : Float.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Float>> OPT_FLOAT =
            FLOAT.optional();

    public static final ExtractorByName<Properties, Integer> INTEGER =
            STRING.map(s -> s == null ? null : Integer.valueOf(s));

    public static final ExtractorByName<Properties, OptionalInt> OPT_INTEGER =
            STRING.map(s ->
                    s == null ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(s))
            );

    public static final ExtractorByName<Properties, Long> LONG =
            STRING.map(s -> s == null ? null : Long.valueOf(s));

    public static final ExtractorByName<Properties, OptionalLong> OPT_LONG =
            STRING.map(s ->
                    s == null ? OptionalLong.empty() : OptionalLong.of(Long.parseLong(s))
            );

    public static final ExtractorByName<Properties, Short> SHORT =
            STRING.map(s -> s == null ? null : Short.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Short>> OPT_SHORT =
            SHORT.optional();
}
