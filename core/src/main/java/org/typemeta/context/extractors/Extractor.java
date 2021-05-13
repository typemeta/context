package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.function.Function;

/**
 * A function to extract a value from an environment.
 * @param <ENV>     the environment type
 * @param <T>       the extracted value type
 */
@FunctionalInterface
public interface Extractor<ENV, T> {

    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <ENV>     the environment type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <ENV, T> Extractor<ENV, T> of(Extractor<ENV, T> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the environment.
     * @param <ENV>     the environment type
     * @return          the extractor
     */
    static <ENV> Extractor<ENV, ENV> id() {
        return env -> env;
    }

    /**
     * An extractor that always returns the same value.
     * @param t         the value
     * @param <ENV>     environment type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <ENV, T> Extractor<ENV, T> konst(T t) {
        return env -> t;
    }

    /**
     * Extract a value of type {@code T} from the given environment.
     * @param env       the environment value
     * @return          the extracted value
     */
    T extract(ENV env);

    /**
     * Map a function over this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> Extractor<ENV, U> map(Functions.F<T, U> f) {
        return rs -> f.apply(extract(rs));
    }

    /**
     * Flatmap a function over this extractor.
     * @param f         the function
     * @param <U>       the extractor value type returned by the function
     * @return          the new extractor
     */
    default <U> Extractor<ENV, U> flatMap(Functions.F<T, Extractor<ENV, U>> f) {
        return rs -> f.apply(this.extract(rs)).extract(rs);
    }

    /**
     * A function to extract a value from an environment, which may throw a checked exception.
     * @param <ENV>     the environment type
     * @param <T>       the extracted value type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<ENV, T, EX extends Exception> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <ENV>     the environment type
         * @param <T>       the extracted value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, T, EX extends Exception> Checked<ENV, T, EX> of(Checked<ENV, T, EX> extr) {
            return extr;
        }

        /**
         * An extractor that simply returns the environment.
         * @param <ENV>     the environment type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, EX extends Exception> Checked<ENV, ENV, EX> id() {
            return env -> env;
        }

        /**
         * An extractor that always returns the same value.
         * @param value     the value
         * @param <ENV>     environment type
         * @param <T>       the extracted value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <ENV, T, EX extends Exception> Checked<ENV, T, EX> konst(T value) {
            return env -> value;
        }

        /**
         * Extract a value of type {@code T} from the given env.
         * @param env       the environment value
         * @return          the extracted value
         */
        T extract(ENV env) throws EX;

        /**
         * Map a function over this extractor.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> Checked<ENV, U, EX> map(Function<T, U> f) {
            return rs -> f.apply(extract(rs));
        }

        /**
         * Flatmap a function over this extractor.
         * @param f         the function
         * @param <U>       the extractor value type returned by the function
         * @return          the flatmapped extractor
         */
        default <U> Checked<ENV, U, EX> flatMap(Functions.F<T, Checked<ENV, U, EX>> f) {
            return rs -> f.apply(this.extract(rs)).extract(rs);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default Extractor<ENV, T> unchecked() {
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
