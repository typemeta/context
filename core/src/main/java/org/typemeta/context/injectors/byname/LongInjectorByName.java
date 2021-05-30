package org.typemeta.context.injectors.byname;

import org.typemeta.context.utils.Exceptions;

import java.util.OptionalLong;

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
     * A variant of the {@link InjectorByName#inject} method specialised for {@code long} values.
     * @param ctx       the context
     * @param name      the name
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectLong(CTX ctx, String name, long value);

    default CTX inject(CTX ctx, String name, Long value) {
        return injectLong(ctx, name, value);
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
         * A variant of the {@link InjectorByName.Checked#inject} method specialised for {@code long} values.
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
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
