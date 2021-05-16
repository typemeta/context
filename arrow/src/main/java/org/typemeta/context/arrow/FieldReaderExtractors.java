package org.typemeta.context.arrow;

import org.apache.arrow.vector.complex.reader.FieldReader;
import org.apache.arrow.vector.util.Text;
import org.typemeta.context.extractors.*;

import java.time.*;
import java.util.*;

/**
 * Extractors for reading values from an Arrow Flight {@link FieldReader}.
 */
public abstract class FieldReaderExtractors {

    /**
     * A combinator function to convert a {@link Extractor} into one for {@link Optional} values.
     * @param extr      the extractor function for the value type
     * @param <T>       the value type
     * @return          the extractor function for the optional value
     */
    public static <T> Extractor<FieldReader, Optional<T>> optional(Extractor<FieldReader, T> extr) {
        return fr -> {
            if (fr.isSet()) {
                return Optional.of(extr.extract(fr));
            } else {
                return Optional.empty();
            }
        };
    }

    public static final Extractor<FieldReader, Boolean> BOOLEAN = FieldReader::readBoolean;
    public static final Extractor<FieldReader, Optional<Boolean>> OPT_BOOLEAN = optional(BOOLEAN);

    public static final Extractor<FieldReader, Byte> BYTE = FieldReader::readByte;
    public static final Extractor<FieldReader, Optional<Byte>> OPT_BYTE = optional(BYTE);

    public static final Extractor<FieldReader, Character> CHAR = FieldReader::readCharacter;
    public static final Extractor<FieldReader, Optional<Character>> OPT_CHAR = optional(CHAR);

    public static final DoubleExtractor<FieldReader> DOUBLE = FieldReader::readDouble;
    public static final OptDoubleExtractor<FieldReader> OPT_DOUBLE =
            fr -> {
                if (fr.isSet()) {
                    return OptionalDouble.of(DOUBLE.extract(fr));
                } else {
                    return OptionalDouble.empty();
                }
            };

    public static final Extractor<FieldReader, Float> FLOAT = FieldReader::readFloat;
    public static final Extractor<FieldReader, Optional<Float>> OPT_FLOAT = optional(FLOAT);

    public static final IntExtractor<FieldReader> INT = FieldReader::readInteger;
    public static final OptIntExtractor<FieldReader> OPT_INT =
            fr -> {
                if (fr.isSet()) {
                    return OptionalInt.of(INT.extract(fr));
                } else {
                    return OptionalInt.empty();
                }
            };

    public static final LongExtractor<FieldReader> LONG = FieldReader::readLong;
    public static final OptLongExtractor<FieldReader> OPT_LONG =
            fr -> {
                if (fr.isSet()) {
                    return OptionalLong.of(LONG.extract(fr));
                } else {
                    return OptionalLong.empty();
                }
            };

    public static final Extractor<FieldReader, Short> SHORT = FieldReader::readShort;
    public static final Extractor<FieldReader, Optional<Short>> OPT_SHORT = optional(SHORT);

    public static final Extractor<FieldReader, String> STRING = Extractor.of(FieldReader::readText).map(Text::toString);
    public static final Extractor<FieldReader, Optional<String>> OPT_STRING = optional(STRING);

    /**
     * An extractor for {@link LocalDateTime} values.
     */
    public static final Extractor<FieldReader, LocalDateTime> LOCALDATETIME = FieldReader::readLocalDateTime;

    /**
     * An extractor for optional {@code LocalDate} values.
     */
    public static final Extractor<FieldReader, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            optional(LOCALDATETIME);

    /**
     * An extractor for {@link LocalDate} values.
     */
    public static final Extractor<FieldReader, LocalDate> LOCALDATE = LOCALDATETIME.map(LocalDateTime::toLocalDate);

    /**
     * An extractor for optional {@code LocalDate} values.
     */
    public static final Extractor<FieldReader, Optional<LocalDate>> OPT_LOCALDATE =
            optional(LOCALDATE);

}
