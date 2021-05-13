package org.typemeta.context.injectors.byindex;

import java.util.*;

@FunctionalInterface
public interface LongInjectByIndex<CTX> extends InjectByIndex<CTX, Long> {
    CTX injectLong(CTX ctx, int n, double value);

    default CTX inject(CTX ctx, int n, Long value) {
        return injectLong(ctx, n, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectByIndex<CTX, OptionalLong> optionalLong() {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? inject(ctx, n, optVal.getAsLong()) : ctx;
    }

    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Long, EX> {
        CTX injectLong(CTX ctx, int n, long value) throws EX;

        default CTX inject(CTX ctx, int n, Long value) throws EX {
            return injectLong(ctx, n, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectByIndex.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, n, optVal) ->
                    optVal.isPresent() ? inject(ctx, n, optVal.getAsLong()) : ctx;
        }
    }
}
