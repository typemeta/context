package org.typemeta.context.extractors.byindex;

public abstract class CheckedExtractorByIndexes {

    private CheckedExtractorByIndexes() {}

    /**
     * Create a {@link ExtractorByIndex.Checked} for enum values.
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    public static <CTX, E extends Enum<E>, EX extends Exception>
    ExtractorByIndex.Checked<CTX, E, EX> enumExtractor(
            Class<E> enumType,
            ExtractorByIndex.Checked<CTX, String, EX> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }
}
