package org.typemeta.context.injectors.byindex;

import java.util.*;

@FunctionalInterface
public interface IntInjectByIndex<CTX> extends InjectByIndex<CTX, Integer> {
    CTX injectInt(CTX ctx, int n, double value);

    default CTX inject(CTX ctx, int n, Integer value) {
        return injectInt(ctx, n, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectByIndex<CTX, OptionalInt> optionalInt() {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? inject(ctx, n, optVal.getAsInt()) : ctx;
    }

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Integer, EX> {
        CTX injectInt(CTX ctx, int n, int value) throws EX;

        default CTX inject(CTX ctx, int n, Integer value) throws EX {
            return injectInt(ctx, n, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectByIndex.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, n, optVal) ->
                    optVal.isPresent() ? inject(ctx, n, optVal.getAsInt()) : ctx;
        }
    }
}
