package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.DoubleFunction;

/**
 * A specialisation of {@link ExtractorByName} for {@code double} values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleExtractorByName<CTX> extends ExtractorByName<CTX, Double> {

    /**
     * The extraction method, specialised to return an unboxed {@code double} value.
     * @param ctx       the context
     * @return          the extracted value
     */
    double extractDouble(CTX ctx, String name);

    @Override
    default Double extract(CTX ctx, String name) {
        return extractDouble(ctx, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code double} values.
     * @param f         the function
     * @param <U>       the return type of the function
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<CTX, U> mapDouble(DoubleFunction<U> f) {
        return (ctx, name) -> f.apply(extractDouble(ctx, name));
    }

    @Override
    default DoubleExtractor<CTX> bind(String name) {
        return rs -> extractDouble(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code double} values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, Double, EX> {
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
         * The extraction method, specialised to return an unboxed {@code double} value.
         * @param ctx       the context
         * @return          the extracted double value
         * @throws EX       if the extraction fails
         */
        double extractDouble(CTX ctx, String name) throws EX;

        @Override
        default Double extract(CTX ctx, String name) throws EX {
            return extractDouble(ctx, name);
        }

        /**
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code double} values.
         * @param f         the function
         * @param <U>       the return type of the function
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<CTX, U, EX> mapDouble(DoubleFunction<U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return the unchecked extractor
         */
        default DoubleExtractorByName<CTX> unchecked() {
            return (ctx, name) -> {
                try {
                    return extractDouble(ctx, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
