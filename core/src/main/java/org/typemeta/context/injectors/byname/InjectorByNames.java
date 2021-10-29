package org.typemeta.context.injectors.byname;

public abstract class InjectorByNames {
    /**
     * Create a {@link InjectorByName} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E>>
    InjectorByName<CTX, E> enumInjector(InjectorByName<CTX, String> strInjr) {
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
    public static <CTX, T> InjectorByName<CTX, T> combine(
            InjectorByName<CTX, T> ... injs
    ) {
        return (ctx, name, value) -> {
            for(InjectorByName<CTX, T> inj : injs) {
                ctx = inj.inject(ctx, name, value);
            }
            return ctx;
        };
    }
}
