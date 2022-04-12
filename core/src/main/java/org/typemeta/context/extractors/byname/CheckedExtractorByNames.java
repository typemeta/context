package org.typemeta.context.extractors.byname;

public abstract class CheckedExtractorByNames {

    private CheckedExtractorByNames() {}

    /**
     * Create a {@link ExtractorByName.Checked} for enum values.
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum extractor
     */
    public static <CTX, E extends Enum<E>, EX extends Exception>
    ExtractorByName.Checked<CTX, E, EX> enumExtractor(
            Class<E> enumType,
            ExtractorByName.Checked<CTX, String, EX> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }
}
