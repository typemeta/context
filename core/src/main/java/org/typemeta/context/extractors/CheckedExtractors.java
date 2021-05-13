package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * A set of combinator methods for constructing {@link Extractor.Checked} extractors.
 */
public abstract class CheckedExtractors {

    /**
     * Create a checked extractor for enum values.
     * @param <ENV>     the environment type
     * @param <E>       the enum type
     * @param <EX>      the exception type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <ENV, E extends Enum<E>, EX extends Exception>
    Extractor.Checked<ENV, E, EX> enumExtractor(
            Class<E> enumType,
            Extractor.Checked<ENV, String, EX> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }

    /**
     * A combinator function to convert a checked extractor into one for {@link Optional} values.
     * The option extractor converts null values to {@code Optional.empty}.
     * @param extr      the extractor function for the value type
     * @param <ENV>     the environment type
     * @param <T>       the value type
     * @param <EX>      the exception type
     * @return          the extractor function for the optional value
     */
    public static <ENV, T, EX extends Exception>
    Extractor.Checked<ENV, Optional<T>, EX> optional(Extractor.Checked<ENV, T, EX> extr) {
        return extr.map(Optional::ofNullable);
    }

    /**
     * A combinator function to convert a collection of checked extractors into a checked extractor for a list.
     * @param exs       the collection of extractors
     * @param <ENV>     the environment type
     * @param <T>       the extractor value type
     * @param <EX>      the exception type
     * @return          an extractor for a list of values
     */
    public static <ENV, T, EX extends Exception> Extractor.Checked<ENV, List<T>, EX> list(
            Collection<Extractor.Checked<ENV, T, EX>> exs
    ) {
        return env ->
                Exceptions.<List<T>, EX>unwrap(() ->
                        exs.stream()
                                .map(ext -> Exceptions.wrap(() -> ext.extract(env)))
                                .collect(toList())
                );
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param f         the value constructor
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Functions.F2<A, B, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env));
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Functions.F3<A, B, C, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env), exC.extract(env));
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the third extractor
     * @param f         the fourth constructor
     * @param <ENV>     the environment type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Extractor.Checked<ENV, D, EX> exD,
            Functions.F4<A, B, C, D, R> f
    ) {
        return env -> f.apply(exA.extract(env), exB.extract(env), exC.extract(env), exD.extract(env));
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Extractor.Checked<ENV, D, EX> exD,
            Extractor.Checked<ENV, E, EX> exE,
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
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, F, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Extractor.Checked<ENV, D, EX> exD,
            Extractor.Checked<ENV, E, EX> exE,
            Extractor.Checked<ENV, F, EX> exF,
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
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, F, G, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Extractor.Checked<ENV, D, EX> exD,
            Extractor.Checked<ENV, E, EX> exE,
            Extractor.Checked<ENV, F, EX> exF,
            Extractor.Checked<ENV, G, EX> exG,
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
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <ENV, A, B, C, D, E, F, G, H, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Extractor.Checked<ENV, A, EX> exA,
            Extractor.Checked<ENV, B, EX> exB,
            Extractor.Checked<ENV, C, EX> exC,
            Extractor.Checked<ENV, D, EX> exD,
            Extractor.Checked<ENV, E, EX> exE,
            Extractor.Checked<ENV, F, EX> exF,
            Extractor.Checked<ENV, G, EX> exG,
            Extractor.Checked<ENV, H, EX> exH,
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
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param f         the value constructor
     * @param exs       an array of the extractors
     * @param <ENV>     the environment type
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    @SafeVarargs
    public static <ENV, R, EX extends Exception> Extractor.Checked<ENV, R, EX> combine(
            Functions.F<Object[], R> f,
            Extractor.Checked<ENV, ?, EX>... exs
    ) {
        return env -> {
            final Object[] vals = new Object[exs.length];
            for (int i = 0; i < exs.length; ++i) {
                vals[i] = exs[i].extract(env);
            }
            return f.apply(vals);
        };
    }

}
