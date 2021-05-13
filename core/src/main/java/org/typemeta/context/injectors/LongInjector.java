package org.typemeta.context.injectors;

import java.util.*;

@FunctionalInterface
public interface LongInjector<CTX> extends Injector<CTX, Long> {
    static <CTX> LongInjector<CTX> of(LongInjector<CTX> injr) {
        return injr;
    }

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

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Long, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> injr) {
            return injr;
        }

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
