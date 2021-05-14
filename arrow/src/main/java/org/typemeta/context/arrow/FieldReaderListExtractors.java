package org.typemeta.context.arrow;

import org.apache.arrow.vector.complex.reader.FieldReader;
import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.extractors.byindex.ExtractorByIndex;

import java.util.*;

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
            new FieldReaderListExtractor(FieldReaderExtractors.BOOLEAN);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_BOOLEAN = BOOLEAN.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> BYTE =
            new FieldReaderListExtractor(FieldReaderExtractors.BYTE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_BYTE = BYTE.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> DOUBLE =
            new FieldReaderListExtractor(FieldReaderExtractors.DOUBLE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_DOUBLE = DOUBLE.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> FLOAT =
            new FieldReaderListExtractor(FieldReaderExtractors.FLOAT);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_FLOAT = FLOAT.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> LONG =
            new FieldReaderListExtractor(FieldReaderExtractors.LONG);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_LONG = LONG.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> SHORT =
            new FieldReaderListExtractor(FieldReaderExtractors.SHORT);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_SHORT = SHORT.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> STRING =
            new FieldReaderListExtractor(FieldReaderExtractors.STRING);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_STRING = STRING.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> LOCALDATETIME =
            new FieldReaderListExtractor(FieldReaderExtractors.LOCALDATETIME);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_LOCALDATETIME = LOCALDATETIME.optional();

    public static final ExtractorByIndex<List<FieldReader>, Boolean> LOCALDATE =
            new FieldReaderListExtractor(FieldReaderExtractors.LOCALDATE);

    public static final ExtractorByIndex<List<FieldReader>, Optional<Boolean>> OPT_LOCALDATE = LOCALDATE.optional();
}
