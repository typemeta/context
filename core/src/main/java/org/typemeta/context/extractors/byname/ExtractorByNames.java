package org.typemeta.context.extractors.byname;

public class ExtractorByNames {

    /**
     * Create a {@code NamedExtractor} for enum values.
     * @param <ENV>     the environment type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <ENV, E extends Enum<E>> ExtractorByName<ENV, E> enumExtractor(
            Class<E> enumType,
            ExtractorByName<ENV, String> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }
}
