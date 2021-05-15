package org.typemeta.context.injectors.byindex;

import java.util.*;

/**
 * A function to inject a long value into an context, given an index.
 * Essentially a specialisation of {@link InjectByIndex} for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjectByIndex<CTX> extends InjectByIndex<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> LongInjectByIndex<CTX> of(LongInjectByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectByIndex#inject} method specialised for {@code long} values.
     * @param ctx       the context
     * @param n         the index
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectLong(CTX ctx, int n, long value);

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

    /**
     * A variation of {@link LongInjectByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Long, EX> {
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
         * A variant of the {@link InjectByIndex.Checked#inject} method specialised for {@code long} values.
         * @param ctx       the context
         * @param n         the index
         * @param value     the value to be injected
         * @return          the context
         */
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
