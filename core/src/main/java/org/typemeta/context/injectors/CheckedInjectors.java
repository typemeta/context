package org.typemeta.context.injectors;

public abstract class CheckedInjectors {

    /**
     * Combinator function for building an injector from an array of injectors.
     * @param exs       the array of the extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T, EX extends Exception> Injector.Checked<CTX, T, EX> combine(
            Injector.Checked<CTX, T, EX>... exs
    ) {
        return (ctx, value) -> {
            for(Injector.Checked<CTX, T, EX> ex : exs) {
                ctx = ex.inject(ctx, value);
            }
            return ctx;
        };
    }
}
