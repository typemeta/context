package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.*;

/**
 * A {@code ExtractorByName} for optional {@code int} values.
 */
@FunctionalInterface
public interface OptIntExtractor<CTX> extends Extractor<CTX, OptionalInt> {
    static <CTX> OptIntExtractor<CTX> of(OptIntExtractor<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> Extractor<CTX, Optional<U>> mapInt(IntFunction<U> f) {
        return ctx -> {
            final OptionalInt od = extract(ctx);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsInt()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * A {@link Extractor.Checked} for optional {@code int} values.
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, OptionalInt, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

        default <U> Extractor.Checked<CTX, Optional<U>, EX> mapInt(IntFunction<U> f) {
            return ctx -> {
                final OptionalInt od = extract(ctx);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsInt()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptIntExtractor<CTX> unchecked() {
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
