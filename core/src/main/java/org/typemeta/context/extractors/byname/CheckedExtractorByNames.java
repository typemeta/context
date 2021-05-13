package org.typemeta.context.extractors.byname;

public class CheckedExtractorByNames {

    /**
     * Create a {@link ExtractorByName.Checked} for enum values.
     * @param <ENV>     the environment type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    public static <ENV, E extends Enum<E>, EX extends Exception>
    ExtractorByName.Checked<ENV, E, EX> enumExtractor(
            Class<E> enumType,
            ExtractorByName.Checked<ENV, String, EX> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }
}
