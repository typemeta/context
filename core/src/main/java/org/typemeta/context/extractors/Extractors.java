package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A set of combinator methods for constructing {@link Extractor} extractors.
 */
public abstract class Extractors {

    private Extractors() {}

    /**
     * Create an {@code Extractor} for enum values.
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <CTX, E extends Enum<E>> Extractor<CTX, E> enumExtractor(
            Class<E> enumType,
            Extractor<CTX, String> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }

    /**
     * A combinator function to convert a collection of extractors into an extractor for a list.
     * @param extrs     the collection of extractors
     * @param <CTX>     the context type
     * @param <T>       the extractor value type
     * @return          an extractor for a list of values
     */
    public static <CTX, T> Extractor<CTX, List<T>> sequence(Collection<Extractor<CTX, T>> extrs) {
        return ctx -> {
            final List<T> lt = new ArrayList<>(extrs.size());
            for (Extractor<CTX, T> extr : extrs) {
                lt.add(extr.extract(ctx));
            }
            return lt;
        };
    }

    /**
     * Combinator function for building a extractor from a single extractor
     * and a constructor function.
     * @param exA       the first extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Functions.F<A, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Functions.F2<A, B, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Functions.F3<A, B, C, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx), exC.extract(ctx));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Extractor<CTX, D> exD,
            Functions.F4<A, B, C, D, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx), exC.extract(ctx), exD.extract(ctx));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param exE       the fifth extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Extractor<CTX, D> exD,
            Extractor<CTX, E> exE,
            Functions.F5<A, B, C, D, E, R> f
    ) {
        return ctx -> f.apply(
                exA.extract(ctx),
                exB.extract(ctx),
                exC.extract(ctx),
                exD.extract(ctx),
                exE.extract(ctx)
        );
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param exE       the fifth extractor
     * @param exF       the sixth extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <F>       the type of value returned by the sixth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, F, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Extractor<CTX, D> exD,
            Extractor<CTX, E> exE,
            Extractor<CTX, F> exF,
            Functions.F6<A, B, C, D, E, F, R> f
    ) {
        return ctx -> f.apply(
                exA.extract(ctx),
                exB.extract(ctx),
                exC.extract(ctx),
                exD.extract(ctx),
                exE.extract(ctx),
                exF.extract(ctx)
        );
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param exE       the fifth extractor
     * @param exF       the sixth extractor
     * @param exG       the seventh extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <F>       the type of value returned by the sixth extractor
     * @param <G>       the type of value returned by the seventh extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, F, G, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Extractor<CTX, D> exD,
            Extractor<CTX, E> exE,
            Extractor<CTX, F> exF,
            Extractor<CTX, G> exG,
            Functions.F7<A, B, C, D, E, F, G, R> f
    ) {
        return ctx -> f.apply(
                exA.extract(ctx),
                exB.extract(ctx),
                exC.extract(ctx),
                exD.extract(ctx),
                exE.extract(ctx),
                exF.extract(ctx),
                exG.extract(ctx)
        );
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param exE       the fifth extractor
     * @param exF       the sixth extractor
     * @param exG       the seventh extractor
     * @param exH       the eighth extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <F>       the type of value returned by the sixth extractor
     * @param <G>       the type of value returned by the seventh extractor
     * @param <H>       the type of value returned by the eighth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, F, G, H, R> Extractor<CTX, R> combine(
            Extractor<CTX, A> exA,
            Extractor<CTX, B> exB,
            Extractor<CTX, C> exC,
            Extractor<CTX, D> exD,
            Extractor<CTX, E> exE,
            Extractor<CTX, F> exF,
            Extractor<CTX, G> exG,
            Extractor<CTX, H> exH,
            Functions.F8<A, B, C, D, E, F, G, H, R> f
    ) {
        return ctx -> f.apply(
                exA.extract(ctx),
                exB.extract(ctx),
                exC.extract(ctx),
                exD.extract(ctx),
                exE.extract(ctx),
                exF.extract(ctx),
                exG.extract(ctx),
                exH.extract(ctx)
        );
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param f         the value constructor
     * @param exs       an array of the extractors
     * @param <CTX>     the context type
     * @param <R>       the value type
     * @return          the new extractor
     */
    @SafeVarargs
    public static <CTX, R> Extractor<CTX, R> combine(
            Functions.F<Object[], R> f,
            Extractor<CTX, ?> ... exs
    ) {
        return ctx -> {
            final Object[] vals = new Object[exs.length];
            for (int i = 0; i < exs.length; ++i) {
                vals[i] = exs[i].extract(ctx);
            }
            return f.apply(vals);
        };
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param f         the value constructor
     * @param exs       an iterable of the extractors
     * @param <CTX>     the context type
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <CTX, R> Extractor<CTX, R> combine(
            Functions.F<List<Object>, R> f,
            Iterable<Extractor<CTX, ?>> exs
    ) {
        return ctx -> {
            final List<Object> vals = new ArrayList<>();
            for (Extractor<CTX, ?> ex : exs) {
                vals.add(ex.extract(ctx));
            }
            return f.apply(vals);
        };
    }
}
