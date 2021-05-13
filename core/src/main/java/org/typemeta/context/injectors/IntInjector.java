package org.typemeta.context.injectors;

import java.util.*;

@FunctionalInterface
public interface IntInjector<CTX> extends Injector<CTX, Integer> {
    static <CTX> IntInjector<CTX> of(IntInjector<CTX> injr) {
        return injr;
    }

    CTX inject(CTX ctx, int value);

    @Override
    default CTX inject(CTX ctx, Integer value) {
        return inject(ctx, value.intValue());
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalInt> optionalInt() {
        return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsInt()) : ctx;
    }

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Integer, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> injr) {
            return injr;
        }

        CTX inject(CTX ctx, int value) throws EX;

        @Override
        default CTX inject(CTX ctx, Integer value) throws EX {
            return inject(ctx, value.intValue());
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsInt()) : ctx;
        }
    }
}
