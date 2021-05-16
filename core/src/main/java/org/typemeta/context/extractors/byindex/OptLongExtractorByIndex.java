package org.typemeta.context.extractors.byindex;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.LongFunction;

/**
 * A function to extract an {@link OptionalLong} value from an context, given an index.
 * Essentially a specialisation of {@link ExtractorByIndex} for integer {@code OptionalLong} values.
 */
@FunctionalInterface
public interface OptLongExtractorByIndex<CTX> extends ExtractorByIndex<CTX, OptionalLong> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> OptLongExtractorByIndex<CTX> of(OptLongExtractorByIndex<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByIndex<CTX, Optional<U>> mapLong(LongFunction<U> f) {
        return (ctx, index) -> {
            final OptionalLong od = extract(ctx, index);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsLong()));
            } else {
                return Optional.empty();
            }
        };
    }

    @Override
    default Extractor<CTX, OptionalLong> bind(int index) {
        return ctx -> extract(ctx, index);
    }

    /**
     * Variant of {@link OptLongExtractorByIndex} where the extract method may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByIndex.Checked<CTX, OptionalLong, EX> {
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
        default <U> ExtractorByIndex.Checked<CTX, Optional<U>, EX> mapLong(LongFunction<U> f) {
            return (rs, index) -> {
                final OptionalLong od = extract(rs, index);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsLong()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptLongExtractorByIndex<CTX> unchecked() {
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
