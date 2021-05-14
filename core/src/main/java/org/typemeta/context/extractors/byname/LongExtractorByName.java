package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A specialisation of {@code ExtractorByName} for {@code long} values.
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
     * The extraction method, specialised to return an unboxed {@code long} value.
     * @param ctx       the context
     * @return          the extracted value
     */
    long extractLong(CTX ctx, String name);

    @Override
    default Long extract(CTX ctx, String name) {
        return extractLong(ctx, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code long} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<CTX, U> mapLong(LongFunction<U> f) {
        return (ctx, name) -> f.apply(extractLong(ctx, name));
    }

    @Override
    default LongExtractor<CTX> bind(String name) {
        return rs -> extractLong(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code long} values.
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
         * The extraction method, specialised to return an unboxed {@code long} value.
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
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code long} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<CTX, U, EX> mapLong(LongFunction<U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
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
