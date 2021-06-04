package org.typemeta.context.arrow;

import org.apache.arrow.vector.complex.reader.FieldReader;
import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.extractors.byindex.ExtractorByIndex;

import java.time.*;
import java.util.*;

/**
 * A set of extractors for extracting values from a list of {@link FieldReader} objects.
 */
public class FieldReaderListExtractors {

    public static final class FieldReaderListExtractor<T> implements ExtractorByIndex<List<FieldReader>, T> {
        private final Extractor<FieldReader, T> extractor;

        public FieldReaderListExtractor(Extractor<FieldReader, T> extractor) {
            this.extractor = extractor;
        }

        @Override
        public T extract(List<FieldReader> fieldReaders, int index) {
            return extractor.extract(fieldReaders.get(index));
        }

        @Override
        public ExtractorByIndex<List<FieldReader>, Optional<T>> optional() {
            return (frs, i) -> {
                final FieldReader fr = frs.get(i);
                if (fr.isSet()) {
                    return Optional.of(extractor.extract(fr));
                } else {
                    return Optional.empty();
                }
            };
        }
    }

    public static final ExtractorByIndex<List<FieldReader>, Boolean> BOOLEAN =
            new FieldReaderListExtractor<>(FieldReaderExtractors.BOOLEAN);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_BOOLEAN =
            BOOLEAN.optional();

    public static final ExtractorByIndex<List<FieldReader>, Byte> BYTE =
            new FieldReaderListExtractor<>(FieldReaderExtractors.BYTE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Byte>> OPT_BYTE =
            BYTE.optional();

    public static final ExtractorByIndex<List<FieldReader>, Character> CHAR =
            new FieldReaderListExtractor<>(FieldReaderExtractors.CHAR);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Character>> OPT_CHAR =
            CHAR.optional();

    public static final ExtractorByIndex<List<FieldReader>, Double> DOUBLE =
            new FieldReaderListExtractor<>(FieldReaderExtractors.DOUBLE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Double>> OPT_DOUBLE =
            DOUBLE.optional();

    public static final ExtractorByIndex<List<FieldReader>, Float> FLOAT =
            new FieldReaderListExtractor<>(FieldReaderExtractors.FLOAT);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Float>> OPT_FLOAT =
            FLOAT.optional();

    public static final ExtractorByIndex<List<FieldReader>, Integer> INTEGER =
            new FieldReaderListExtractor<>(FieldReaderExtractors.INTEGER);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Integer>> OPT_INTEGER =
            INTEGER.optional();

    public static final ExtractorByIndex<List<FieldReader>, Long> LONG =
            new FieldReaderListExtractor<>(FieldReaderExtractors.LONG);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Long>> OPT_LONG =
            LONG.optional();

    public static final ExtractorByIndex<List<FieldReader>, Short> SHORT =
            new FieldReaderListExtractor<>(FieldReaderExtractors.SHORT);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Short>> OPT_SHORT =
            SHORT.optional();

    public static final ExtractorByIndex<List<FieldReader>, String> STRING =
            new FieldReaderListExtractor<>(FieldReaderExtractors.STRING);

    public static final ExtractorByIndex<List<FieldReader>, Optional<String>> OPT_STRING =
            STRING.optional();

    public static final ExtractorByIndex<List<FieldReader>, LocalDateTime> LOCALDATETIME =
            new FieldReaderListExtractor<>(FieldReaderExtractors.LOCALDATETIME);

    public static final ExtractorByIndex<List<FieldReader>, Optional<LocalDateTime>> OPT_LOCALDATETIME =
            LOCALDATETIME.optional();

    public static final ExtractorByIndex<List<FieldReader>, LocalDate> LOCALDATE =
            new FieldReaderListExtractor<>(FieldReaderExtractors.LOCALDATE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<LocalDate>> OPT_LOCALDATE =
            LOCALDATE.optional();
}
