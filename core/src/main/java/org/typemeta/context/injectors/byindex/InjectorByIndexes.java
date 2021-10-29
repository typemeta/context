package org.typemeta.context.injectors.byindex;

public abstract class InjectorByIndexes {
    /**
     * Create a {@link InjectorByIndex} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E>>
    InjectorByIndex<CTX, E> enumInjector(InjectorByIndex<CTX, String> strInjr) {
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
    public static <CTX, T> InjectorByIndex<CTX, T> combine(
            InjectorByIndex<CTX, T> ... injs
    ) {
        return (ctx, idx, value) -> {
            for(InjectorByIndex<CTX, T> inj : injs) {
                ctx = inj.inject(ctx, idx, value);
            }
            return ctx;
        };
    }
}
