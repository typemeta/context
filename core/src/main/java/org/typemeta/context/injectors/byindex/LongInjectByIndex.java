package org.typemeta.context.injectors.byindex;

@FunctionalInterface
public interface LongInjectByIndex<ENV> extends InjectByIndex<ENV, Long> {
    ENV injectLong(ENV env, int n, double value);

    default ENV inject(ENV env, int n, Long value) {
        return injectLong(env, n, value);
    }
}
