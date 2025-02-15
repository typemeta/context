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

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
     * @param injs      the array of the extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T, EX extends Exception> InjectorByIndex.Checked<CTX, T, EX> combine(
            InjectorByIndex.Checked<CTX, T, EX> ... injs
    ) {
        return (ctx, idx, value) -> {
            for(InjectorByIndex.Checked<CTX, T, EX> inj : injs) {
                ctx = inj.inject(ctx, idx, value);
            }
            return ctx;
        };
    }

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
     * @param injs      the iterable of extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    public static <CTX, T, EX extends Exception> InjectorByIndex.Checked<CTX, T, EX> combine(
            Iterable<InjectorByIndex.Checked<CTX, T, EX>> injs
    ) {
        return (ctx, idx, value) -> {
            for(InjectorByIndex.Checked<CTX, T, EX> inj : injs) {
                ctx = inj.inject(ctx, idx, value);
            }
            return ctx;
        };
    }
}
