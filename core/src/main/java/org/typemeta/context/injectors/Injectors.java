package org.typemeta.context.injectors;

public abstract class Injectors {

    /**
     * A combinator function for building an injector from an array of injectors.
     * @param exs       the array of the extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T> Injector<CTX, T> combine(
            Injector<CTX, T> ... exs
    ) {
        return (ctx, value) -> {
            for(Injector<CTX, T> ex : exs) {
                ctx = ex.inject(ctx, value);
            }
            return ctx;
        };
    }
}
