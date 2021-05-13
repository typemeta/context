package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A specialisation of {@code Extractor} for {@code long} values.
 * @param <ENV>     the environment type
 */
@FunctionalInterface
public interface LongExtractor<ENV> extends Extractor<ENV, Long> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <ENV>     the environment type
     * @return the extractor
     */
    static <ENV> LongExtractor<ENV> of(LongExtractor<ENV> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the environment.
     * @return          the extractor
     */
    static LongExtractor<Long> id() {
        return env -> env;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <ENV>     the environment type
     * @return          the extractor
     */
    static <ENV> LongExtractor<ENV> konst(long value) {
        return env -> value;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code long} value.
     * @param env       the environment
     * @return the extracted value
     */
    long extractLong(ENV env);

    @Override
    default Long extract(ENV env) {
        return extractLong(env);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for {@code long} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return the mapped extractor
     */
    default <U> Extractor<ENV, U> mapLong(LongFunction<U> f) {
        return env -> f.apply(extractLong(env));
    }

    /**
     * A specialisation of {@code ExtractorEx} for {@code long} values.
     * @param <ENV>     the environment type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<ENV, EX extends Exception> extends Extractor.Checked<ENV, Long, EX> {
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
        static <EX extends Exception> Checked<Long, EX> id() {
            return env -> env;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, EX> konst(long value) {
            return env -> value;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code long} value.
         * @param env       the environment
         * @return          the extracted value
         */
        long extractLong(ENV env);

        @Override
        default Long extract(ENV env) {
            return extractLong(env);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for {@code long} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<ENV, U, EX> mapLong(LongFunction<U> f) {
            return env -> f.apply(extractLong(env));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongExtractor<ENV> unchecked() {
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
