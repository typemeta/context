package org.typemeta.context.injectors.byindex;

@FunctionalInterface
public interface IntInjectByIndex<ENV> extends InjectByIndex<ENV, Integer> {
    ENV injectInt(ENV env, int n, double value);

    default ENV inject(ENV env, int n, Integer value) {
        return injectInt(env, n, value);
    }
}
