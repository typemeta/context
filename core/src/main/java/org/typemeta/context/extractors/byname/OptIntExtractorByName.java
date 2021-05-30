package org.typemeta.context.extractors.byname;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.IntFunction;

/**
 * A function to extract an {@link OptionalInt} value from an context, given an index.
 * Essentially a specialisation of {@link ExtractorByName} for integer {@code OptionalInt} values.
 */
@FunctionalInterface
public interface OptIntExtractorByName<CTX> extends ExtractorByName<CTX, OptionalInt> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> OptIntExtractorByName<CTX> of(OptIntExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByName<CTX, Optional<U>> mapInt(IntFunction<U> f) {
        return (ctx, name) -> {
            final OptionalInt od = extract(ctx, name);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsInt()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * Variant of {@link OptDoubleExtractorByName} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, OptionalInt, EX> {
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
        default <U> ExtractorByName.Checked<CTX, Optional<U>, EX> mapInt(IntFunction<U> f) {
            return (ctx, name) -> {
                final OptionalInt od = extract(ctx, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsInt()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptIntExtractorByName<CTX> unchecked() {
            return (ctx, name) -> {
                try {
                    return extract(ctx, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
