package org.typemeta.context.injectors.byindex;

@FunctionalInterface
public interface DoubleInjectByIndex<ENV> extends InjectByIndex<ENV, Double> {
    ENV injectDouble(ENV env, int n, double value);

    default ENV inject(ENV env, int n, Double value) {
        return injectDouble(env, n, value);
    }
}
