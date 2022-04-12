package org.typemeta.context.properties;

import org.typemeta.context.extractors.byname.ExtractorByName;

import java.time.LocalDate;
import java.util.*;

/**
 * A set of extractors for extracting values from {@link Properties} objects.
 */
public abstract class PropertiesExtractors {

    private PropertiesExtractors() {}

    /**
     * A {@code Properties} extractor for {@link String} values.
     */
    public static final ExtractorByName<Properties, String> STRING =
            ExtractorByName.of(Properties::getProperty);

    /**
     * A {@code Properties} extractor for optional {@code String} values.
     */
    public static final ExtractorByName<Properties, Optional<String>> OPT_STRING =
            STRING.optional();

    /**
     * A {@code Properties} extractor for {@link Boolean} values.
     */
    public static final ExtractorByName<Properties, Boolean> BOOLEAN =
            STRING.map(s -> s == null ? null : Boolean.valueOf(s));

    /**
     * A {@code Properties} extractor for optional {@code String} values.
     */
    public static final ExtractorByName<Properties, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    /**
     * A {@code Properties} extractor for {@link Byte} values.
     */
    public static final ExtractorByName<Properties, Byte> BYTE =
            STRING.map(s -> s == null ? null : Byte.valueOf(s));

    /**
     * A {@code Properties} extractor for optional {@code String} values.
     */
    public static final ExtractorByName<Properties, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    /**
     * A {@code Properties} extractor for {@link Character} values.
     */
    public static final ExtractorByName<Properties, Character> CHAR =
            STRING.map(s -> s == null || s.isEmpty() ? null : s.charAt(0));

    /**
     * A {@code Properties} extractor for optional {@code Character} values.
     */
    public static final ExtractorByName<Properties, Optional<Character>> OPT_CHAR =
            CHAR.optional();

    /**
     * A {@code Properties} extractor for {@link Double} values.
     */
    public static final ExtractorByName<Properties, Double> DOUBLE =
            STRING.map(s -> s == null ? null : Double.valueOf(s));

    /**
     * A {@code Properties} extractor for {@link OptionalDouble} values.
     */
    public static final ExtractorByName<Properties, OptionalDouble> OPT_DOUBLE =
            STRING.map(s ->
                    s == null ? OptionalDouble.empty() : OptionalDouble.of(Double.parseDouble(s))
            );

    /**
     * A {@code Properties} extractor for {@link Float} values.
     */
    public static final ExtractorByName<Properties, Float> FLOAT =
            STRING.map(s -> s == null ? null : Float.valueOf(s));

    /**
     * A {@code Properties} extractor for optional {@code Float} values.
     */
    public static final ExtractorByName<Properties, Optional<Float>> OPT_FLOAT =
            FLOAT.optional();

    /**
     * A {@code Properties} extractor for {@link Integer} values.
     */
    public static final ExtractorByName<Properties, Integer> INTEGER =
            STRING.map(s -> s == null ? null : Integer.valueOf(s));

    /**
     * A {@code Properties} extractor for {@link OptionalInt} values.
     */
    public static final ExtractorByName<Properties, OptionalInt> OPT_INTEGER =
            STRING.map(s ->
                    s == null ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(s))
            );

    /**
     * A {@code Properties} extractor for {@link Long} values.
     */
    public static final ExtractorByName<Properties, Long> LONG =
            STRING.map(s -> s == null ? null : Long.valueOf(s));

    /**
     * A {@code Properties} extractor for {@link OptionalLong} values.
     */
    public static final ExtractorByName<Properties, OptionalLong> OPT_LONG =
            STRING.map(s ->
                    s == null ? OptionalLong.empty() : OptionalLong.of(Long.parseLong(s))
            );

    /**
     * A {@code Properties} extractor for {@link Short} values.
     */
    public static final ExtractorByName<Properties, Short> SHORT =
            STRING.map(s -> s == null ? null : Short.valueOf(s));

    /**
     * A {@code Properties} extractor for optional {@code Short} values.
     */
    public static final ExtractorByName<Properties, Optional<Short>> OPT_SHORT =
            SHORT.optional();

    /**
     * A {@code Properties} extractor for {@link LocalDate} values.
     */
    public static final ExtractorByName<Properties, LocalDate> LOCALDATE =
            STRING.map(s -> s == null ? null : LocalDate.parse(s));

    /**
     * A {@code Properties} extractor for optional {@code LocalDate} values.
     */
    public static final ExtractorByName<Properties, Optional<LocalDate>> OPT_LOCALDATE =
            LOCALDATE.optional();
}
