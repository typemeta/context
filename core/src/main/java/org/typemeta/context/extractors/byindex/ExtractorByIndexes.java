package org.typemeta.context.extractors.byindex;

public abstract class ExtractorByIndexes {

    private ExtractorByIndexes() {}

    /**
     * Create a {@code ExtractorByIndex} for enum values.
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum extractor
     */
    static <CTX, E extends Enum<E>> ExtractorByIndex<CTX, E> enumExtractor(
            Class<E> enumType,
            ExtractorByIndex<CTX, String> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }
}
