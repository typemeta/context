package org.typemeta.context.injextors;

import org.typemeta.context.extractors.*;
import org.typemeta.context.functions.Functions;
import org.typemeta.context.injectors.*;

public class InjExtor<CTX, IN, OUT> {
    public static<CTX, IN, OUT, A> InjExtor<CTX, IN, OUT> combine(
            InjExtor<CTX, IN, A> inJexA,
            Functions.F<A, OUT> f
    ) {
        return new InjExtor<>(
                Extractors.combine(inJexA.extractor(), f),
                Injectors.combine(inJexA.injector())
        );
    }

    public static<CTX, IN, OUT, A, B> InjExtor<CTX, IN, OUT> combine(
            InjExtor<CTX, IN, A> inJexA,
            InjExtor<CTX, IN, B> inJexB,
            Functions.F2<A, B, OUT> f
    ) {
        return new InjExtor<>(
                Extractors.combine(inJexA.extractor(), inJexB.extractor(), f),
                Injectors.combine(inJexA.injector(), inJexB.injector())
        );
    }

    public static<CTX, IN, OUT, A, B, C> InjExtor<CTX, IN, OUT> combine(
            InjExtor<CTX, IN, A> inJexA,
            InjExtor<CTX, IN, B> inJexB,
            InjExtor<CTX, IN, C> inJexC,
            Functions.F3<A, B, C, OUT> f
    ) {
        return new InjExtor<>(
                Extractors.combine(inJexA.extractor(), inJexB.extractor(), inJexC.extractor(), f),
                Injectors.combine(inJexA.injector(), inJexB.injector(), inJexC.injector())
        );
    }

    private final Extractor<CTX, OUT> extractor;
    private final Injector<CTX, IN> injector;

    public InjExtor(Extractor<CTX, OUT> extractor, Injector<CTX, IN> injector) {
        this.extractor = extractor;
        this.injector = injector;
    }

    public Extractor<CTX, OUT> extractor() {
        return extractor;
    }

    public Injector<CTX, IN> injector() {
        return injector;
    }

    public OUT extract(CTX ctx) {
        return extractor.extract(ctx);
    }

    public CTX inject(CTX ctx, IN value) {
        return injector.inject(ctx, value);
    }

    public <IN2, OUT2> InjExtor<CTX, IN2, OUT2> map(Functions.F<OUT, OUT2> extF, Functions.F<IN2, IN> injF) {
        return new InjExtor<>(extractor.map(extF), injector.premap(injF));
    }
}
