package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.Optional;
import java.util.function.Function;

/**
 * A function to extract a value from an context.
 * @param <CTX>     the context type
 * @param <T>       the extracted value type
 */
@FunctionalInterface
public interface Extractor<CTX, T> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <CTX, T> Extractor<CTX, T> of(Extractor<CTX, T> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the context.
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> Extractor<CTX, CTX> id() {
        return ctx -> ctx;
    }

    /**
     * An extractor that always returns the same value.
     * @param t         the value
     * @param <CTX>     context type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <CTX, T> Extractor<CTX, T> konst(T t) {
        return ctx -> t;
    }

    /**
     * Extract a value of type {@code T} from the given context.
     * @param ctx       the context value
     * @return          the extracted value
     */
    T extract(CTX ctx);

    /**
     * Map a function over this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> Extractor<CTX, U> map(Functions.F<T, U> f) {
        return rs -> f.apply(extract(rs));
    }

    /**
     * Flatmap a function over this extractor.
     * @param f         the function
     * @param <U>       the extractor value type returned by the function
     * @return          the new extractor
     */
    default <U> Extractor<CTX, U> flatMap(Functions.F<T, Extractor<CTX, U>> f) {
        return rs -> f.apply(this.extract(rs)).extract(rs);
    }

    /**
     * Convert this extractor into one that extracts optional values.
     * The optional extractor converts null values to {@code Optional.empty}.
     * @return          the extractor function for the optional value
     */
    default Extractor<CTX, Optional<T>> optional() {
        return map(Optional::ofNullable);
    }

    /**
     * A function to extract a value from an context, which may throw a checked exception.
     * @param <CTX>     the context type
     * @param <T>       the extracted value type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, T, EX extends Exception> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <T>       the extracted value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> of(Checked<CTX, T, EX> extr) {
            return extr;
        }

        /**
         * An extractor that simply returns the context.
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, CTX, EX> id() {
            return ctx -> ctx;
        }

        /**
         * An extractor that always returns the same value.
         * @param value     the value
         * @param <CTX>     context type
         * @param <T>       the extracted value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> konst(T value) {
            return ctx -> value;
        }

        /**
         * Extract a value of type {@code T} from the given ctx.
         * @param ctx       the context value
         * @return          the extracted value
         */
        T extract(CTX ctx) throws EX;

        /**
         * Map a function over this extractor.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> Checked<CTX, U, EX> map(Function<T, U> f) {
            return rs -> f.apply(extract(rs));
        }

        /**
         * Flatmap a function over this extractor.
         * @param f         the function
         * @param <U>       the extractor value type returned by the function
         * @return          the new extractor
         */
        default <U> Checked<CTX, U, EX> flatMap(Functions.F<T, Checked<CTX, U, EX>> f) {
            return rs -> f.apply(this.extract(rs)).extract(rs);
        }

        /**
         * Convert this extractor into one that extracts optional values.
         * The optional extractor converts null values to {@code Optional.empty}.
         * @return          the extractor function for the optional value
         */
        default Checked<CTX, Optional<T>, EX> optional() {
            return map(Optional::ofNullable);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default Extractor<CTX, T> unchecked() {
            return ctx -> {
                try {
                    return extract(ctx);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
