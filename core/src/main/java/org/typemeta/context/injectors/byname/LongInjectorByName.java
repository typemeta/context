package org.typemeta.context.injectors.byname;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.Injector;
import org.typemeta.context.injectors.LongInjector;

import java.util.OptionalLong;
import java.util.function.ToLongFunction;

/**
 * A function to inject a long value into an context, given an name.
 * Essentially a specialisation of {@link InjectorByName} for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjectorByName<CTX> extends InjectorByName<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> LongInjectorByName<CTX> of(LongInjectorByName<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>     the context type
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, String name, long value);
    }

    /**
     * Construct an injector from a {@link Injector.SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> LongInjectorByName<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, name, value) -> {
            f.inject(ctx, name, value);
            return ctx;
        };
    }

    /**
     * Inject a long value into a context.
     * A variant of the {@link InjectorByName#inject} method specialised for long values.
     * @param ctx       the context
     * @param name      the name
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectLong(CTX ctx, String name, long value);

    default CTX inject(CTX ctx, String name, Long value) {
        return injectLong(ctx, name, value);
    }

    @Override
    default LongInjector<CTX> bind(String name) {
        return (ctx, value) -> injectLong(ctx, name, value);
    }

    @Override
    default <U> InjectorByName<CTX, U> premap(Functions.F<U, Long> f) {
        return premapLong(f::apply);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByName<CTX, U> premapLong(ToLongFunction<U> f) {
        return (ctx, name, value) -> inject(ctx, name, f.applyAsLong(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByName<CTX, OptionalLong> optionalLong() {
        return (ctx, name, optVal) ->
                optVal.isPresent() ? inject(ctx, name, optVal.getAsLong()) : ctx;
    }

    /**
     * A variation of {@link LongInjectorByName} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByName.Checked<CTX, Long, EX> {
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
            void inject(CTX ctx, String name, long value) throws EX;
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
         * Inject a long value into a context.
         * A variant of the {@link InjectorByName.Checked#inject} method specialised for long values.
         * @param ctx       the context
         * @param name      the name
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectLong(CTX ctx, String name, long value) throws EX;

        @Override
        default CTX inject(CTX ctx, String name, Long value) throws EX {
            return injectLong(ctx, name, value);
        }

        @Override
        default <U> InjectorByName.Checked<CTX, U, EX> premap(Functions.F<U, Long> f) {
            return premapLong(f::apply);
        }

        /**
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> InjectorByName.Checked<CTX, U, EX> premapLong(ToLongFunction<U> f) {
            return (ctx, name, value) -> inject(ctx, name, f.applyAsLong(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByName.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, name, optVal) ->
                    optVal.isPresent() ? inject(ctx, name, optVal.getAsLong()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongInjectorByName<CTX> unchecked() {
            return (ctx, name, value)  -> {
                try {
                    return inject(ctx, name, value);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
