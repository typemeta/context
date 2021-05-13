package org.typemeta.context.injectors.byindex;

import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.Injector;

/**
 * A {@link InjectByIndex} is an injector that binds to a number.
 * @param <ENV>     the context type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface InjectByIndex<ENV, T> {
    /**
     * Inject a value into a context.
     * @param env       the context
     * @param n         the index
     * @param value     the value
     * @return          the new context
     */
    ENV inject(ENV env, int n, T value);

    /**
     * Bind this injector to an index.
     * @param n         the index
     * @return          the new injector
     */
    default Injector<ENV, T> bind(int n) {
        return (env, value) -> inject(env, n, value);
    }

    /**
     * Return an injector which first applies the given function to the value.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new injector
     */
    default <U> InjectByIndex<ENV, U> premap(Functions.F<U, T> f) {
        return (env, n, value) -> inject(env, n, f.apply(value));
    }
}
