package org.typemeta.context.injectors.byindex;

public abstract class CheckedInjectorByIndexes {

    private CheckedInjectorByIndexes() {}
    /**
     * Create a {@link InjectorByIndex.Checked} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param <EX>      the exception type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E>, EX extends Exception>
    InjectorByIndex.Checked<CTX, E, EX> enumInjector(InjectorByIndex.Checked<CTX, String, EX> strInjr) {
        return strInjr.premap(Enum::name);
    }
}
