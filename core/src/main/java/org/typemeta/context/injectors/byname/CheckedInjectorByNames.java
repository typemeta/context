package org.typemeta.context.injectors.byname;

public abstract class CheckedInjectorByNames {
    /**
     * Create a {@link InjectorByName.Checked} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param <EX>      the exception type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E>, EX extends Exception>
    InjectorByName.Checked<CTX, E, EX> enumInjector(InjectorByName.Checked<CTX, String, EX> strInjr) {
        return strInjr.premap(Enum::name);
    }
}
