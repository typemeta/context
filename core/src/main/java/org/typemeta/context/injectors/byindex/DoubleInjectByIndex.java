package org.typemeta.context.injectors.byindex;

import java.util.*;

@FunctionalInterface
public interface DoubleInjectByIndex<CTX> extends InjectByIndex<CTX, Double> {
    CTX injectDouble(CTX ctx, int n, double value);

    default CTX inject(CTX ctx, int n, Double value) {
        return injectDouble(ctx, n, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectByIndex<CTX, OptionalDouble> optionalDbl() {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? inject(ctx, n, optVal.getAsDouble()) : ctx;
    }

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Double, EX> {
        CTX injectDouble(CTX ctx, int n, double value) throws EX;

        default CTX inject(CTX ctx, int n, Double value) throws EX {
            return injectDouble(ctx, n, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectByIndex.Checked<CTX, OptionalDouble, EX> optionalDbl() {
            return (ctx, n, optVal) ->
                    optVal.isPresent() ? inject(ctx, n, optVal.getAsDouble()) : ctx;
        }
    }
}
