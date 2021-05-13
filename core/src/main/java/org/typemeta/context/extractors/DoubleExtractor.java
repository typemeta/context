package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.function.DoubleFunction;

/**
 * A specialisation of {@link Extractor} for {@code double} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface DoubleExtractor<ENV> extends Extractor<ENV, Double> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <ENV>     the environment type
     * @return the extractor
     */
    static <ENV> DoubleExtractor<ENV> of(DoubleExtractor<ENV> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the environment.
     * @return          the extractor
     */
    static DoubleExtractor<Double> id() {
        return env -> env;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <ENV>     the environment type
     * @return          the extractor
     */
    static <ENV> DoubleExtractor<ENV> konst(double value) {
        return env -> value;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code double} value.
     * @param env   the environment
     * @return the extracted value
     */
    double extractDouble(ENV env);

    @Override
    default Double extract(ENV env) {
        return extractDouble(env);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for {@code double} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return the mapped extractor
     */
    default <U> Extractor<ENV, U> mapDouble(DoubleFunction<U> f) {
        return env -> f.apply(extractDouble(env));
    }

    /**
     * A specialisation of {@link Extractor.Checked} for {@code double} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<ENV, EX extends Exception> extends Extractor.Checked<ENV, Double, EX> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, EX> of(Checked<ENV, EX> extr) {
            return extr;
        }

        /**
         * An extractor that simply returns the environment.
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <EX extends Exception> Checked<Double, EX> id() {
            return env -> env;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, EX> konst(double value) {
            return env -> value;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code double} value.
         * @param env       the environment
         * @return          the extracted value
         */
        double extractDouble(ENV env) throws EX;

        @Override
        default Double extract(ENV env) throws EX {
            return extractDouble(env);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for {@code double} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<ENV, U, EX> mapDouble(DoubleFunction<U> f) {
            return env -> f.apply(extractDouble(env));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default DoubleExtractor<ENV> unchecked() {
            return env -> {
                try {
                    return extract(env);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
