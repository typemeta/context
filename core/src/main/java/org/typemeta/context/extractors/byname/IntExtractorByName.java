package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.function.IntFunction;

/**
 * A function to extract a integer value from an context, given a name.
 * Essentially a specialisation of {@link ExtractorByName} for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntExtractorByName<CTX> extends ExtractorByName<CTX, Integer> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntExtractorByName<CTX> of(IntExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * The extraction method, specialised to return an unboxed {@code int} value.
     * @param ctx       the context
     * @return          the extracted value
     */
    int extractInt(CTX ctx, String name);

    @Override
    default Integer extract(CTX ctx, String name) {
        return extractInt(ctx, name);
    }

    /**
     * A variant of the {@link ExtractorByName#map} method specialised for {@code int} values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<CTX, U> mapInt(IntFunction<U> f) {
        return (ctx, name) -> f.apply(extractInt(ctx, name));
    }

    @Override
    default IntExtractor<CTX> bind(String name) {
        return rs -> extractInt(rs, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for {@code int} values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, Integer, EX> {
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
         * The extraction method, specialised to return an unboxed {@code int} value.
         * @param ctx       the context
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        int extractInt(CTX ctx, String name) throws EX;

        @Override
        default Integer extract(CTX ctx, String name) throws EX {
            return extractInt(ctx, name);
        }

        /**
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for {@code int} values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<CTX, U, EX> mapInt(IntFunction<U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return          the unchecked extractor
         */
        default IntExtractorByName<CTX> unchecked() {
            return (ctx, name) -> {
                try {
                    return extractInt(ctx, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
