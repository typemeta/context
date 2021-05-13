package org.typemeta.context.extractors.byname;

public class ExtractorByNames {

    /**
     * Create a {@code ExtractorByName} for enum values.
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <CTX, E extends Enum<E>> ExtractorByName<CTX, E> enumExtractor(
            Class<E> enumType,
            ExtractorByName<CTX, String> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }

}
