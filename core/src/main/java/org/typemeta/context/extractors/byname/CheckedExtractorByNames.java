package org.typemeta.context.extractors.byname;

public class CheckedExtractorByNames {

    /**
     * Create a {@link ExtractorByName.Checked} for enum values.
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
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
