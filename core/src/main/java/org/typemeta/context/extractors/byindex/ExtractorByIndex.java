package org.typemeta.context.extractors.byindex;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.functions.Functions;

import java.util.Optional;

/**
 * A function to extract a value from an context, given an index.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface ExtractorByIndex<CTX, T> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @param <T>       the extracted value type
     * @return          the extractor
     */
    static <CTX, T> ExtractorByIndex<CTX, T> of(ExtractorByIndex<CTX, T> extr) {
        return extr;
    }

    /**
     * Extract a value of type {@code T} from the given context, for the given index.
     * @param ctx       the context
     * @param index     the index
     * @return          the extracted value
     */
    T extract(CTX ctx, int index);

    /**
     * Convert this extractor into one that applies a function to the result of this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, U> map(Functions.F<T, U> f) {
        return (ctx, index) -> f.apply(extract(ctx, index));
    }

    /**
     * Bind this extractor to an index, giving us an {@link Extractor}.
     * @param index     the index
     * @return          the extractor
     */
    default Extractor<CTX, T> bind(int index) {
        return ctx -> extract(ctx, index);
    }

    /**
     * Convert this extractor into one that extracts optional values.
     * The optional extractor converts null values to {@code Optional.empty}.
     * @return          the extractor function for the optional value
     */
    default ExtractorByIndex<CTX, Optional<T>> optional() {
        return map(Optional::ofNullable);
    }

    /**
     * Variant of {@link ExtractorByIndex} where the extract method may throw an exception.
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
         * Extract a value of type {@code T} from the given context, for the given index.
         * @param ctx       the context
         * @param index     the index
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        T extract(CTX ctx, int index) throws EX;

        /**
         * Convert this extractor into one that applies a function to the result of this extractor.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> Checked<CTX, U, EX> map(Functions.F<T, U> f) {
            return (ctx, index) -> f.apply(extract(ctx, index));
        }

        /**
         * Bind this extractor to an index, giving us an {@link Extractor}.
         * @param index     the index
         * @return          the extractor
         */
        default Extractor.Checked<CTX, T, EX> bind(int index) {
            return ctx -> extract(ctx, index);
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
        default ExtractorByIndex<CTX, T> unchecked() {
            return (ctx, index) -> {
                try {
                    return extract(ctx, index);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
