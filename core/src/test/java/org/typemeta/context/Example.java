package org.typemeta.context;

import org.typemeta.context.extractors.Extractor;
import org.typemeta.context.extractors.Extractors;
import org.typemeta.context.extractors.byindex.ExtractorByIndex;
import org.typemeta.context.extractors.byname.ExtractorByName;
import org.typemeta.context.injectors.Injector;
import org.typemeta.context.injectors.Injectors;
import org.typemeta.context.properties.PropertiesExtractors;
import org.typemeta.context.properties.PropertiesInjectors;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

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

    public static class ExtractorExamples {
        public static void main(String[] args) {
            example0();
//            example1();
//            example2();
//            example3();
//            example4();
//            example5();
//            example6();
//            example7();
        }

        private static void example0() {
            final Config before = new Config(
                    LocalDate.of(2021, 04, 19),
                    OptionalInt.of(24),
                    "DEV"
            );

            final Properties props = new Properties();

            props.setProperty("endDate", before.endDate().toString());
            before.numThreads().ifPresent(n -> props.setProperty("numThreads", Integer.toString(n)));
            props.setProperty("env", before.env());

            final String numThreads = props.getProperty("numThreads");
            final Config after = new Config(
                    LocalDate.parse(props.getProperty("endDate")),
                    numThreads == null ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(numThreads)),
                    props.getProperty("env")
            );

            assert (before.equals(after));

            System.out.println("before=" + before);
            System.out.println("after=" + after);
        }

        private static void example1() {
            final Config before = new Config(
                    LocalDate.of(2021, 04, 19),
                    OptionalInt.of(24),
                    "DEV"
            );

            final Properties props = new Properties();

            props.setProperty("endDate", before.endDate().toString());
            before.numThreads().ifPresent(n -> props.setProperty("numThreads", Integer.toString(n)));
            props.setProperty("env", before.env());

            INJR.inject(props, before);

            final Config after = EXTR.extract(props);

            assert (before.equals(after));

            System.out.println("before=" + before);
            System.out.println("after=" + after);
        }

        private static void example2() {

            final Extractor<Optional<String>, String> optGet = Optional::get;

            final Optional<String> optStr = Optional.of("test");

            final String s = optGet.extract(optStr);
            assert (s.equals("test"));
            System.out.println("s=" + s);

            final Extractor<Optional<String>, Integer> optLen = optGet.map(String::length);
            final int len = optLen.extract(optStr);
            assert (len == 4);
            System.out.println("len=" + len);
        }

        private static void example3() {
            final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;

            {
                final String javaVer = getPropVal.extract(System.getProperties(), "java.version");
                System.out.println(javaVer);
            }

            {
                final Extractor<Properties, String> getJavaVer = getPropVal.bind("java.version");
                final String javaVer = getJavaVer.extract(System.getProperties());
                System.out.println(javaVer);
            }
        }

        private static void example4() {
            final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
            final Extractor<Properties, LocalDate> getJavaVerDate =
                    getPropVal.bind("java.version.date")
                            .map(s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));
            final LocalDate javaVerDate = getJavaVerDate.extract(System.getProperties());
            System.out.println(javaVerDate);
        }

        private static void example5() {
            final Properties props = new Properties();
            props.put("keyA", "valueA");
            props.put("kayB", "valueB");
            props.put("whichKey", "keyA");

            final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
            final Extractor<Properties, String> getKey = getPropVal.bind("whichKey");

            final Extractor<Properties, String> getKeyVal = getKey.flatMap(key -> getPropVal.bind(key));

            final String value = getKeyVal.extract(props);
            System.out.println(value);
        }

        private static void example6() {
            final ExtractorByIndex<String, Character> getChar = String::charAt;
            final Extractor<Integer, Character> getFirstHexChar = getChar.bind(0).mapContext(Integer::toHexString);
            final char c = getFirstHexChar.extract(0xfebca987);
            System.out.println("c=" + c);
        }

        private static void example7() {
            final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
            final ExtractorByName<Properties, Optional<String>> getPropOptVal = getPropVal.optional();

            final Optional<String> empty = getPropOptVal.extract(System.getProperties(), "no_such_key");
            System.out.println("empty=" + empty);
            assert (!empty.isPresent());


            final Optional<String> notEmpty = getPropOptVal.extract(System.getProperties(), "java.home");
            System.out.println("notEmpty=" + notEmpty);
            assert (notEmpty.isPresent());
        }
    }

    public static class InjectorExamples {
        public static void main(String[] args) {
            example1();
            example2();
        }

        private static void example1() {
            final Injector<Optional<String>, String> setOptVal = (os, s) -> Optional.ofNullable(s);

            final Optional<String> os = setOptVal.inject(Optional.empty(), "test");

            assert(os.get().equals("test"));
        }

        private static void example2() {
            final Injector<AtomicInteger, Integer> setAtomVal = Injector.ofSideEffect(AtomicInteger::set);

            final AtomicInteger ai = new AtomicInteger(0);
            setAtomVal.inject(ai, 100);

            assert(ai.get() == 100);
        }

        private static void example3() {
            final Injector<Optional<String>, String> optSet = (os, s) -> Optional.ofNullable(s);
        }
    }
}
