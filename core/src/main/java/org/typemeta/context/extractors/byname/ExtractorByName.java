package org.typemeta.context.extractors.byname;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.functions.Functions;
import org.typemeta.context.utils.Exceptions;

/**
 * A function to extract a value from an environment, given a name.
 * @param <ENV>     the environment type
 * @param <T>       the value type
 */
@FunctionalInterface
public interface ExtractorByName<ENV, T> {
    static <ENV, T> ExtractorByName<ENV, T> of(ExtractorByName<ENV, T> extr) {
        return extr;
    }

    /**
     * Extract a value of type {@code T} from the given environment,
     * for the given column name.
     * @param env       the environment
     * @param name      the name
     * @return          the extracted value
     */
    T extract(ENV env, String name);

    /**
     * Convert this extractor into another that applies a function to the result of this extractor.
     * @param f         the function
     * @param <U>       the function return type
     * @return          the new extractor
     */
    default <U> ExtractorByName<ENV, U> map(Functions.F<T, U> f) {
        return (rs, name) -> f.apply(extract(rs, name));
    }

    /**
     * Bind this extractor to a name, giving us an {@link Extractor}.
     * @param name      the column name
     * @return          the extractor
     */
    default Extractor<ENV, T> bind(String name) {
        return rs -> extract(rs, name);
    }

    /**
     * Variant of {@link ExtractorByName} where the extract method may throw an exception.
     * @param <ENV>     the environment type
     * @param <T>       the value type
     * @param <EX>      the exception type
     */
    @FunctionalInterface
    interface Checked<ENV, T, EX extends Exception> {
        static <ENV, T, EX extends Exception> Checked<ENV, T, EX> of(Checked<ENV, T, EX> extr) {
            return  extr;
        }

        /**
         * Extract a value of type {@code T} from the given environment,
         * for the given column name.
         * @param env       the environment
         * @param name      the name
         * @return          the extracted value
         * @throws EX       if the extraction fails
         */
        T extract(ENV env, String name) throws EX;

        /**
         * Convert this extractor into another that applies a function to the result of this extractor.
         * @param f         the function
         * @param <U>       the function return type
         * @return          the new extractor
         */
        default <U> Checked<ENV, U, EX> map(Functions.F<T, U> f) {
            return (rs, name) -> f.apply(extract(rs, name));
        }

        /**
         * Bind this extractor to a name, giving us an {@link Extractor}.
         * @param name      the column name
         * @return          the extractor
         */
        default Extractor.Checked<ENV, T, EX> bind(String name) {
            return rs -> extract(rs, name);
        }

        /**
         * Convert this extractor to an unchecked extractor (one that doesn't throw a checked exception).
         * @return          the unchecked extractor
         */
        default ExtractorByName<ENV, T> unchecked() {
            return (env, name) -> {
                try {
                    return extract(env, name);
                } catch (Exception ex) {
                    return Exceptions.throwUnchecked(ex);
                }
            };
        }
    }
}
