package org.typemeta.context.injectors.byname;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.Injector;

import java.util.Optional;

/**
 * A {@link InjectorByName} is an injector that binds to a name.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface InjectorByName<CTX, T> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @param <T>       the value type
     * @return          the injector
     */
    static <CTX, T> InjectorByName<CTX, T> of(InjectorByName<CTX, T> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     */
    interface SideEffect<CTX, T> {
        void inject(CTX ctx, String name, T value);
    }

    /**
     * Construct an injector from a {@link Injector.SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     * @return          the extractor
     */
    static <CTX, T> InjectorByName<CTX, T> ofSideEffect(SideEffect<CTX, T> f) {
        return (ctx, name, value) -> {
            f.inject(ctx, name, value);
            return ctx;
        };
    }

    /**
     * Inject a value into a context.
     * @param ctx       the context
     * @param name      the name
     * @param value     the value to be injected
     * @return          the new context
     */
    CTX inject(CTX ctx, String name, T value);

    /**
     * Bind this injector to an name.
     * @param name      the name
     * @return          the new injector
     */
    default Injector<CTX, T> bind(String name) {
        return (ctx, value) -> inject(ctx, name, value);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByName<CTX, U> premap(Functions.F<U, T> f) {
        return (ctx, name, value) -> inject(ctx, name, f.apply(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByName<CTX, Optional<T>> optional() {
        return (ctx, name, optVal) ->
                optVal.isPresent() ? inject(ctx, name, optVal.get()) : ctx;
    }

    /**
     * A variation of {@link InjectorByName} that may throw an exception.
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
         * @param <T>       the value type
         * @param <EX>      the exception type
         * @return          the injector
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> of(Checked<CTX, T, EX> injr) {
            return injr;
        }

        /**
         * A variant of {@code Injector} that modifies the given context as a side effect.
         * @param <CTX>     the context type
         * @param <T>       the value type
         * @param <EX>      the exception type
         */
        interface SideEffect<CTX, T, EX extends Exception> {
            void inject(CTX ctx, String name, T value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <T>       the injected value type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, T, EX extends Exception> Checked<CTX, T, EX> ofSideEffect(SideEffect<CTX, T, EX> f) {
            return (ctx, name, value) -> {
                f.inject(ctx, name, value);
                return ctx;
            };
        }

        /**
         * Inject a value into a context.
         * @param ctx       the context
         * @param name      the name
         * @param value     the value
         * @return          the new context
         * @throws EX       if the injection fails
         */
        CTX inject(CTX ctx, String name, T value) throws EX;

        /**
         * Bind this injector to an name.
         * @param name      the name
         * @return          the new injector
         */
        default Injector.Checked<CTX, T, EX> bind(String name) {
            return (ctx, value) -> inject(ctx, name, value);
        }

        /**
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> Checked<CTX, U, EX> premap(Functions.F<U, T> f) {
            return (ctx, n, value) -> inject(ctx, n, f.apply(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Checked<CTX, Optional<T>, EX> optional() {
            return (ctx, name, optVal) -> optVal.isPresent() ? inject(ctx, name, optVal.get()) : ctx;
        }

        /**
         * Return an unchecked equivalent of this injector.
         * @return an unchecked equivalent of this injector
         */
        default InjectorByName<CTX, T> unchecked() {
            return (ctx, name, value) -> {
                try {
                    return inject(ctx, name, value);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
