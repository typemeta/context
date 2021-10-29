package org.typemeta.context.injectors.byindex;

import org.typemeta.context.injectors.*;
import org.typemeta.context.injectors.byname.DoubleInjectorByName;
import org.typemeta.context.utils.Exceptions;

import java.util.OptionalDouble;
import java.util.function.*;

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
     * A variant of {@code Injector} that modifies the given context as a side-effect.
     * @param <CTX>
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, int index, double value);
    }

    /**
     * Construct an injector from a {@link Injector.SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleInjectorByIndex<CTX> of(SideEffect<CTX> f) {
        return (ctx, index, value) -> {
            f.inject(ctx, index, value);
            return ctx;
        };
    }

    /**
     * Inject a double value into a context.
     * A variant of the {@link InjectorByIndex#inject} method specialised for double values.
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
     * Return an injector which first applies the given function to the value.
     * A variant of the {@link InjectorByIndex#premap} method specialised for double values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByIndex<CTX, U> premapDouble(ToDoubleFunction<U> f) {
        return (ctx, index, value) -> injectDouble(ctx, index, f.applyAsDouble(value));
    }

    @Override
    default DoubleInjector<CTX> bind(int index) {
        return (ctx, value) -> injectDouble(ctx, index, value);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByIndex<CTX, U> premap(ToDoubleFunction<U> f) {
        return (ctx, index, value) -> inject(ctx, index, f.applyAsDouble(value));
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
         * A variant of {@code Injector} that modifies the given context as a side-effect.
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         */
        @FunctionalInterface
        interface SideEffect<CTX, EX extends Exception> {
            void inject(CTX ctx, int index, double value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(SideEffect<CTX, EX> f) {
            return (ctx, index, value) -> {
                f.inject(ctx, index, value);
                return ctx;
            };
        }

        /**
         * Inject a double value into a context.
         * A variant of the {@link InjectorByIndex.Checked#inject} method specialised for double values.
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
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> InjectorByIndex.Checked<CTX, U, EX> premap(ToDoubleFunction<U> f) {
            return (ctx, index, value) -> inject(ctx, index, f.applyAsDouble(value));
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
