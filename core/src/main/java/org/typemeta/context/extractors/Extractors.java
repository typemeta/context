package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * A set of combinator methods for constructing {@link Extractor} extractors.
 */
public abstract class Extractors {

    /**
     * Create an {@code Extractor} for enum values.
     * @param <ENV>     the environment type
     * @param <E>       the enum type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <ENV, E extends Enum<E>> Extractor<ENV, E> enumExtractor(
            Class<E> enumType,
            Extractor<ENV, String> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }

    /**
     * A combinator function to convert an {@link Extractor} into one for {@link Optional} values.
     * The option extractor converts null values to {@code Optional.empty}.
     * @param extr      the extractor function for the value type
     * @param <ENV>     the environment type
     * @param <T>       the value type
     * @return          the extractor function for the optional value
     */
    public static <ENV, T> Extractor<ENV, Optional<T>> optional(Extractor<ENV, T> extr) {
        return extr.map(Optional::ofNullable);
    }

    /**
     * A combinator function to convert a collection of extractors into an extractor for a list.
     * @param extrs     the collection of extractors
     * @param <ENV>     the environment type
     * @param <T>       the extractor value type
     * @return          an extractor for a list of values
     */
    public static <ENV, T> Extractor<ENV, List<T>> list(Collection<Extractor<ENV, T>> extrs) {
        return env ->
                extrs.stream()
                        .map(ext -> ext.extract(env))
                        .collect(toList());
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param f         the value constructor
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <ENV, A, B, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Functions.F2<A, B, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param f         the value constructor
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Functions.F3<A, B, C, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env), exC.extract(env));
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the fourth extractor
     * @param f         the value constructor
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Extractor<ENV, D> exD,
            Functions.F4<A, B, C, D, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env), exC.extract(env), exD.extract(env));
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
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Extractor<ENV, D> exD,
            Extractor<ENV, E> exE,
            Functions.F5<A, B, C, D, E, R> f
    ) {
        return env -> f.apply(
                exA.extract(env),
                exB.extract(env),
                exC.extract(env),
                exD.extract(env),
                exE.extract(env)
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
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <F>       the type of value returned by the sixth extractor
     * @param <R>       the value type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, F, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Extractor<ENV, D> exD,
            Extractor<ENV, E> exE,
            Extractor<ENV, F> exF,
            Functions.F6<A, B, C, D, E, F, R> f
    ) {
        return env -> f.apply(
                exA.extract(env),
                exB.extract(env),
                exC.extract(env),
                exD.extract(env),
                exE.extract(env),
                exF.extract(env)
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
     * @param <ENV>     the environment type
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
    public static <ENV, A, B, C, D, E, F, G, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Extractor<ENV, D> exD,
            Extractor<ENV, E> exE,
            Extractor<ENV, F> exF,
            Extractor<ENV, G> exG,
            Functions.F7<A, B, C, D, E, F, G, R> f
    ) {
        return env -> f.apply(
                exA.extract(env),
                exB.extract(env),
                exC.extract(env),
                exD.extract(env),
                exE.extract(env),
                exF.extract(env),
                exG.extract(env)
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
     * @param <ENV>     the environment type
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
    public static <ENV, A, B, C, D, E, F, G, H, R> Extractor<ENV, R> combine(
            Extractor<ENV, A> exA,
            Extractor<ENV, B> exB,
            Extractor<ENV, C> exC,
            Extractor<ENV, D> exD,
            Extractor<ENV, E> exE,
            Extractor<ENV, F> exF,
            Extractor<ENV, G> exG,
            Extractor<ENV, H> exH,
            Functions.F8<A, B, C, D, E, F, G, H, R> f
    ) {
        return env -> f.apply(
                exA.extract(env),
                exB.extract(env),
                exC.extract(env),
                exD.extract(env),
                exE.extract(env),
                exF.extract(env),
                exG.extract(env),
                exH.extract(env)
        );
    }

    /**
     * Combinator function for building a extractor from a set of extractors
     * and a constructor function.
     * @param f         the value constructor
     * @param exs       an array of the extractors
     * @param <ENV>     the environment type
     * @param <R>       the value type
     * @return          the new extractor
     */
    @SafeVarargs
    public static <ENV, R> Extractor<ENV, R> combine(
            Functions.F<Object[], R> f,
            Extractor<ENV, ?> ... exs
    ) {
        return env -> {
            final Object[] vals = Arrays.stream(exs).map(ex -> ex.extract(env)).toArray();
            return f.apply(vals);
        };
    }
}
