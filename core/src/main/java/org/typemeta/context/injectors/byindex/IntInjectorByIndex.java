package org.typemeta.context.injectors.byindex;

import org.typemeta.context.injectors.IntInjector;
import org.typemeta.context.utils.Exceptions;

import java.util.OptionalInt;

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
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
