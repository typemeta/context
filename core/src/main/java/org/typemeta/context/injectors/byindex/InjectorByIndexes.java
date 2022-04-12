package org.typemeta.context.injectors.byindex;

public abstract class InjectorByIndexes {

    private InjectorByIndexes() {}

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
}
