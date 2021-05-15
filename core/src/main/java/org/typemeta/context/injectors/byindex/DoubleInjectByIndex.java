package org.typemeta.context.injectors.byindex;

import java.util.*;

/**
 * A function to inject a double value into an context, given an index.
 * Essentially a specialisation of {@link InjectByIndex} for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleInjectByIndex<CTX> extends InjectByIndex<CTX, Double> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> DoubleInjectByIndex<CTX> of(DoubleInjectByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectByIndex#inject} method specialised for {@code double} values.
     * @param ctx       the context
     * @param n         the index
     * @param value     the value to be injected
     * @return          the context
     */
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

    /**
     * A variation of {@link DoubleInjectByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Double, EX> {
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
         * A variant of the {@link InjectByIndex.Checked#inject} method specialised for {@code double} values.
         * @param ctx       the context
         * @param n         the index
         * @param value     the value to be injected
         * @return          the context
         */
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
