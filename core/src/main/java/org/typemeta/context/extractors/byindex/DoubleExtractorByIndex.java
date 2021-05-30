package org.typemeta.context.extractors.byindex;

import org.typemeta.context.extractors.DoubleExtractor;
import org.typemeta.context.utils.Exceptions;

import java.util.function.DoubleFunction;

/**
 * A function to extract a double value from an context, given an index.
 * Essentially a specialisation of {@link ExtractorByIndex} for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleExtractorByIndex<CTX> extends ExtractorByIndex<CTX, Double> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleExtractorByIndex<CTX> of(DoubleExtractorByIndex<CTX> extr) {
        return extr;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code double} value.
     * @param ctx       the context
     * @return          the extracted value
     */
    double extractDouble(CTX ctx, int index);

    /**
     * Extract a value of type {@code T} from the given context,
     * for the given index.
     * @param ctx       the context
     * @param index     the index
     * @return          the extracted value
     */
    @Override
    default Double extract(CTX ctx, int index) {
        return extractDouble(ctx, index);
    }

    /**
     * Convert this extractor into another that applies a function to the result of this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, U> mapDouble(DoubleFunction<U> f) {
        return (ctx, index) -> f.apply(extractDouble(ctx, index));
    }

    @Override
    default DoubleExtractor<CTX> bind(int index) {
        return rs -> extract(rs, index);
    }

    /**
     * Variant of {@link DoubleExtractorByIndex} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByIndex.Checked<CTX, Double, EX> {
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
        double extractDouble(CTX ctx, int index) throws EX;

        @Override
        default Double extract(CTX ctx, int index) throws EX {
            return extractDouble(ctx, index);
        }

        /**
         * A variant of the {@link ExtractorByIndex.Checked#map} method specialised for {@code double} values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByIndex.Checked<CTX, U, EX> mapDouble(DoubleFunction<U> f) {
            return (rs, index) -> f.apply(extract(rs, index));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default DoubleExtractorByIndex<CTX> unchecked() {
            return (ctx, index) -> {
                try {
                    return extractDouble(ctx, index);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
