package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.function.LongFunction;

/**
 * A specialisation of {@code Extractor} for {@code long} values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongExtractor<CTX> extends Extractor<CTX, Long> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return the extractor
     */
    static <CTX> LongExtractor<CTX> of(LongExtractor<CTX> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the context.
     * @return          the extractor
     */
    static LongExtractor<Long> id() {
        return ctx -> ctx;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongExtractor<CTX> konst(long value) {
        return ctx -> value;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code long} value.
     * @param ctx       the context
     * @return the extracted value
     */
    long extractLong(CTX ctx);

    @Override
    default Long extract(CTX ctx) {
        return extractLong(ctx);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for {@code long} values.
     * @param f         the function
     * @param <U>       the function return type
     * @return the mapped extractor
     */
    default <U> Extractor<CTX, U> mapLong(LongFunction<U> f) {
        return ctx -> f.apply(extractLong(ctx));
    }

    /**
     * A specialisation of {@code ExtractorEx} for {@code long} values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, Long, EX> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return extr;
        }

        /**
         * An extractor that simply returns the context.
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <EX extends Exception> Checked<Long, EX> id() {
            return ctx -> ctx;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> konst(long value) {
            return ctx -> value;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code long} value.
         * @param ctx       the context
         * @return          the extracted value
         */
        long extractLong(CTX ctx);

        @Override
        default Long extract(CTX ctx) {
            return extractLong(ctx);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for {@code long} values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<CTX, U, EX> mapLong(LongFunction<U> f) {
            return ctx -> f.apply(extractLong(ctx));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongExtractor<CTX> unchecked() {
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
