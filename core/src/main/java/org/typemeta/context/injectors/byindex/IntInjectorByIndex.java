package org.typemeta.context.injectors.byindex;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.Injector;
import org.typemeta.context.injectors.IntInjector;

import java.util.OptionalInt;
import java.util.function.ToIntFunction;

/**
 * A function to inject an integer value into an context, given an index.
 * Essentially a specialisation of {@link InjectorByIndex} for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntInjectorByIndex<CTX> extends InjectorByIndex<CTX, Integer> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> IntInjectorByIndex<CTX> of(IntInjectorByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of {@code Injector} that modifies the given context as a side effect.
     * @param <CTX>
     */
    @FunctionalInterface
    interface SideEffect<CTX> {
        void inject(CTX ctx, int index, int value);
    }

    /**
     * Construct an injector from a {@link SideEffect} function.
     * @param f         a function that injects the value as a side effect
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> IntInjectorByIndex<CTX> ofSideEffect(SideEffect<CTX> f) {
        return (ctx, index, value) -> {
            f.inject(ctx, index, value);
            return ctx;
        };
    }

    /**
     * Inject a integer value into a context.
     * A variant of the {@link InjectorByIndex#inject} method specialised for integer values.
     * @param ctx       the context
     * @param index     the index
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectInt(CTX ctx, int index, int value);

    default CTX inject(CTX ctx, int index, Integer value) {
        return injectInt(ctx, index, value);
    }

    @Override
    default IntInjector<CTX> bind(int index) {
        return (ctx, value) -> injectInt(ctx, index, value);
    }

    @Override
    default <U> InjectorByIndex<CTX, U> premap(Functions.F<U, Integer> f) {
        return premapInt(f::apply);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectorByIndex<CTX, U> premapInt(ToIntFunction<U> f) {
        return (ctx, index, value) -> inject(ctx, index, f.applyAsInt(value));
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByIndex<CTX, OptionalInt> optionalInt() {
        return (ctx, index, optVal) ->
                optVal.isPresent() ? inject(ctx, index, optVal.getAsInt()) : ctx;
    }

    /**
     * A variation of {@link IntInjectorByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByIndex.Checked<CTX, Integer, EX> {
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
            void inject(CTX ctx, int index, int value) throws EX;
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
         * Inject a integer value into a context.
         * A variant of the {@link InjectorByIndex.Checked#inject} method specialised for integer values.
         * @param ctx       the context
         * @param index     the index
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectInt(CTX ctx, int index, int value) throws EX;

        @Override
        default CTX inject(CTX ctx, int index, Integer value) throws EX {
            return injectInt(ctx, index, value);
        }

        @Override
        default <U> InjectorByIndex.Checked<CTX, U, EX> premap(Functions.F<U, Integer> f) {
            return premapInt(f::apply);
        }

        /**
         * Return an injector which first applies the given function to the value.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new injector
         */
        default <U> InjectorByIndex.Checked<CTX, U, EX> premapInt(ToIntFunction<U> f) {
            return (ctx, index, value) -> inject(ctx, index, f.applyAsInt(value));
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByIndex.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, index, optVal) ->
                    optVal.isPresent() ? inject(ctx, index, optVal.getAsInt()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default IntInjectorByIndex<CTX> unchecked() {
            return (ctx, index, value)  -> {
                try {
                    return inject(ctx, index, value);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
