package org.typemeta.context.injectors;

import org.typemeta.context.functions.Functions;

import java.util.Optional;

/**
 * An {@link Injector} injects values into a context.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface Injector<CTX, T> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     * @return          the extractor
     */
    static <CTX, T> Injector<CTX, T> of(Injector<CTX, T> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     */
    interface SideEffect<CTX, T> {
        void inject(CTX ctx, T value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     * @return          the extractor
     */
    static <CTX, T> Injector<CTX, T> ofSideEffect(SideEffect<CTX, T> f) {
        return (ctx, value) -> {
            f.inject(ctx, value);
            return ctx;
        };
    }

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
         * Static constructor.
         * @param injr      the injector
         * @param <CTX>     the context type
         * @param <T>       the injected value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> of(Checked<CTX, T, EX> injr) {
            return injr;
        }

        /**
         * A variant of {@code Injector} that modifies the given context as a side effect.
         * @param <CTX>     the context type
         * @param <T>       the injected value type
         * @param <EX>      the exception type
         */
        interface SideEffect<CTX, T, EX extends Exception> {
            void inject(CTX ctx, T value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <T>       the injected value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> of(SideEffect<CTX, T, EX> f) {
            return (ctx, value) -> {
                f.inject(ctx, value);
                return ctx;
            };
        }

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
         * Convert this injector into one that applies the given function to the value before injecting it.
         * @param f         the function to be applied to the injected value
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> Injector.Checked<CTX, U, EX> premap(Functions.F<U, T> f) {
            return (ctx, value) -> inject(ctx, f.apply(value));
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
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
