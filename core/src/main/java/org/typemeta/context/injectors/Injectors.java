package org.typemeta.context.injectors;

public abstract class Injectors {

    /**
     * Combine the uilding an injector from an array of injectors.
     * @param ex1       the first extractor
     * @param ex2       the first extractor
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    public static <CTX, T> Injector<CTX, T> combine(
            Injector<CTX, T> ex1,
            Injector<CTX, T> ex2
    ) {
        return (ctx, value) -> ex2.inject(ex1.inject(ctx, value), value);
    }

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
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
