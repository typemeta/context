package org.typemeta.context.injectors.byname;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.OptionalDouble;
import java.util.function.ToDoubleFunction;

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
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, String name, double value);
    }

    /**
     * Construct an injector from a {@link Injector.SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleInjectorByName<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, name, value) -> {
            f.inject(ctx, name, value);
            return ctx;
        };
    }

    /**
     * Inject a value into a context.
     * A variant of the {@link InjectorByName#inject} method specialised for double values.
     * @param ctx       the context
     * @param name     the name
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectDouble(CTX ctx, String name, double value);

    default CTX inject(CTX ctx, String name, Double value) {
        return injectDouble(ctx, name, value);
    }

    @Override
    default DoubleInjector<CTX> bind(String name) {
        return (ctx, value) -> injectDouble(ctx, name, value);
    }

    @Override
    default <U> InjectorByName<CTX, U> premap(Functions.F<U, Double> f) {
        return premapDbl(f::apply);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByName<CTX, U> premapDbl(ToDoubleFunction<U> f) {
        return (ctx, name, value) -> inject(ctx, name, f.applyAsDouble(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByName<CTX, OptionalDouble> optionalDbl() {
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
         * A variant of {@code Injector} that modifies the given context as a side effect.
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         */
        @FunctionalInterface
        interface SideEffect<CTX, EX extends Exception> {
            void inject(CTX ctx, String name, double value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> ofSideEffect(SideEffect<CTX, EX> f) {
            return (ctx, name, value) -> {
                f.inject(ctx, name, value);
                return ctx;
            };
        }

        /**
         * Inject a value into a context.
         * A variant of the {@link InjectorByName.Checked#inject} method specialised for double values.
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

        @Override
        default <U> InjectorByName.Checked<CTX, U, EX> premap(Functions.F<U, Double> f) {
            return premapDbl(f::apply);
        }

        /**
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> InjectorByName.Checked<CTX, U, EX> premapDbl(ToDoubleFunction<U> f) {
            return (ctx, name, value) -> inject(ctx, name, f.applyAsDouble(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByName.Checked<CTX, OptionalDouble, EX> optionalDbl() {
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
