package org.typemeta.context.injectors.byname;

import org.typemeta.context.injectors.IntInjector;
import org.typemeta.context.utils.Exceptions;

import java.util.OptionalInt;

/**
 * A function to inject an integer value into an context, given an name.
 * Essentially a specialisation of {@link InjectorByName} for integer values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface IntInjectorByName<CTX> extends InjectorByName<CTX, Integer> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> IntInjectorByName<CTX> of(IntInjectorByName<CTX> injr) {
        return injr;
    }

    /**
     * Inject a value into a context.
     * A variant of the {@link InjectorByName#inject} method specialised for integer values.
     * @param ctx       the context
     * @param name      the name
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectInt(CTX ctx, String name, int value);

    default CTX inject(CTX ctx, String name, Integer value) {
        return injectInt(ctx, name, value);
    }

    @Override
    default IntInjector<CTX> bind(String name) {
        return (ctx, value) -> injectInt(ctx, name, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectorByName<CTX, OptionalInt> optionalInt() {
        return (ctx, name, optVal) ->
                optVal.isPresent() ? inject(ctx, name, optVal.getAsInt()) : ctx;
    }

    /**
     * A variation of {@link IntInjectorByName} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectorByName.Checked<CTX, Integer, EX> {
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
         * Inject a value into a context.
         * A variant of the {@link InjectorByName.Checked#inject} method specialised for integer values.
         * @param ctx       the context
         * @param name      the name
         * @param value     the value to be injected
         * @return          the context
         */
        CTX injectInt(CTX ctx, String name, int value) throws EX;

        @Override
        default CTX inject(CTX ctx, String name, Integer value) throws EX {
            return injectInt(ctx, name, value);
        }

        /**
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectorByName.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, name, optVal) ->
                    optVal.isPresent() ? inject(ctx, name, optVal.getAsInt()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default IntInjectorByName<CTX> unchecked() {
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
