package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.LongFunction;

/**
 * A {@code ExtractorByName} for optional {@code long} values.
 */
@FunctionalInterface
public interface OptLongExtractorByName<CTX> extends ExtractorByName<CTX, OptionalLong> {
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

    @Override
    default Extractor<CTX, OptionalLong> bind(String name) {
        return ctx -> extract(ctx, name);
    }

    /**
     * A {@code NamedExtractorEx} for optional {@code long} values.
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, OptionalLong, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

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
        default Extractor.Checked<CTX, OptionalLong, EX> bind(String name) {
            return rs -> extract(rs, name);
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
