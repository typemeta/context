package org.typemeta.context.injectors;

import java.util.OptionalInt;
import java.util.function.ToIntFunction;

/**
 * A {@link Injector} specialised for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntInjector<CTX> extends Injector<CTX, Integer> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntInjector<CTX> of(IntInjector<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side-effect.
     * @param <CTX>
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, int value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntInjector<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, value) -> {
            f.inject(ctx, value);
            return ctx;
        };
    }

    /**
     * integer an integer value into a context.
     * A variant of the {@link Injector#inject} method specialised for integer values.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX injectInt(CTX ctx, int value);

    @Override
    default CTX inject(CTX ctx, Integer value) {
        return injectInt(ctx, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalInt> optionalInt() {
        return (ctx, optVal) -> optVal.isPresent() ? injectInt(ctx, optVal.getAsInt()) : ctx;
    }

    /**
     * Convert this injector into one that applies the given function to the value before injecting it.
     * @param f         the function to be applied to the injected value
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> Injector<CTX, U> premap(ToIntFunction<U> f) {
        return (ctx, value) -> inject(ctx, f.applyAsInt(value));
    }

    /**
     * An {@link Injector.Checked} injector specialised for integer values.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Integer, EX> {
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
            void inject(CTX ctx, int value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> ofSideEffect(SideEffect<CTX, EX> f) {
            return (ctx, value) -> {
                f.inject(ctx, value);
                return ctx;
            };
        }

        /**
         * integer an integer value into a context.
         * A variant of the {@link Injector.Checked#inject} method specialised for integer values.
         * @param ctx       the context
         * @param value     the value
         * @return          the new context
         */
        CTX injectInt(CTX ctx, int value) throws EX;

        @Override
        default CTX inject(CTX ctx, Integer value) throws EX {
            return injectInt(ctx, value);
        }

        /**
         * Convert this injector into one that applies the given function to the value before injecting it.
         * @param f         the function to be applied to the injected value
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> Injector.Checked<CTX, U, EX> premap(ToIntFunction<U> f) {
            return (ctx, value) -> inject(ctx, f.applyAsInt(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, optVal) -> optVal.isPresent() ? injectInt(ctx, optVal.getAsInt()) : ctx;
        }
    }
}
