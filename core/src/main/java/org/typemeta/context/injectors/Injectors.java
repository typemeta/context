package org.typemeta.context.injectors;

public abstract class Injectors {
    /**
     * Create a {@link Injector} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E>>
    Injector<CTX, E> enumInjector(Injector<CTX, String> strInjr) {
        return strInjr.premap(Enum::name);
    }

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
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
