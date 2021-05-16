package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.function.DoubleFunction;

/**
 * A specialisation of {@link Extractor} for {@code double} values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleExtractor<CTX> extends Extractor<CTX, Double> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return the extractor
     */
    static <CTX> DoubleExtractor<CTX> of(DoubleExtractor<CTX> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the context.
     * @return          the extractor
     */
    static DoubleExtractor<Double> id() {
        return ctx -> ctx;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleExtractor<CTX> konst(double value) {
        return ctx -> value;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code double} value.
     * @param ctx   the context
     * @return the extracted value
     */
    double extractDouble(CTX ctx);

    @Override
    default Double extract(CTX ctx) {
        return extractDouble(ctx);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for {@code double} values.
     * @param f         the function
     * @param <U>       the function return type
     * @return the mapped extractor
     */
    default <U> Extractor<CTX, U> mapDouble(DoubleFunction<U> f) {
        return ctx -> f.apply(extractDouble(ctx));
    }

    /**
     * A specialisation of {@link Extractor.Checked} for {@code double} values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, Double, EX> {
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
        static <EX extends Exception> Checked<Double, EX> id() {
            return ctx -> ctx;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> konst(double value) {
            return ctx -> value;
        }

        /**
         * The extraction method, specialised to return an unboxed {@code double} value.
         * @param ctx       the context
         * @return          the extracted value
         */
        double extractDouble(CTX ctx) throws EX;

        @Override
        default Double extract(CTX ctx) throws EX {
            return extractDouble(ctx);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for {@code double} values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<CTX, U, EX> mapDouble(DoubleFunction<U> f) {
            return ctx -> f.apply(extractDouble(ctx));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default DoubleExtractor<CTX> unchecked() {
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
