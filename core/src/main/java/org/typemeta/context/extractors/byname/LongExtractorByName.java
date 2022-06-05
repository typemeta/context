package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.LongExtractor;
import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A function to extract a long value from an context, given a name.
 * Essentially a specialisation of {@link ExtractorByName} for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongExtractorByName<CTX> extends ExtractorByName<CTX, Long> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongExtractorByName<CTX> of(LongExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * Extract a long value from the given context, for the given name.
     * A variant of the {@link ExtractorByName#extract method specialised for long values.
     * @param ctx       the context
     * @return          the extracted value
     */
    long extractLong(CTX ctx, String name);

    @Override
    default Long extract(CTX ctx, String name) {
        return extractLong(ctx, name);
    }

    /**
     * Convert this extractor into one that applies a function to the result of this extractor.
     * A variant of the {@link ExtractorByName#map} method specialised for long values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<CTX, U> mapLong(LongFunction<U> f) {
        return (ctx, name) -> f.apply(extractLong(ctx, name));
    }

    @Override
    default LongExtractor<CTX> bind(String name) {
        return ctx -> extractLong(ctx, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for long values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, Long, EX> {
        /**
         * Static constructor.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return extr;
        }

        /**
         * Extract a long value from the given context, for the given name.
         * A variant of the {@link ExtractorByName.Checked#extract method specialised for long values.
         * @param ctx       the context
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        long extractLong(CTX ctx, String name) throws EX;

        @Override
        default Long extract(CTX ctx, String name) throws EX {
            return extractLong(ctx, name);
        }

        /**
         * Convert this extractor into one that applies a function to the result of this extractor.
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for long values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<CTX, U, EX> mapLong(LongFunction<U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
        }

        @Override
        default LongExtractor.Checked<CTX, EX> bind(String name) {
            return ctx -> extractLong(ctx, name);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return          the unchecked extractor
         */
        default LongExtractorByName<CTX> unchecked() {
            return (ctx, name) -> {
                try {
                    return extractLong(ctx, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
