package org.typemeta.context.injectors;

import java.util.OptionalDouble;
import java.util.function.*;

/**
 * A {@link Injector} specialised for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleInjector<CTX> extends Injector<CTX, Double> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleInjector<CTX> of(DoubleInjector<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side-effect.
     * @param <CTX>
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, double value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleInjector<CTX> of(SideEffect<CTX> f) {
        return (ctx, value) -> {
            f.inject(ctx, value);
            return ctx;
        };
    }

    /**
     * Inject a double value into a context.
     * A variant of the {@link Injector#inject} method specialised for double values.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX injectDouble(CTX ctx, double value);

    @Override
    default CTX inject(CTX ctx, Double value) {
        return injectDouble(ctx, value);
    }

    /**
     * Convert this injector into one that applies the given function to the value before injecting it.
     * @param f         the function to be applied to the injected value
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> Injector<CTX, U> premap(ToDoubleFunction<U> f) {
        return (ctx, value) -> inject(ctx, f.applyAsDouble(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalDouble> optionalDbl() {
        return (ctx, optVal) -> optVal.isPresent() ? inject(ctx, optVal.getAsDouble()) : ctx;
    }

    /**
     * A {@link Injector.Checked} specialised for double values.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Double, EX> {
        /**
         * Static constructor.
         * @param injr      the injector
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
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
            void inject(CTX ctx, double value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(SideEffect<CTX, EX> f) {
            return (ctx, value) -> {
                f.inject(ctx, value);
                return ctx;
            };
        }

        /**
         * Inject a double value into a context.
         * A variant of the {@link Injector.Checked#inject} method specialised for double values.
         * @param ctx       the context
         * @param value     the value
         * @return          the new context
         */
        CTX injectDouble(CTX ctx, double value) throws EX;

        @Override
        default CTX inject(CTX ctx, Double value) throws EX {
            return injectDouble(ctx, value);
        }

        /**
         * Convert this injector into one that applies the given function to the value before injecting it.
         * @param f         the function to be applied to the injected value
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> Injector.Checked<CTX, U, EX> premap(ToDoubleFunction<U> f) {
            return (ctx, value) -> inject(ctx, f.applyAsDouble(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalDouble, EX> optionalDbl() {
            return (ctx, optVal) -> optVal.isPresent() ? injectDouble(ctx, optVal.getAsDouble()) : ctx;
        }
    }
}
