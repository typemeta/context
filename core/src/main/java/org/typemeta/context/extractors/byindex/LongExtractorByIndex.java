package org.typemeta.context.extractors.byindex;

import org.typemeta.context.extractors.LongExtractor;
import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A function to extract a value from an context, given an index.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongExtractorByIndex<CTX> extends ExtractorByIndex<CTX, Long> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongExtractorByIndex<CTX> of(LongExtractorByIndex<CTX> extr) {
        return extr;
    }

    /**
     * The extraction method, specialised to return an unboxed long value.
     * A variant of the {@link ExtractorByIndex#extract} method specialised for long values.
     * @param ctx       the context
     * @return          the extracted value
     */
    long extractLong(CTX ctx, int index);

    /**
     * Extract a long value from the given context, for the given index.
     * @param ctx       the context
     * @param index     the index
     * @return          the extracted value
     */
    @Override
    default Long extract(CTX ctx, int index) {
        return extractLong(ctx, index);
    }

    /**
     * Convert this extractor into one that applies a function to the result of this extractor.
     * A variant of the {@link ExtractorByIndex#map} method specialised for long values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, U> mapLong(LongFunction<U> f) {
        return (ctx, index) -> f.apply(extractLong(ctx, index));
    }

    @Override
    default LongExtractor<CTX> bind(int index) {
        return ctx -> extractLong(ctx, index);
    }

    /**
     * Variant of {@link LongExtractorByIndex} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByIndex.Checked<CTX, Long, EX> {
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
         * Extract a long value from the given context, for the given index.
         * A variant of the {@link ExtractorByIndex.Checked#extract} method specialised for long values.
         * @param ctx       the context
         * @param index     the index
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        long extractLong(CTX ctx, int index) throws EX;

        @Override
        default Long extract(CTX ctx, int index) throws EX {
            return extractLong(ctx, index);
        }

        /**
         * Convert this extractor into one that applies a function to the result of this extractor.
         * A variant of the {@link ExtractorByIndex.Checked#map} method specialised for long values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByIndex.Checked<CTX, U, EX> mapLong(LongFunction<U> f) {
            return (ctx, index) -> f.apply(extract(ctx, index));
        }

        @Override
        default LongExtractor.Checked<CTX, EX> bind(int index) {
            return ctx -> extractLong(ctx, index);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongExtractorByIndex<CTX> unchecked() {
            return (ctx, index) -> {
                try {
                    return extractLong(ctx, index);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
