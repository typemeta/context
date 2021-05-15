package org.typemeta.context.extractors.byindex;

import org.typemeta.context.extractors.IntExtractor;
import org.typemeta.context.utils.Exceptions;

import java.util.function.IntFunction;

/**
 * A function to extract a int value from an context, given a index.
 * Essentially a specialisation of {@link ExtractorByIndex} for {@code int} values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntExtractorByIndex<CTX> extends ExtractorByIndex<CTX, Integer> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntExtractorByIndex<CTX> of(IntExtractorByIndex<CTX> extr) {
        return extr;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code int} value.
     * @param ctx       the context
     * @return          the extracted value
     */
    int extractInteger(CTX ctx, int index);

    /**
     * Extract a value of type {@code T} from the given context,
     * for the given index.
     * @param ctx       the context
     * @param index     the index
     * @return          the extracted value
     */
    @Override
    default Integer extract(CTX ctx, int index) {
        return extractInteger(ctx, index);
    }

    /**
     * Convert this extractor into another that applies a function to the result of this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, U> mapInteger(IntFunction<U> f) {
        return (ctx, index) -> f.apply(extractInteger(ctx, index));
    }

    @Override
    default IntExtractor<CTX> bind(int index) {
        return rs -> extract(rs, index);
    }

    /**
     * Variant of {@link IntExtractorByIndex} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByIndex.Checked<CTX, Integer, EX> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return extr;
        }

        /**
         * Extract a value of type {@code T} from the given context, for the given index.
         * @param ctx       the context
         * @param index     the index
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        int extractInteger(CTX ctx, int index) throws EX;

        @Override
        default Integer extract(CTX ctx, int index) throws EX {
            return extractInteger(ctx, index);
        }
        
        /**
         * A variant of the {@link ExtractorByIndex.Checked#map} method specialised for {@code int} values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByIndex.Checked<CTX, U, EX> mapInteger(IntFunction<U> f) {
            return (rs, index) -> f.apply(extract(rs, index));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default IntExtractorByIndex<CTX> unchecked() {
            return (ctx, index) -> {
                try {
                    return extractInteger(ctx, index);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
