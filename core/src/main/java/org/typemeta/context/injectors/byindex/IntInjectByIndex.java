package org.typemeta.context.injectors.byindex;

import java.util.*;

/**
 * A function to inject a integer value into an context, given an index.
 * Essentially a specialisation of {@link InjectByIndex} for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntInjectByIndex<CTX> extends InjectByIndex<CTX, Integer> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> IntInjectByIndex<CTX> of(IntInjectByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectByIndex#inject} method specialised for {@code int} values.
     * @param ctx       the context
     * @param n         the index
     * @param value     the value to be injected
     * @return          the context
     */
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

    /**
     * A variation of {@link IntInjectByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Integer, EX> {
        /**
         * Static constructor.
         * @param injr      the injector
         * @param <CTX>     the context type
         * @return          the injector
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> injr) {
            return injr;
        }

        /**
         * A variant of the {@link InjectByIndex.Checked#inject} method specialised for {@code int} values.
         * @param ctx       the context
         * @param n         the index
         * @param value     the value to be injected
         * @return          the context
         */
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
