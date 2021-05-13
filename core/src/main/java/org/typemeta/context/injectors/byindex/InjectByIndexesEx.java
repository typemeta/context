package org.typemeta.context.injectors.byindex;

import java.util.*;

public abstract class InjectByIndexesEx {

    public static <CTX, T, EX extends Exception> InjectByIndex.Checked<CTX, Optional<T>, EX> optional(
            InjectByIndex.Checked<CTX, T, EX> injr
    ) {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? injr.inject(ctx, n, optVal.get()) : ctx;
    }

    public static <CTX, EX extends Exception> InjectByIndex.Checked<CTX, OptionalDouble, EX> optional(
            DoubleInjectByIndex.Checked<CTX, EX> injr
    ) {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? injr.inject(ctx, n, optVal.getAsDouble()) : ctx;
    }

    public static <CTX, EX extends Exception> InjectByIndex.Checked<CTX, OptionalInt, EX> optional(
            IntInjectByIndex.Checked<CTX, EX> injr
    ) {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? injr.inject(ctx, n, optVal.getAsInt()) : ctx;
    }

    public static <CTX, EX extends Exception> InjectByIndex.Checked<CTX, OptionalLong, EX> optional(
            LongInjectByIndex.Checked<CTX, EX> injr
    ) {
        return (ctx, n, optVal) ->
                optVal.isPresent() ? injr.inject(ctx, n, optVal.getAsLong()) : ctx;
    }
}
