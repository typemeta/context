package org.typemeta.context.injectors.byname;

public abstract class CheckedInjectorByNames {

    private CheckedInjectorByNames() {}

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

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
     * @param injs      the array of injectors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T, EX extends Exception> InjectorByName.Checked<CTX, T, EX> combine(
            InjectorByName.Checked<CTX, T, EX> ... injs
    ) {
        return (ctx, name, value) -> {
            for(InjectorByName.Checked<CTX, T, EX> inj : injs) {
                ctx = inj.inject(ctx, name, value);
            }
            return ctx;
        };
    }

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
     * @param injs      the iterable of injectors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    public static <CTX, T, EX extends Exception> InjectorByName.Checked<CTX, T, EX> combine(
            Iterable<InjectorByName.Checked<CTX, T, EX>> injs
    ) {
        return (ctx, name, value) -> {
            for(InjectorByName.Checked<CTX, T, EX> inj : injs) {
                ctx = inj.inject(ctx, name, value);
            }
            return ctx;
        };
    }
}
