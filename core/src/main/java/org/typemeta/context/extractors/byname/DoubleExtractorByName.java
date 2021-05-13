package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.DoubleFunction;

/**
 * A specialisation of {@link ExtractorByName} for {@code double} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface DoubleExtractorByName<ENV> extends ExtractorByName<ENV, Double> {

    /**
     * The extraction method, specialised to return an unboxed {@code double} value.
     * @param env       the environment
     * @return          the extracted value
     */
    double extractDouble(ENV env, String name);

    @Override
    default Double extract(ENV env, String name) {
        return extractDouble(env, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code double} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<ENV, U> mapDouble(DoubleFunction<U> f) {
        return (env, name) -> f.apply(extractDouble(env, name));
    }

    @Override
    default DoubleExtractor<ENV> bind(String name) {
        return rs -> extractDouble(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code double} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    interface Checked<ENV, EX extends Exception> extends ExtractorByName.Checked<ENV, Double, EX> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, EX> of(Checked<ENV, EX> extr) {
            return extr;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code double} value.
         * @param env       the environment
         * @return          the extracted double value
         * @throws EX       if the extraction fails
         */
        double extractDouble(ENV env, String name) throws EX;

        @Override
        default Double extract(ENV env, String name) throws EX {
            return extractDouble(env, name);
        }

        /**
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code double} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<ENV, U, EX> mapDouble(DoubleFunction<U> f) {
            return (env, name) -> f.apply(extract(env, name));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return the unchecked extractor
         */
        default DoubleExtractorByName<ENV> unchecked() {
            return (env, name) -> {
                try {
                    return extractDouble(env, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
