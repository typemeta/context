package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.DoubleExtractor;
import org.typemeta.context.functions.Functions;

import java.util.function.DoubleFunction;

/**
 * A function to extract a double value from an context, given a name.
 * Essentially a specialisation of {@link ExtractorByName} for double values.
 * @param <CTX>     the context type
 */
@FunctionalInterface
public interface DoubleExtractorByName<CTX> extends ExtractorByName<CTX, Double> {
    /**
     * Static constructor.
     * @param extr      the extractor
     * @param <CTX>     the context type
     * @return          the extractor
     */
    static <CTX> DoubleExtractorByName<CTX> of(DoubleExtractorByName<CTX> extr) {
        return extr;
    }

    /**
     * Extract a double value from the given context, for the given name.
     * A variant of the {@link ExtractorByName#extract} method specialised for double values.
     * @param ctx       the context
     * @return          the extracted value
     */
    double extractDouble(CTX ctx, String name);

    /**
     * Extract a value of type {@code T} from the given context,
     * for the given name.
     * @param ctx       the context
     * @param name      the index
     * @return          the extracted value
     */
    @Override
    default Double extract(CTX ctx, String name) {
        return extractDouble(ctx, name);
    }

    @Override
    default <U> ExtractorByName<CTX, U> map(Functions.F<Double, U> f) {
        return mapDouble(f::apply);
    }

    /**
     * Convert this extractor into one that applies a function to the result of this extractor.
     * A variant of the {@link ExtractorByName#map} method specialised for double values.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the mapped extractor
     */
    default <U> ExtractorByName<CTX, U> mapDouble(DoubleFunction<U> f) {
        return (ctx, name) -> f.apply(extractDouble(ctx, name));
    }

    @Override
    default DoubleExtractor<CTX> bind(String name) {
        return ctx -> extractDouble(ctx, name);
    }

    /**
     * A specialisation of {@link ExtractorByName.Checked} for double values.
     * @param <CTX>     the context type
     * @param <EX>      the exception type
     */
    interface Checked<CTX, EX extends Exception> extends ExtractorByName.Checked<CTX, Double, EX> {
        /**
         * Static constructor method.
         * @param extr      the extractor
         * @param <CTX>     the context type
         * @param <EX>      the exception type
         * @return          the extractor
         */
        static <CTX, EX extends Exception> Checked<CTX, EX> of(Checked<CTX, EX> extr) {
            return extr;
        }

        /**
         * Extract a double value from the given context, for the given name.
         * A variant of the {@link ExtractorByName.Checked#extract} method specialised for double values.
         * @param ctx       the context
         * @param name      the name
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        double extractDouble(CTX ctx, String name) throws EX;

        @Override
        default Double extract(CTX ctx, String name) throws EX {
            return extractDouble(ctx, name);
        }

        @Override
        default <U> ExtractorByName.Checked<CTX, U, EX> map(Functions.F<Double, U> f) {
            return mapDouble(f::apply);
        }

        /**
         * Convert this extractor into one that applies a function to the result of this extractor.
         * A variant of the {@link ExtractorByName.Checked#map} method specialised for double values.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the mapped extractor
         */
        default <U> ExtractorByName.Checked<CTX, U, EX> mapDouble(DoubleFunction<U> f) {
            return (ctx, name) -> f.apply(extract(ctx, name));
        }

        @Override
        default DoubleExtractor.Checked<CTX, EX> bind(String name) {
            return ctx -> extractDouble(ctx, name);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw).
         * @return the unchecked extractor
         */
        default DoubleExtractorByName<CTX> unchecked() {
            return (ctx, name) -> {
                try {
                    return extractDouble(ctx, name);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            };
        }
    }
}
