package org.typemeta.context.injectors.byindex;

import java.util.*;

public abstract class InjectByIndexes {

    public static <ENV, T> InjectByIndex<ENV, Optional<T>> optional(InjectByIndex<ENV, T> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.get()) : env;
    }

    public static <ENV> InjectByIndex<ENV, OptionalDouble> optional(DoubleInjectByIndex<ENV> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsDouble()) : env;
    }

    public static <ENV> InjectByIndex<ENV, OptionalInt> optional(IntInjectByIndex<ENV> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsInt()) : env;
    }

    public static <ENV> InjectByIndex<ENV, OptionalLong> optional(LongInjectByIndex<ENV> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsLong()) : env;
    }
}
