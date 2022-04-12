package org.typemeta.context.extractors;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

import java.util.*;

import static java.util.stream.Collectors.toList;

/**
 * A set of combinator methods for constructing {@link Extractor.Checked} extractors.
 */
public abstract class CheckedExtractors {

    private CheckedExtractors() {}

    /**
     * Create a checked extractor for enum values.
     * @param <CTX>     the context type
     * @param <E>       the enum type
     * @param <EX>      the exception type
     * @param enumType  the enum type class
     * @param strExtr   the string extractor
     * @return          the enum extractor
     */
    static <CTX, E extends Enum<E>, EX extends Exception>
    Extractor.Checked<CTX, E, EX> enumExtractor(
            Class<E> enumType,
            Extractor.Checked<CTX, String, EX> strExtr
    ) {
        return strExtr.map(s -> Enum.valueOf(enumType, s));
    }

    /**
     * A combinator function to convert a collection of checked extractors into a checked extractor for a list.
     * @param exs       the collection of extractors
     * @param <CTX>     the context type
     * @param <T>       the extractor value type
     * @param <EX>      the exception type
     * @return          an extractor for a list of values
     */
    public static <CTX, T, EX extends Exception> Extractor.Checked<CTX, List<T>, EX> list(
            Collection<Extractor.Checked<CTX, T, EX>> exs
    ) {
        return ctx ->
                Exceptions.<List<T>, EX>unwrap(() ->
                        exs.stream()
                                .map(ext -> Exceptions.wrap(() -> ext.extract(ctx)))
                                .collect(toList())
                );
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param f         the value constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Functions.F2<A, B, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx));
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Functions.F3<A, B, C, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx), exC.extract(ctx));
    }

    /**
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param exA       the first extractor
     * @param exB       the second extractor
     * @param exC       the third extractor
     * @param exD       the third extractor
     * @param f         the fourth constructor
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Extractor.Checked<CTX, D, EX> exD,
            Functions.F4<A, B, C, D, R> f
    ) {
        return ctx -> f.apply(exA.extract(ctx), exB.extract(ctx), exC.extract(ctx), exD.extract(ctx));
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
     * @param <CTX>     the context type
     * @param <A>       the type of value returned by the first extractor
     * @param <B>       the type of value returned by the second extractor
     * @param <C>       the type of value returned by the third extractor
     * @param <D>       the type of value returned by the fourth extractor
     * @param <E>       the type of value returned by the fifth extractor
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Extractor.Checked<CTX, D, EX> exD,
            Extractor.Checked<CTX, E, EX> exE,
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
     * Combinator function for building a checked extractor from a set of checked extractors
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, F, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Extractor.Checked<CTX, D, EX> exD,
            Extractor.Checked<CTX, E, EX> exE,
            Extractor.Checked<CTX, F, EX> exF,
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
     * @param <CTX>     the context type
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
    public static <CTX, A, B, C, D, E, F, G, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Extractor.Checked<CTX, D, EX> exD,
            Extractor.Checked<CTX, E, EX> exE,
            Extractor.Checked<CTX, F, EX> exF,
            Extractor.Checked<CTX, G, EX> exG,
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
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    public static <CTX, A, B, C, D, E, F, G, H, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Extractor.Checked<CTX, A, EX> exA,
            Extractor.Checked<CTX, B, EX> exB,
            Extractor.Checked<CTX, C, EX> exC,
            Extractor.Checked<CTX, D, EX> exD,
            Extractor.Checked<CTX, E, EX> exE,
            Extractor.Checked<CTX, F, EX> exF,
            Extractor.Checked<CTX, G, EX> exG,
            Extractor.Checked<CTX, H, EX> exH,
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
     * Combinator function for building a checked extractor from a set of checked extractors
     * and a constructor function.
     * @param f         the value constructor
     * @param exs       an array of the extractors
     * @param <CTX>     the context type
     * @param <R>       the value type
     * @param <EX>      the exception type
     * @return          the new extractor
     */
    @SafeVarargs
    public static <CTX, R, EX extends Exception> Extractor.Checked<CTX, R, EX> combine(
            Functions.F<Object[], R> f,
            Extractor.Checked<CTX, ?, EX>... exs
    ) {
        return ctx -> {
            final Object[] vals = new Object[exs.length];
            for (int i = 0; i < exs.length; ++i) {
                vals[i] = exs[i].extract(ctx);
            }
            return f.apply(vals);
        };
    }

}
