package org.typemeta.context.injectors.byindex;

import org.typemeta.context.injectors.*;
import org.typemeta.context.utils.Exceptions;

import java.util.OptionalLong;
import java.util.function.*;

/**
 * A function to inject a long value into an context, given an index.
 * Essentially a specialisation of {@link InjectorByIndex} for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjectorByIndex<CTX> extends InjectorByIndex<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> LongInjectorByIndex<CTX> of(LongInjectorByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side-effect.
     * @param <CTX>
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, int index, long value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongInjectorByIndex<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, index, value) -> {
            f.inject(ctx, index, value);
            return ctx;
        };
    }

    /**
     * Inject a long value into a context.
     * A variant of the {@link InjectorByIndex#inject} method specialised for long values.
     * @param ctx       the context
     * @param index     the index
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectLong(CTX ctx, int index, long value);

    default CTX inject(CTX ctx, int index, Long value) {
        return injectLong(ctx, index, value);
    }

    @Override
    default LongInjector<CTX> bind(int index) {
        return (ctx, value) -> injectLong(ctx, index, value);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByIndex<CTX, U> premap(ToLongFunction<U> f) {
        return (ctx, index, value) -> inject(ctx, index, f.applyAsLong(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByIndex<CTX, OptionalLong> optionalLong() {
        return (ctx, index, optVal) ->
                optVal.isPresent() ? inject(ctx, index, optVal.getAsLong()) : ctx;
    }

    /**
     * A variation of {@link LongInjectorByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByIndex.Checked<CTX, Long, EX> {
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
            void inject(CTX ctx, int index, long value) throws EX;
        }

        /**
         * Construct an injector from a {@link Injector.SideEffect} function.
         * @param f         a function that injects the value as a side effect
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> ofSideEffect(SideEffect<CTX, EX> f) {
            return (ctx, index, value) -> {
                f.inject(ctx, index, value);
                return ctx;
            };
        }

        /**
         * Inject a long value into a context.
         * A variant of the {@link InjectorByIndex.Checked#inject} method specialised for long values.
         * @param ctx       the context
         * @param index         the index
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectLong(CTX ctx, int index, long value) throws EX;

        @Override
        default CTX inject(CTX ctx, int index, Long value) throws EX {
            return injectLong(ctx, index, value);
        }

        /**
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> InjectorByIndex.Checked<CTX, U, EX> premap(ToLongFunction<U> f) {
            return (ctx, index, value) -> inject(ctx, index, f.applyAsLong(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByIndex.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, index, optVal) ->
                    optVal.isPresent() ? inject(ctx, index, optVal.getAsLong()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongInjectorByIndex<CTX> unchecked() {
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
