package org.typemeta.context.injectors;

import java.util.OptionalLong;

/**
 * A {@link Injector} specialised for {@code long} values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjector<CTX> extends Injector<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongInjector<CTX> of(LongInjector<CTX> injr) {
        return injr;
    }

    /**
     * Inject a value into a context.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX inject(CTX ctx, long value);

    @Override
    default CTX inject(CTX ctx, Long value) {
        return inject(ctx, value.longValue());
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalLong> optionalLong() {
        return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsLong()) : ctx;
    }

    /**
     * A {@link Injector.Checked} specialised for {@code long} values.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Long, EX> {
        /**
         * Static constructor.
         * @param injr      the injector
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> injr) {
            return injr;
        }

        /**
         * Inject a value into a context.
         * @param ctx       the context
         * @param value     the value
         * @return          the new context
         */
        CTX inject(CTX ctx, long value) throws EX;

        @Override
        default CTX inject(CTX ctx, Long value) throws EX {
            return inject(ctx, value.longValue());
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsLong()) : ctx;
        }
    }
}
