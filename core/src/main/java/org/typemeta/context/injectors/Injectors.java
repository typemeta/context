package org.typemeta.context.injectors;

public abstract class Injectors {

    private Injectors() {}

    /**
     * Construct an injector by combining the given injectors.
     * @param injs      the array of the extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T> Injector<CTX, T> combine(
            Injector<CTX, T> ... injs
    ) {
        return (ctx, value) -> {
            for(Injector<CTX, T> inj : injs) {
                ctx = inj.inject(ctx, value);
            }
            return ctx;
        };
    }
}
