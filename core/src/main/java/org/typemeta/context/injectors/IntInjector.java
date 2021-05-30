package org.typemeta.context.injectors;

import java.util.OptionalInt;

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
     * A {@link Injector.Checked} specialised for integer values.
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
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalInt, EX> optionalInt() {
            return (ctx, optVal) -> optVal.isPresent() ? injectInt(ctx, optVal.getAsInt()) : ctx;
        }
    }
}
