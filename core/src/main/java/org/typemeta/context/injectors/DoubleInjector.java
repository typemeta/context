package org.typemeta.context.injectors;

import org.typemeta.context.injectors.byname.InjectorByName;

import java.util.OptionalDouble;

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
     * Inject a double value into a context.
     * A variant of the {@link Injector#inject} method specialised for double values.
     * @param ctx       the context
     * @param value     the value
     * @return          the new context
     */
    CTX injectDouble(CTX ctx, double value);

    @Override
    default CTX inject(CTX ctx, Double value) {
        return inject(ctx, value.doubleValue());
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
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default Injector.Checked<CTX, OptionalDouble, EX> optionalDbl() {
            return (ctx, optVal) -> optVal.isPresent() ? injectDouble(ctx, optVal.getAsDouble()) : ctx;
        }
    }
}
