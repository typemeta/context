package org.typemeta.context.extractors.byname;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.LongFunction;

/**
 * A function to extract an {@link OptionalLong} value from an context, given an index.
 * Essentially a specialisation of {@link ExtractorByName} for integer {@code OptionalLong} values.
 */
@FunctionalInterface
public interface OptLongExtractorByName<CTX> extends ExtractorByName<CTX, OptionalLong> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> OptLongExtractorByName<CTX> of(OptLongExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByName<CTX, Optional<U>> mapLong(LongFunction<U> f) {
        return (ctx, name) -> {
            final OptionalLong od = extract(ctx, name);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsLong()));
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
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, OptionalLong, EX> {
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
        default <U> ExtractorByName.Checked<CTX, Optional<U>, EX> mapLong(LongFunction<U> f) {
            return (rs, name) -> {
                final OptionalLong od = extract(rs, name);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsLong()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptLongExtractorByName<CTX> unchecked() {
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
