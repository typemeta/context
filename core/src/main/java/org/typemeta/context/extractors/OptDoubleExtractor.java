package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.DoubleFunction;

/**
 * A {@code ExtractorByName} for optional double values.
 */
@FunctionalInterface
public interface OptDoubleExtractor<CTX> extends Extractor<CTX, OptionalDouble> {
    static <CTX> OptDoubleExtractor<CTX> of(OptDoubleExtractor<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> Extractor<CTX, Optional<U>> mapDouble(DoubleFunction<U> f) {
        return ctx -> {
            final OptionalDouble od = extract(ctx);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsDouble()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * A {@link Extractor.Checked} for optional double values.
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, OptionalDouble, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

        default <U> Extractor.Checked<CTX, Optional<U>, EX> mapDouble(DoubleFunction<U> f) {
            return ctx -> {
                final OptionalDouble od = extract(ctx);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsDouble()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptDoubleExtractor<CTX> unchecked() {
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
