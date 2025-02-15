package org.typemeta.context.utils;

import org.typemeta.context.functions.CheckedFunctions;
import org.typemeta.context.functions.Functions;

/**
 * Utility functions relating to exceptions.
 */
@SuppressWarnings("unchecked")
public abstract class Exceptions {

    /**
     * Wrap a function which throws a checked exception
     * into one that that throws a hidden checked exception.
     * The original exception can be rethrown by an enclosing call to {@link #unwrap(Functions.F0)}.
     * @param thrower   the function that may throw
     * @param <R>       the return type of function
     * @return          the result of function if it doesn't throw
     */
    public static <R> R wrap(CheckedFunctions.F0<R> thrower) {
        try {
            return thrower.apply();
        } catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Wrap a function which throws a checked exception
     * into one that that throws a hidden checked exception.
     * The original exception can be rethrown by an enclosing call to {@link #unwrap(Functions.F0)}.
     * @param thrower   the function that may throw
     * @param <T>       the input type of the function
     * @param <R>       the return type of function
     * @return          a function which throws an unchecked exception
     */
    public static <T, R> Functions.F<T, R> wrap(CheckedFunctions.F<T, R> thrower) {
        return t -> {
            try {
                return thrower.apply(t);
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * Wrap a function which throws a checked exception
     * into one that that throws a hidden checked exception.
     * The original exception can be rethrown by an enclosing call to {@link #unwrap(Functions.F0)}.
     * @param thrower   the function that may throw
     * @param <A>       the type of the first argument of the function
     * @param <B>       the type of the second argument of the function
     * @param <R>       the return type of function
     * @return          a function which throws an unchecked exception
     */
    public static <A, B, R>
    Functions.F2<A, B, R> wrap(CheckedFunctions.F2<A, B, R> thrower) {
        return (a, b) -> {
            try {
                return thrower.apply(a, b);
            } catch(Exception ex) {
                throw new RuntimeException(ex);
            }
        };
    }

    /**
     * Undo the effect of {@code wrap} by catching the exception,
     * and rethrowing the original checked exception.
     * @param thrower   the function that may throw an exception
     * @param <R>       the function return type
     * @param <X>       the original exception type
     * @return          the function value, if it doesn't throw
     * @throws X        the original exception
     */
    public static <R, X extends Exception> R unwrap(Functions.F0<R> thrower) throws X {
        try {
            return thrower.apply();
        } catch (Exception ex) {
            throw (X)ex;
        }
    }
}