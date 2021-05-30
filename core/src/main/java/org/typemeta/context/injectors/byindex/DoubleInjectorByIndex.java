package org.typemeta.context.injectors.byindex;

import org.typemeta.context.utils.Exceptions;

import java.util.OptionalDouble;

/**
 * A function to inject a double value into an context, given an index.
 * Essentially a specialisation of {@link InjectorByIndex} for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleInjectorByIndex<CTX> extends InjectorByIndex<CTX, Double> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> DoubleInjectorByIndex<CTX> of(DoubleInjectorByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectorByIndex#inject} method specialised for {@code double} values.
     * @param ctx       the context
     * @param index     the index
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectDouble(CTX ctx, int index, double value);

    default CTX inject(CTX ctx, int index, Double value) {
        return injectDouble(ctx, index, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByIndex<CTX, OptionalDouble> optionalDouble() {
        return (ctx, index, optVal) ->
                optVal.isPresent() ? inject(ctx, index, optVal.getAsDouble()) : ctx;
    }

    /**
     * A variation of {@link DoubleInjectorByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByIndex.Checked<CTX, Double, EX> {
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
         * A variant of the {@link InjectorByIndex.Checked#inject} method specialised for {@code double} values.
         * @param ctx       the context
         * @param index     the index
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectDouble(CTX ctx, int index, double value) throws EX;

        @Override
        default CTX inject(CTX ctx, int index, Double value) throws EX {
            return injectDouble(ctx, index, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByIndex.Checked<CTX, OptionalDouble, EX> optionalDouble() {
            return (ctx, index, optVal) ->
                    optVal.isPresent() ? inject(ctx, index, optVal.getAsDouble()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default DoubleInjectorByIndex<CTX> unchecked() {
            return (ctx, index, value)  -> {
                try {
                    return inject(ctx, index, value);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
