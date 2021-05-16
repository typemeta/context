package org.typemeta.context.injectors.byindex;

import org.typemeta.context.utils.Exceptions;

import java.util.OptionalLong;

/**
 * A function to inject a long value into an context, given an index.
 * Essentially a specialisation of {@link InjectByIndex} for long values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface LongInjectByIndex<CTX> extends InjectByIndex<CTX, Long> {
    /**
     * Static constructor.
     * @param injr      the injector
     * @param <CTX>     the context type
     * @return          the injector
     */
    static <CTX> LongInjectByIndex<CTX> of(LongInjectByIndex<CTX> injr) {
        return injr;
    }

    /**
     * A variant of the {@link InjectByIndex#inject} method specialised for {@code long} values.
     * @param ctx       the context
     * @param index     the index
     * @param value     the value to be injected
     * @return          the context
     */
    CTX injectLong(CTX ctx, int index, long value);

    default CTX inject(CTX ctx, int index, Long value) {
        return injectLong(ctx, index, value);
    }

    /**
     * Convert this injector into one that accepts optional values.
     * @return          the injector for optional values
     */
    default InjectByIndex<CTX, OptionalLong> optionalLong() {
        return (ctx, index, optVal) ->
                optVal.isPresent() ? inject(ctx, index, optVal.getAsLong()) : ctx;
    }

    /**
     * A variation of {@link LongInjectByIndex} that may throw an exception.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<CTX, EX extends Exception> extends InjectByIndex.Checked<CTX, Long, EX> {
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
         * A variant of the {@link InjectByIndex.Checked#inject} method specialised for {@code long} values.
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
         * Convert this injector into one that accepts optional values.
         * @return          the injector for optional values
         */
        default InjectByIndex.Checked<CTX, OptionalLong, EX> optionalLong() {
            return (ctx, index, optVal) ->
                    optVal.isPresent() ? inject(ctx, index, optVal.getAsLong()) : ctx;
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default LongInjectByIndex<CTX> unchecked() {
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
