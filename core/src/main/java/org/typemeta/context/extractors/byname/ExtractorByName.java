package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.functions.Functions;

import java.util.Optional;

/**
 * A function to extract a value from a context, given a name.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface ExtractorByName<CTX, T> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <CTX, T> ExtractorByName<CTX, T> of(ExtractorByName<CTX, T> extr) {
        return extr;
    }

    /**
     * An extractor that returns a constant value.
     * @param t         the value
     * @param <CTX>     context type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <CTX, T> ExtractorByName<CTX, T> konst(T t) {
        return (ctx, s) -> t;
    }

    /**
     * Extract a value of type {@code T} from the given context,
     * for the given column name.
     * @param ctx       the context
     * @param name      the name
     * @return          the extracted value
     */
    T extract(CTX ctx, String name);

    /**
     * Convert this extractor into one that applies a function to the result of this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByName<CTX, U> map(Functions.F<T, U> f) {
        return (ctx, name) -> f.apply(extract(ctx, name));
    }

    /**
     * Flatmap a function over this extractor.
     * @param f         the function
     * @param <U>       the extractor value type returned by the function
     * @return          the new extractor
     */
    default <U> ExtractorByName<CTX, U> flatMap(Functions.F<T, ExtractorByName<CTX, U>> f) {
        return (ctx, name) -> f.apply(this.extract(ctx, name)).extract(ctx, name);
    }

    /**
     * Construct an extractor that transforms the context that this extractor applies to,
     * by applying a function to the context before the extractor is applied.
     * @param f         the function
     * @param <CTX2>    the transformed context type
     * @return          the new extractor
     */
    default <CTX2> ExtractorByName<CTX2, T> mapContext(Functions.F<CTX2, CTX> f) {
        return (ctx2, name) -> extract(f.apply(ctx2), name);
    }

    /**
     * Bind this extractor to a name, giving us an {@link Extractor}.
     * @param name      the column name
     * @return          the extractor
     */
    default Extractor<CTX, T> bind(String name) {
        return ctx -> extract(ctx, name);
    }

    /**
     * Convert this extractor into one that extracts optional values.
     * The optional extractor converts null values to {@code Optional.empty}.
     * @return          the extractor function for the optional value
     */
    default ExtractorByName<CTX, Optional<T>> optional() {
        return map(Optional::ofNullable);
    }

    /**
     * Variant of {@link ExtractorByName} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <T>       the value type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, T, EX extends Exception> {
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> of(Checked<CTX, T, EX> extr) {
            return  extr;
        }

        /**
         * Extract a value of type {@code T} from the given context,
         * for the given column name.
         * @param ctx       the context
         * @param name      the name
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        T extract(CTX ctx, String name) throws EX;

        /**
         * Convert this extractor into one that applies a function to the result of this extractor.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> Checked<CTX, U, EX> map(Functions.F<T, U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
        }

        /**
         * Flatmap a function over this extractor.
         * @param f         the function
         * @param <U>       the extractor value type returned by the function
         * @return          the new extractor
         */
        default <U> Checked<CTX, U, EX> flatMap(Functions.F<T, Checked<CTX, U, EX>> f) {
            return (ctx, name) -> f.apply(this.extract(ctx, name)).extract(ctx, name);
        }

        /**
         * Construct an extractor that transforms the context that this extractor applies to,
         * by applying a function to the context before the extractor is applied.
         * @param f         the function
         * @param <CTX2>    the transformed context type
         * @return          the new extractor
         */
        default <CTX2> Checked<CTX2, T, EX> mapContext(Functions.F<CTX2, CTX> f) {
            return (ctx2, name) -> extract(f.apply(ctx2), name);
        }

        /**
         * Bind this extractor to a name, giving us an {@link Extractor}.
         * @param name      the column name
         * @return          the extractor
         */
        default Extractor.Checked<CTX, T, EX> bind(String name) {
            return ctx -> extract(ctx, name);
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
        default ExtractorByName<CTX, T> unchecked() {
            return (ctx, name) -> {
                try {
                    return extract(ctx, name);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
