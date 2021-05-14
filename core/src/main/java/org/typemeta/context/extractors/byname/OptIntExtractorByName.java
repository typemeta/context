package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.IntFunction;

/**
 * A {@code ExtractorByName} for optional {@code int} values.
 */
@FunctionalInterface
public interface OptIntExtractorByName<CTX> extends ExtractorByName<CTX, OptionalInt> {
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

    @Override
    default Extractor<CTX, OptionalInt> bind(String name) {
        return ctx -> extract(ctx, name);
    }

    /**
     * A {@code NamedExtractorEx} for optional {@code int} values.
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, OptionalInt, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

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
        default Extractor.Checked<CTX, OptionalInt, EX> bind(String name) {
            return ctx -> extract(ctx, name);
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
