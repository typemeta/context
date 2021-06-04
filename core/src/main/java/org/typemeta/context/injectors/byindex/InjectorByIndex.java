package org.typemeta.context.injectors.byindex;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.Injector;
import org.typemeta.context.injectors.byname.InjectorByName;
import org.typemeta.context.utils.Exceptions;

import java.util.Optional;

/**
 * A {@link InjectorByIndex} is an injector that binds to a number.
 * @param <CTX>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface InjectorByIndex<CTX, T> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @param <T>       the value type
     * @return          the injector
     */
    static <CTX, T> InjectorByIndex<CTX, T> of(InjectorByIndex<CTX, T> injr) {
        return injr;
    }

    /**
     * A variant of {@code InjectorByIndex} that modifies the given context as a side-effect.
     * @param <CTX>
     * @param <T>
     */
    interface SideEffect<CTX, T> {
        void inject(CTX ctx, int index, T value);
    }

    /**
     * Construct an injector from a {@link Injector.SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @param <T>       the injected value type
     * @return          the extractor
     */
    static <CTX, T> InjectorByName<CTX, T> of(InjectorByName.SideEffect<CTX, T> f) {
        return (ctx, name, value) -> {
            f.inject(ctx, name, value);
            return ctx;
        };
    }

    /**
     * Inject a value into a context.
     * @param ctx       the context
     * @param index     the index
     * @param value     the value to be injected
     * @return          the new context
     */
    CTX inject(CTX ctx, int index, T value);

    /**
     * Bind this injector to an index.
     * @param index     the index
     * @return          the new injector
     */
    default Injector<CTX, T> bind(int index) {
        return (ctx, value) -> inject(ctx, index, value);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByIndex<CTX, U> premap(Functions.F<U, T> f) {
        return (ctx, index, value) -> inject(ctx, index, f.apply(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByIndex<CTX, Optional<T>> optional() {
        return (ctx, index, optVal) ->
                optVal.isPresent() ? inject(ctx, index, optVal.get()) : ctx;
    }

    /**
     * A variation of {@link InjectorByIndex} that may throw an exception.
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
        static <CTX, T, EX extends Exception> InjectorByName.Checked<CTX, T, EX> of(InjectorByName.Checked<CTX, T, EX> injr) {
            return injr;
        }

        /**
         * A variant of {@code Injector} that modifies the given context as a side-effect.
         * @param <CTX>     the context type
         * @param <T>       the value type
         * @param <EX>      the exception type
         */
        interface SideEffect<CTX, T, EX extends Exception> {
            void inject(CTX ctx, int index, T value) throws EX;
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
            return (ctx, index, value) -> {
                f.inject(ctx, index, value);
                return ctx;
            };
        }

        /**
         * Inject a value into a context.
         * @param ctx       the context
         * @param index     the index
         * @param value     the value
         * @return          the new context
         * @throws EX       if the injection fails
         */
        CTX inject(CTX ctx, int index, T value) throws EX;

        /**
         * Bind this injector to an index.
         * @param n         the index
         * @return          the new injector
         */
        default Injector.Checked<CTX, T, EX> bind(int n) {
            return (ctx, value) -> inject(ctx, n, value);
        }

        /**
         * Convert this injector into one which first applies the given function to the value.
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
            return (ctx, index, optVal) -> optVal.isPresent() ? inject(ctx, index, optVal.get()) : ctx;
        }

        /**
         * Return an unchecked equivalent of this injector.
         * @return an unchecked equivalent of this injector
         */
        default InjectorByIndex<CTX, T> unchecked() {
            return (ctx, index, value) -> {
                try {
                    return inject(ctx, index, value);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
