package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.IntFunction;

/**
 * A specialisation of {@code NamedExtractor} for {@code int} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface IntExtractorByName<ENV> extends ExtractorByName<ENV, Integer> {

    /**
     * The extraction method, specialised to return an unboxed {@code int} value.
     * @param env       the environment
     * @return          the extracted value
     */
    int extractInt(ENV env, String name);

    @Override
    default Integer extract(ENV env, String name) {
        return extractInt(env, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code int} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<ENV, U> mapInt(IntFunction<U> f) {
        return (env, name) -> f.apply(extractInt(env, name));
    }

    @Override
    default IntExtractor<ENV> bind(String name) {
        return rs -> extractInt(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code int} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    interface Checked<ENV, EX extends Exception> extends ExtractorByName.Checked<ENV, Integer, EX> {
        static <ENV, EX extends Exception> Checked<ENV, EX> of(Checked<ENV, EX> extr) {
            return extr;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code int} value.
         * @param env       the environment
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        int extractInt(ENV env, String name) throws EX;

        @Override
        default Integer extract(ENV env, String name) throws EX {
            return extractInt(env, name);
        }

        /**
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code int} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<ENV, U, EX> mapInt(IntFunction<U> f) {
            return (env, name) -> f.apply(extract(env, name));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return          the unchecked extractor
         */
        default IntExtractorByName<ENV> unchecked() {
            return (env, name) -> {
                try {
                    return extractInt(env, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
