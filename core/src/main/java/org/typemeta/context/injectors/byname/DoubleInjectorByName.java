package org.typemeta.context.injectors.byname;

import org.typemeta.context.utils.Exceptions;

import java.util.OptionalDouble;

/**
 * A function to inject a double value into an context, given an name.
 * Essentially a specialisation of {@link InjectorByName} for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleInjectorByName<CTX> extends InjectorByName<CTX, Double> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> DoubleInjectorByName<CTX> of(DoubleInjectorByName<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectorByName#inject} method specialised for {@code double} values.
     * @param ctx       the context
     * @param name     the name
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectDouble(CTX ctx, String name, double value);

    default CTX inject(CTX ctx, String name, Double value) {
        return injectDouble(ctx, name, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByName<CTX, OptionalDouble> optionalDouble() {
        return (ctx, name, optVal) ->
                optVal.isPresent() ? inject(ctx, name, optVal.getAsDouble()) : ctx;
    }

    /**
     * A variation of {@link DoubleInjectorByName} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByName.Checked<CTX, Double, EX> {
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
         * A variant of the {@link InjectorByName.Checked#inject} method specialised for {@code double} values.
         * @param ctx       the context
         * @param name     the name
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectDouble(CTX ctx, String name, double value) throws EX;

        @Override
        default CTX inject(CTX ctx, String name, Double value) throws EX {
            return injectDouble(ctx, name, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByName.Checked<CTX, OptionalDouble, EX> optionalDouble() {
            return (ctx, name, optVal) ->
                    optVal.isPresent() ? inject(ctx, name, optVal.getAsDouble()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default DoubleInjectorByName<CTX> unchecked() {
            return (ctx, name, value)  -> {
                try {
                    return inject(ctx, name, value);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
