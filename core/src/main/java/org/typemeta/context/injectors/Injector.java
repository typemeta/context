package org.typemeta.context.injectors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.Optional;

/**
 * An {@link Injector} injects values into a context.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface Injector<CTX, T> {
    /**
     * Inject a value into a context.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX inject(CTX ctx, T value);

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, Optional<T>> optional() {
        return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.get()) : ctx;
    }

    /**
     * Convert this injector into one that applies the given function to the value before injecting it.
     * @param f         the function to be applied to the injected value
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> Injector<CTX, U> premap(Functions.F<U, T> f) {
        return (ctx, value) -> inject(ctx, f.apply(value));
    }

    /**
     * A variation of {@link Injector} that may throw an exception.
     * @param <CTX>     the context type
     * @param <T>       the value type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, T, EX extends Exception> {
        /**
         * Inject a value into a context.
         * @param ctx       the context
         * @param value     the value
         * @return          the new context
         * @throws EX       if the injection fails
         */
        CTX inject(CTX ctx, T value) throws EX;

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Checked<CTX, Optional<T>, EX> optional() {
            return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.get()) : ctx;
        }

        /**
         * Return an unchecked equivalent of this injector.
         * @return an unchecked equivalent of this injector
         */
        default Injector<CTX, T> unchecked() {
            return (ctx, value) -> {
                try {
                    return inject(ctx, value);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
