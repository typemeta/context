package org.typemeta.context.extractors;

import org.typemeta.context.utils.Exceptions;

import java.util.*;
import java.util.function.LongFunction;

/**
 * A {@code ExtractorByName} for optional {@code long} values.
 */
public interface OptLongExtractor<CTX> extends Extractor<CTX, OptionalLong> {
    static <CTX> OptLongExtractor<CTX> of(OptLongExtractor<CTX> extr) {
        return extr;
    }

    /**
     * Create an extractor that applies this extractor first and then maps a function over the
     * extracted optional value.
     * @param f         the function to be mapped over the optional
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> Extractor<CTX, Optional<U>> mapLong(LongFunction<U> f) {
        return ctx -> {
            final OptionalLong od = extract(ctx);
            if (od.isPresent()) {
                return Optional.of(f.apply(od.getAsLong()));
            } else {
                return Optional.empty();
            }
        };
    }

    /**
     * A {@link Extractor.Checked} for optional {@code long} values.
     */
    interface Checked<CTX, EX extends Exception> extends Extractor.Checked<CTX, OptionalLong, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return  extr;
        }

        default <U> Extractor.Checked<CTX, Optional<U>, EX> mapLong(LongFunction<U> f) {
            return ctx -> {
                final OptionalLong od = extract(ctx);
                if (od.isPresent()) {
                    return Optional.of(f.apply(od.getAsLong()));
                } else {
                    return Optional.empty();
                }
            };
        }

        @Override
        default OptLongExtractor<CTX> unchecked() {
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
