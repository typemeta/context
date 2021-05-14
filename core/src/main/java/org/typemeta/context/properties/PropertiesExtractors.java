package org.typemeta.context.properties;

import org.typemeta.context.extractors.byname.*;

import java.util.*;

public class PropertiesExtractors {
    public static final ExtractorByName<Properties, String> STRING = ExtractorByName.of(Properties::getProperty);

    public static final ExtractorByName<Properties, Boolean> BOOLEAN =
            STRING.map(s -> s == null ? null : Boolean.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    public static final ExtractorByName<Properties, Byte> BYTE =
            STRING.map(s -> s == null ? null : Byte.valueOf(s));

    public static final ExtractorByName<Properties, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    public static final ExtractorByName<Properties, Double> DOUBLE =
            STRING.map(s -> s == null ? null : Double.parseDouble(s));

    public static final ExtractorByName<Properties, OptionalDouble> OPT_DOUBLE =
            STRING.map(s ->
                    s == null ? OptionalDouble.empty() : OptionalDouble.of(Double.parseDouble(s))
            );
}
