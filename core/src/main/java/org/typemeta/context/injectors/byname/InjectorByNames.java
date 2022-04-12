package org.typemeta.context.injectors.byname;

public abstract class InjectorByNames {

    private InjectorByNames() {}

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
}
