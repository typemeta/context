package org.typemeta.context.injectors;

public abstract class CheckedInjectors {
    /**
     * Create a {@link Injector} for enum values
     * @param strInjr   the string injector
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @return          the enum injector
     */
    public static <CTX, E extends Enum<E> , EX extends Exception>
    Injector.Checked<CTX, E, EX> enumInjector(Injector.Checked<CTX, String, EX> strInjr) {
        return strInjr.premap(Enum::name);
    }

    private CheckedInjectors() {}

    /**
     * Construct an injector by combining the given injectors.
     * The new injector applies each of the given injectors in turn.
     * @param exs       the array of the extractors
     * @param <CTX>     the context type
     * @param <T>       the injector value type
     * @return          the new injector
     */
    @SafeVarargs
    public static <CTX, T, EX extends Exception> Injector.Checked<CTX, T, EX> combine(
            Injector.Checked<CTX, T, EX>... exs
    ) {
        return (ctx, value) -> {
            for(Injector.Checked<CTX, T, EX> ex : exs) {
                ctx = ex.inject(ctx, value);
            }
            return ctx;
        };
    }
}
