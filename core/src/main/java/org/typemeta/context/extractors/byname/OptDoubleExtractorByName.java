package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.DoubleFunction;

/**
 * A {@code ExtractorByName} for optional {@code double} values.
 */
public interface OptDoubleExtractorByName<CTX> extends ExtractorByName<CTX, OptionalDouble> {
    static <CTX> OptDoubleExtractorByName<CTX> of(OptDoubleExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByName<CTX, Optional<U>> mapDbl(DoubleFunction<U> f) {
        return (ctx, name) -> {
            final OptionalDouble od = extract(ctx, name);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsDouble()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * A {@code NamedExtractorEx} for optional {@code double} values.
     */
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, OptionalDouble, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

        default <U> ExtractorByName.Checked<CTX, Optional<U>, EX> mapDbl(DoubleFunction<U> f) {
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
        default OptDoubleExtractorByName<CTX> unchecked() {
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
