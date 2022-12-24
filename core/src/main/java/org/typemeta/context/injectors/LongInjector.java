package org.typemeta.context.injectors;

import java.util.OptionalLong;
import java.util.function.*;

/**
 * A {@link Injector} specialised for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjector<CTX> extends Injector<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongInjector<CTX> of(LongInjector<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, long value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongInjector<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, value) -> {
            f.inject(ctx, value);
            return ctx;
        };
    }

    /**
     * integer a long value into a context.
     * A variant of the {@link Injector#inject} method specialised for long values.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX injectLong(CTX ctx, long value);

    @Override
    default CTX inject(CTX ctx, Long value) {
        return injectLong(ctx, value);
    }

    /**
     * Convert this injector into one that applies the given function to the value before injecting it.
     * @param f         the function to be applied to the injected value
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> Injector<CTX, U> premap(ToLongFunction<U> f) {
        return (ctx, value) -> inject(ctx, f.applyAsLong(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default Injector<CTX, OptionalLong> optionalLong() {
        return (ctx, optVal) -> optVal.isPresent() ? injectLong(ctx, optVal.getAsLong()) : ctx;
    }

    /**
     * A {@link Injector.Checked} specialised for long values.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends Injector.Checked<CTX, Long, EX> {
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
         * A variant of {@code Injector} that modifies the given context as a side effect.
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         */
        @FunctionalInterface
        interface SideEffect<CTX, EX extends Exception> {
            void inject(CTX ctx, long value) throws EX;
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
         * integer a long value into a context.
         * A variant of the {@link Injector.Checked#inject} method specialised for long values.
         * @param ctx       the context
         * @param value     the value
         * @return          the new context
         */
        CTX injectLong(CTX ctx, long value) throws EX;

        @Override
        default CTX inject(CTX ctx, Long value) throws EX {
            return injectLong(ctx, value);
        }

        /**
         * Convert this injector into one that applies the given function to the value before injecting it.
         * @param f         the function to be applied to the injected value
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> Injector.Checked<CTX, U, EX> premap(ToLongFunction<U> f) {
            return (ctx, value) -> inject(ctx, f.applyAsLong(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, optVal) -> optVal.isPresent() ? injectLong(ctx, optVal.getAsLong()) : ctx;
        }
    }
}
