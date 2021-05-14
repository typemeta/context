package org.typemeta.context.properties;

import org.junit.*;
import org.typemeta.context.extractors.byname.ExtractorByName;

import java.util.*;

public class PropertiesExtractorsTest {
    private static final class TestData<T> {
        final String key;
        final T value;
        final ExtractorByName<Properties, T> extractor;
        final ExtractorByName<Properties, Optional<T>> optExtractor;

        private TestData(
                String key,
                T value,
                ExtractorByName<Properties, T> extractor
        ) {
            this.key = key;
            this.value = value;
            this.extractor = extractor;
            this.optExtractor = extractor.optional();
        }

        public void set(Properties props) {
            props.put(key, Objects.toString(value));
        }
    }

    private static final TestData<Boolean> BOOLEAN = new TestData<>(
            "BOOLEAN",
            false,
            PropertiesExtractors.BOOLEAN
    );

    private static final TestData<Byte> BYTE = new TestData<>(
            "BYTE",
            (byte)123,
            PropertiesExtractors.BYTE
    );

    private static final TestData<Double> DOUBLE = new TestData<>(
            "DOUBLE",
            123.456d,
            PropertiesExtractors.DOUBLE
    );

    private static final TestData<Float> FLOAT = new TestData<>(
            "FLOAT",
            456.789f,
            PropertiesExtractors.FLOAT
    );

    private static final TestData<Integer> INT = new TestData<>(
            "INT",
            123456,
            PropertiesExtractors.INT
    );

    private static final TestData<Long> LONG = new TestData<>(
            "LONG",
            123456l,
            PropertiesExtractors.LONG
    );

    private static final TestData<String> STRING = new TestData<>(
            "STRING",
            "1234abcd",
            PropertiesExtractors.STRING
    );

    private static final List<TestData<?>> testDataList = Arrays.asList(
            BOOLEAN, BYTE, DOUBLE, FLOAT, INT, LONG, STRING
    );

    private static final Properties PROPS = new Properties();

    static {
        testDataList.forEach(td -> td.set(PROPS));
    }

    @Test
    public void testExtractors() {
        testDataList.forEach(td -> {
            Assert.assertEquals(td.key, td.value, td.extractor.extract(PROPS, td.key));
        });
    }

    @Test
    public void testExtractors2() {
        testDataList.forEach(td -> {
            Assert.assertNull(td.key, td.extractor.extract(PROPS, "NO_SUCH_KEY"));
        });
    }

    @Test
    public void testOptExtractors() {
        testDataList.forEach(td -> {
            Assert.assertEquals(td.key, Optional.of(td.value), td.optExtractor.extract(PROPS, td.key));
        });
    }

    @Test
    public void testOptExtractors2() {
        testDataList.forEach(td -> {
            Assert.assertEquals(td.key, Optional.empty(), td.optExtractor.extract(PROPS, "NO_SUCH_KEY"));
        });
    }
}
