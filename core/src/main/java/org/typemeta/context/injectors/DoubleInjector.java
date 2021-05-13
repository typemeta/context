package org.typemeta.context.injectors;

import java.util.*;

@FunctionalInterface
public interface DoubleInjector<CTX> extends Injector<CTX, Double> {
    static <CTX> DoubleInjector<CTX> of(DoubleInjector<CTX> injr) {
        return injr;
    }

    CTX inject(CTX ctx, double value);

    @Override
    default CTX inject(CTX ctx, Double value) {
        return inject(ctx, value.doubleValue());
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalDouble> optionalDbl() {
        return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsDouble()) : ctx;
    }

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Double, EX> {
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> injr) {
            return injr;
        }

        CTX inject(CTX ctx, double value) throws EX;

        @Override
        default CTX inject(CTX ctx, Double value) throws EX {
            return inject(ctx, value.doubleValue());
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalDouble, EX> optionalDbl() {
            return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsDouble()) : ctx;
        }
    }
}
