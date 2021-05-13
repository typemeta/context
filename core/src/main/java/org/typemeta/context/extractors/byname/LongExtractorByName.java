package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A specialisation of {@code NamedExtractor} for {@code long} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface LongExtractorByName<ENV> extends ExtractorByName<ENV, Long> {

    /**
     * The extraction method, specialised to return an unboxed {@code long} value.
     * @param env       the environment
     * @return          the extracted value
     */
    long extractLong(ENV env, String name);

    @Override
    default Long extract(ENV env, String name) {
        return extractLong(env, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code long} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<ENV, U> mapLong(LongFunction<U> f) {
        return (env, name) -> f.apply(extractLong(env, name));
    }

    @Override
    default LongExtractor<ENV> bind(String name) {
        return rs -> extractLong(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code long} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    interface Checked<ENV, EX extends Exception> extends ExtractorByName.Checked<ENV, Long, EX> {
        static <ENV, EX extends Exception> Checked<ENV, EX> of(Checked<ENV, EX> extr) {
            return extr;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code long} value.
         * @param env       the environment
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        long extractLong(ENV env, String name) throws EX;

        @Override
        default Long extract(ENV env, String name) throws EX {
            return extractLong(env, name);
        }

        /**
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code long} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<ENV, U, EX> mapLong(LongFunction<U> f) {
            return (env, name) -> f.apply(extract(env, name));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return          the unchecked extractor
         */
        default LongExtractorByName<ENV> unchecked() {
            return (env, name) -> {
                try {
                    return extractLong(env, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
