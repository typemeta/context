package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.function.IntFunction;

/**
 * A specialisation of {@code Extractor} for {@code int} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface IntExtractor<ENV> extends Extractor<ENV, Integer> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <ENV>     the environment type
     * @return the extractor
     */
    static <ENV> IntExtractor<ENV> of(IntExtractor<ENV> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the environment.
     * @return          the extractor
     */
    static IntExtractor<Integer> id() {
        return env -> env;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <ENV>     the environment type
     * @return          the extractor
     */
    static <ENV> IntExtractor<ENV> konst(int value) {
        return env -> value;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code int} value.
     * @param env   the environment
     * @return the extracted value
     */
    int extractInt(ENV env);

    @Override
    default Integer extract(ENV env) {
        return extractInt(env);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for {@code int} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return the mapped extractor
     */
    default <U> Extractor<ENV, U> mapInt(IntFunction<U> f) {
        return env -> f.apply(extractInt(env));
    }

    /**
     * A specialisation of {@code ExtractorEx} for {@code int} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<ENV, EX extends Exception> extends Extractor.Checked<ENV, Integer, EX> {
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
        static <EX extends Exception> Checked<Integer, EX> id() {
            return env -> env;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, EX> konst(int value) {
            return env -> value;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code int} value.
         * @param env       the environment
         * @return          the extracted value
         */
        int extractInt(ENV env);

        @Override
        default Integer extract(ENV env) {
            return extractInt(env);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for {@code int} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<ENV, U, EX> mapInt(IntFunction<U> f) {
            return env -> f.apply(extractInt(env));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default IntExtractor<ENV> unchecked() {
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
