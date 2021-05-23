package org.typemeta.context.properties;

import org.junit.jupiter.api.Test;
import org.typemeta.context.extractors.*;
import org.typemeta.context.extractors.byname.ExtractorByName;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class PropertiesExtractorsTest {

    private static final class Composite {
        final boolean booleanField;
        final byte byteField;
        final char charField;
        final double doubleField;
        final float floatField;
        final int intField;
        final long longField;
        final short shortField;
        final String stringField;

        private Composite(
                boolean booleanField,
                byte byteField,
                char charField,
                double doubleField,
                float floatField,
                int intField,
                long longField,
                short shortField,
                String stringField
        ) {
            this.booleanField = booleanField;
            this.byteField = byteField;
            this.charField = charField;
            this.doubleField = doubleField;
            this.floatField = floatField;
            this.intField = intField;
            this.longField = longField;
            this.shortField = shortField;
            this.stringField = stringField;
        }

        private Composite(Object[] args) {
            this(
                    (Boolean)args[0],
                    (Byte)args[1],
                    (Character) args[2],
                    (Double)args[3],
                    (Float)args[4],
                    (Integer)args[5],
                    (Long)args[6],
                    (Short)args[7],
                    (String)args[8]
            );
        }

        public boolean booleanField() {
            return booleanField;
        }

        public byte byteField() {
            return byteField;
        }

        public char charField() {
            return charField;
        }

        public double doubleField() {
            return doubleField;
        }

        public float floatField() {
            return floatField;
        }

        public int intField() {
            return intField;
        }

        public long longField() {
            return longField;
        }

        public short shortField() {
            return shortField;
        }

        public String stringField() {
            return stringField;
        }
    }


    private static final class TestData<T> {
        final String key;
        final T value;
        final ExtractorByName<Properties, T> extractor;
        final ExtractorByName<Properties, Optional<T>> optExtractor;
        final Function<Composite, T> fieldGetter;

        private TestData(
                String key,
                T value,
                ExtractorByName<Properties, T> extractor,
                Function<Composite, T> fieldGetter
        ) {
            this.key = key;
            this.value = value;
            this.extractor = extractor;
            this.optExtractor = extractor.optional();
            this.fieldGetter = fieldGetter;
        }

        void set(Properties props) {
            props.put(key, Objects.toString(value));
        }

        Extractor<Properties, T> bindExtractor() {
            return extractor.bind(key);
        }

        Extractor<Properties, Optional<T>> bindOptExtractor() {
            return optExtractor.bind(key);
        }

        T getField(Composite comp) {
            return fieldGetter.apply(comp);
        }
    }

    private static final TestData<Boolean> BOOLEAN = new TestData<>(
            "BOOLEAN",
            false,
            PropertiesExtractors.BOOLEAN,
            Composite::booleanField
    );

    private static final TestData<Byte> BYTE = new TestData<>(
            "BYTE",
            (byte)123,
            PropertiesExtractors.BYTE,
            Composite::byteField
    );

    private static final TestData<Character> CHAR = new TestData<>(
            "CHAR",
            'x',
            PropertiesExtractors.CHAR,
            Composite::charField
    );

    private static final TestData<Double> DOUBLE = new TestData<>(
            "DOUBLE",
            123.456d,
            PropertiesExtractors.DOUBLE,
            Composite::doubleField
    );

    private static final TestData<Float> FLOAT = new TestData<>(
            "FLOAT",
            456.789f,
            PropertiesExtractors.FLOAT,
            Composite::floatField
    );

    private static final TestData<Integer> INT = new TestData<>(
            "INT",
            123456,
            PropertiesExtractors.INT,
            Composite::intField
    );

    private static final TestData<Long> LONG = new TestData<>(
            "LONG",
            123456l,
            PropertiesExtractors.LONG,
            Composite::longField
    );

    private static final TestData<Short> SHORT = new TestData<>(
            "SHORT",
            (short)123456,
            PropertiesExtractors.SHORT,
            Composite::shortField
    );

    private static final TestData<String> STRING = new TestData<>(
            "STRING",
            "1234abcd",
            PropertiesExtractors.STRING,
            Composite::stringField
    );

    private static final List<TestData<?>> testDataList = Arrays.asList(
            BOOLEAN, BYTE, CHAR, DOUBLE, FLOAT, INT, LONG, SHORT, STRING
    );

    private static final Extractor<Properties, Composite> COMP_EXTRACTOR =
            Extractors.combine(
                    Composite::new,
                    BOOLEAN.bindExtractor(),
                    BYTE.bindExtractor(),
                    CHAR.bindExtractor(),
                    DOUBLE.bindExtractor(),
                    FLOAT.bindExtractor(),
                    INT.bindExtractor(),
                    LONG.bindExtractor(),
                    SHORT.bindExtractor(),
                    STRING.bindExtractor()
            );

    private static final Extractor<Properties, Composite> COMP_OPT_EXTRACTOR =
            Extractors.combine(
                    Composite::new,
                    BOOLEAN.bindOptExtractor().map(o -> o.orElse(null)),
                    BYTE.bindOptExtractor().map(o -> o.orElse(null)),
                    CHAR.bindOptExtractor().map(o -> o.orElse(null)),
                    DOUBLE.bindOptExtractor().map(o -> o.orElse(null)),
                    FLOAT.bindOptExtractor().map(o -> o.orElse(null)),
                    INT.bindOptExtractor().map(o -> o.orElse(null)),
                    LONG.bindOptExtractor().map(o -> o.orElse(null)),
                    SHORT.bindOptExtractor().map(o -> o.orElse(null)),
                    STRING.bindOptExtractor().map(o -> o.orElse(null))
            );

    private static final Properties PROPS = new Properties();

    static {
        testDataList.forEach(td -> td.set(PROPS));
    }

    @Test
    public void testExtractors() {
        testDataList.forEach(td -> {
            assertEquals(td.value, td.extractor.extract(PROPS, td.key), td.key);
        });
    }

    @Test
    public void testExtractors2() {
        testDataList.forEach(td -> {
            assertNull(td.extractor.extract(PROPS, "NO_SUCH_KEY"), td.key);
        });
    }

    @Test
    public void testOptExtractors() {
        testDataList.forEach(td -> {
            assertEquals(Optional.of(td.value), td.optExtractor.extract(PROPS, td.key), td.key);
        });
    }

    @Test
    public void testOptExtractors2() {
        testDataList.forEach(td -> {
            assertEquals(Optional.empty(), td.optExtractor.extract(PROPS, "NO_SUCH_KEY"), td.key);
        });
    }

    @Test
    public void testCompositeExtractors() {
        final Composite comp = COMP_EXTRACTOR.extract(PROPS);
        testDataList.forEach(td -> {
            assertEquals(td.value, td.getField(comp), td.key);
        });
    }

    @Test
    public void testCompositeOptExtractors() {
        final Composite comp = COMP_OPT_EXTRACTOR.extract(PROPS);
        testDataList.forEach(td -> {
            assertEquals(td.value, td.getField(comp), td.key);
        });
    }
}
