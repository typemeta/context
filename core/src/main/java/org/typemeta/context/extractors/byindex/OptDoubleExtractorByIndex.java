package org.typemeta.context.extractors.byindex;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.DoubleFunction;

/**
 * A function to extract an {@link OptionalDouble} value from an context, given an index.
 * Essentially a specialisation of {@link ExtractorByIndex} for integer {@code OptionalDouble} values.
 */
@FunctionalInterface
public interface OptDoubleExtractorByIndex<CTX> extends ExtractorByIndex<CTX, OptionalDouble> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> OptDoubleExtractorByIndex<CTX> of(OptDoubleExtractorByIndex<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, Optional<U>> mapDouble(DoubleFunction<U> f) {
        return (ctx, index) -> {
            final OptionalDouble od = extract(ctx, 8);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsDouble()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * Variant of {@link OptDoubleExtractorByIndex} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByIndex.Checked<CTX, OptionalDouble, EX> {
        /**
         * Static constructor.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

        /**
         * Create an extractor that applies this extractor first and then maps a function over the
         * extracted optional value.
         * @param f         the function to be mapped over the optional
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> ExtractorByIndex.Checked<CTX, Optional<U>, EX> mapDouble(DoubleFunction<U> f) {
            return (ctx, name) -> {
                final OptionalDouble od = extract(ctx, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsDouble()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptDoubleExtractorByIndex<CTX> unchecked() {
            return (ctx, index) -> {
                try {
                    return extract(ctx, index);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
