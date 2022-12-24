package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.Optional;
import java.util.function.*;

/**
 * A specialisation of {@code Extractor} for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntExtractor<CTX> extends Extractor<CTX, Integer> {
    /**
     * Static constructor method.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntExtractor<CTX> of(IntExtractor<CTX> extr) {
        return extr;
    }

    /**
     * An extractor that simply returns the context.
     * @return          the extractor
     */
    static IntExtractor<Integer> id() {
        return ctx -> ctx;
    }

    /**
     * An extractor that always returns the given value.
     * @param value     the value
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntExtractor<CTX> konst(int value) {
        return ctx -> value;
    }

    /**
     * Extract a integer value from the given context.
     * A variant of the {@link Extractor#extract} method specialised for double values.
     * @param ctx       the context
     * @return          the extracted value
     */
    int extractInt(CTX ctx);

    @Override
    default Integer extract(CTX ctx) {
        return extractInt(ctx);
    }

    @Override
    default Extractor<CTX, Optional<Integer>> optional() {
        // This extractor doesn't support nulls, so this won't work.
        throw new RuntimeException("Cannot construct an optional extractor from a IntExtractor");
    }

    @Override
    default <U> Extractor<CTX, U> map(Functions.F<Integer, U> f) {
        return mapInt(f::apply);
    }

    /**
     * A variant of the {@link Extractor#map} method specialised for integer values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the mapped extractor
     */
    default <U> Extractor<CTX, U> mapInt(IntFunction<U> f) {
        return ctx -> f.apply(extractInt(ctx));
    }

    /**
     * A specialisation of {@code ExtractorEx} for integer values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, Integer, EX> {
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
         * An extractor that simply returns the context.
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <EX extends Exception> Checked<Integer, EX> id() {
            return ctx -> ctx;
        }

        /**
         * An extractor that always returns the given value.
         * @param value     the value
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> konst(int value) {
            return ctx -> value;
        }

        /**
         * Extract a integer value from the given context.
         * A variant of the {@link Extractor.Checked#extract} method specialised for integer values.
         * @param ctx       the context
         * @return          the extracted value
         */
        int extractInt(CTX ctx) throws EX;

        @Override
        default Integer extract(CTX ctx) throws EX {
            return extractInt(ctx);
        }

        @Override
        default <U> Extractor.Checked<CTX, U, EX> map(Function<Integer, U> f) {
            return mapInt(f::apply);
        }

        /**
         * A variant of the {@link Extractor.Checked#map} method specialised for integer values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> Extractor.Checked<CTX, U, EX> mapInt(IntFunction<U> f) {
            return ctx -> f.apply(extractInt(ctx));
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default IntExtractor<CTX> unchecked() {
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
