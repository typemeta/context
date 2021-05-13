package org.typemeta.context.injectors.byindex;

import java.util.*;

public abstract class InjectByIndexesEx {

    @FunctionalInterface
    public interface DoubleInjectByIndexEx<ENV, EX extends Exception> extends InjectByIndexEx<ENV, Double, EX> {
        ENV injectDouble(ENV env, int n, double value) throws EX;
        
        default ENV inject(ENV env, int n, Double value) throws EX {
            return injectDouble(env, n, value);
        }
    }

    @FunctionalInterface
    public interface IntInjectByIndexEx<ENV, EX extends Exception> extends InjectByIndexEx<ENV, Integer, EX> {
        ENV injectInt(ENV env, int n, int value) throws EX;

        default ENV inject(ENV env, int n, Integer value) throws EX {
            return injectInt(env, n, value);
        }
    }

    @FunctionalInterface
    public interface LongInjectByIndexEx<ENV, EX extends Exception> extends InjectByIndexEx<ENV, Long, EX> {
        ENV injectLong(ENV env, int n, long value) throws EX;

        default ENV inject(ENV env, int n, Long value) throws EX {
            return injectLong(env, n, value);
        }
    }

    public static <ENV, T, EX extends Exception> InjectByIndexEx<ENV, Optional<T>, EX> optional(
            InjectByIndexEx<ENV, T, EX> injr
    ) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.get()) : env;
    }

    public static <ENV, EX extends Exception> InjectByIndexEx<ENV, OptionalDouble, EX> optional(DoubleInjectByIndexEx<ENV, EX> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsDouble()) : env;
    }

    public static <ENV, EX extends Exception> InjectByIndexEx<ENV, OptionalInt, EX> optional(IntInjectByIndexEx<ENV, EX> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsInt()) : env;
    }

    public static <ENV, EX extends Exception> InjectByIndexEx<ENV, OptionalLong, EX> optional(LongInjectByIndexEx<ENV, EX> injr) {
        return (env, n, optVal) ->
                optVal.isPresent() ? injr.inject(env, n, optVal.getAsLong()) : env;
    }
}
