package org.typemeta.context.properties;

import org.typemeta.context.extractors.*;
import org.typemeta.context.injectors.*;

import java.time.LocalDate;
import java.util.*;

public class Example {
    static class Config {
        final LocalDate endDate;
        final OptionalInt numThreads;
        final String env;

        Config(LocalDate endDate, OptionalInt numThreads, String env) {
            this.endDate = endDate;
            this.numThreads = numThreads;
            this.env = env;
        }

        @Override
        public String toString() {
            return "Config{" +
                    "endDate=" + endDate +
                    ", numThreads=" + numThreads +
                    ", env='" + env + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object rhs) {
            if (this == rhs) {
                return true;
            } else if (rhs == null || getClass() != rhs.getClass()) {
                return false;
            } else {
                final Config rhsT = (Config) rhs;
                return endDate.equals(rhsT.endDate) &&
                        numThreads.equals(rhsT.numThreads) &&
                        env.equals(rhsT.env);
            }
        }

        @Override
        public int hashCode() {
            return Objects.hash(endDate, numThreads, env);
        }

        public LocalDate endDate() {
            return endDate;
        }

        public OptionalInt numThreads() {
            return numThreads;
        }

        public String env() {
            return env;
        }
    }

    static final Injector<Properties, Config> INJR =
            Injectors.combine(
                    PropertiesInjectors.LOCALDATE.bind("endDate").premap(Config::endDate),
                    PropertiesInjectors.OPT_INTEGER.bind("numThreads").premap(Config::numThreads),
                    PropertiesInjectors.STRING.bind("env").premap(Config::env)
            );

    static final Extractor<Properties, Config> EXTR =
            Extractors.combine(
                    PropertiesExtractors.LOCALDATE.bind("endDate"),
                    PropertiesExtractors.OPT_INTEGER.bind("numThreads"),
                    PropertiesExtractors.STRING.bind("env"),
                    Config::new
            );

    public static void main(String[] args) {
        final Config before = new Config(
                LocalDate.of(2021, 04, 19),
                OptionalInt.of(24),
                "DEV"
        );

        final Properties props = new Properties();

        INJR.inject(props, before);

        System.out.println(props);

        final Config after = EXTR.extract(props);

        assert(before.equals(after));
    }
}
