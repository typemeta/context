package org.typemeta.context.arrow;

import org.apache.arrow.memory.*;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.reader.FieldReader;
import org.apache.arrow.vector.types.pojo.Field;
import org.junit.*;
import org.typemeta.context.extractors.*;
import org.typemeta.context.extractors.byindex.ExtractorByIndex;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import static java.util.stream.Collectors.toList;

public class FieldReaderExtractorTest {
    private static final int N = 10;

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

    }

    private interface VectorAdd<T, VT extends FieldVector> {
        void add(VT fv, int index, T value);
    }

    private static final class TestData<T, VT extends FieldVector> {
        final String name;
        final List<T> testValues;
        final ExtractorByIndex<List<FieldReader>, T> extractor;
        final ExtractorByIndex<List<FieldReader>, Optional<T>> optExtractor;
        final BiFunction<String, BufferAllocator, VT> vecCtor;
        final VectorAdd<T, VT> vecAdd;

        protected TestData(
                String name,
                List<T> testValues,
                ExtractorByIndex<List<FieldReader>, T> extractor,
                ExtractorByIndex<List<FieldReader>, Optional<T>> optExtractor,
                BiFunction<String, BufferAllocator, VT> vecCtor,
                VectorAdd<T, VT> vecAdd
        ) {
            this.name = name;
            this.testValues = testValues;
            this.extractor = extractor;
            this.optExtractor = optExtractor;
            this.vecCtor = vecCtor;
            this.vecAdd = vecAdd;
        }

        ExtractorByIndex<List<FieldReader>, T> extractor() {
            return extractor;
        }

        T getValue(int i) {
            return testValues.get(i % testValues.size());
        }

        VT createVector(BufferAllocator allocator) {
            return vecCtor.apply(name, allocator);
        }

        void addToVector(FieldVector vec, int i) {
            vecAdd.add((VT)vec, i, getValue(i));
        }
    }

    private static final TestData<Boolean, BitVector> BOOLEAN  = new TestData<>(
            "boolean",
            Arrays.asList(true, false),
            FieldReaderListExtractors.BOOLEAN,
            FieldReaderListExtractors.OPT_BOOLEAN,
            BitVector::new,
            (BitVector vec, int i, Boolean value) -> vec.setSafe(i, value ? 1 : 0)
    );

    private static final TestData<Byte, UInt1Vector> BYTE  = new TestData<>(
            "byte",
            Arrays.asList((byte)0, (byte)123, (byte)45),
            FieldReaderListExtractors.BYTE,
            FieldReaderListExtractors.OPT_BYTE,
            UInt1Vector::new,
            UInt1Vector::setSafe
    );

    private static final TestData<Character, UInt2Vector> CHAR  = new TestData<>(
            "char",
            Arrays.asList((char)0, 'a', '0', '\n'),
            FieldReaderListExtractors.CHAR,
            FieldReaderListExtractors.OPT_CHAR,
            UInt2Vector::new,
            UInt2Vector::setSafe
    );

    private static final TestData<Double, Float8Vector> DOUBLE  = new TestData<>(
            "double",
            Arrays.asList(0.0d, 1234.5678d, 1234e56, -1234.5678, -1234e56, 1234e-56, -1234e-56),
            FieldReaderListExtractors.DOUBLE,
            FieldReaderListExtractors.OPT_DOUBLE,
            Float8Vector::new,
            Float8Vector::setSafe
    );

    private static final TestData<Float, Float4Vector> FLOAT  = new TestData<>(
            "float",
            Arrays.asList(0.0f, 1234.5678f, (float) 1234e56, -1234.5678f, (float) -1234e56, (float) 1234e-56, (float) -1234e-56),
            FieldReaderListExtractors.FLOAT,
            FieldReaderListExtractors.OPT_FLOAT,
            Float4Vector::new,
            Float4Vector::setSafe
    );

    private static final TestData<Integer, UInt4Vector> INT  = new TestData<>(
            "int",
            Arrays.asList(0, 123, 123456, -123, -123456),
            FieldReaderListExtractors.INT,
            FieldReaderListExtractors.OPT_INT,
            UInt4Vector::new,
            UInt4Vector::setSafe
    );

    private static final TestData<Long, UInt8Vector> LONG  = new TestData<>(
            "long",
            Arrays.asList(0L, 123L, 123456L, -123L, -123456L),
            FieldReaderListExtractors.LONG,
            FieldReaderListExtractors.OPT_LONG,
            UInt8Vector::new,
            UInt8Vector::setSafe
    );

    private static final TestData<Short, UInt2Vector> SHORT  = new TestData<>(
            "short",
            Arrays.asList((short)0, (short)123, (short)123456, (short)-123, (short)-123456),
            FieldReaderListExtractors.SHORT,
            FieldReaderListExtractors.OPT_SHORT,
            UInt2Vector::new,
            UInt2Vector::setSafe
    );

    private static final TestData<String, VarCharVector> STRING  = new TestData<>(
            "byte",
            Arrays.asList("", " ", "a", " a ", "1234567890"),
            FieldReaderListExtractors.STRING,
            FieldReaderListExtractors.OPT_STRING,
            VarCharVector::new,
            (VarCharVector vec, int i, String value) -> vec.setSafe(i, value.getBytes(StandardCharsets.UTF_8))
    );

    private static final List<TestData<?, ?>> TEST_DATA = Arrays.asList(
            BOOLEAN,
            BYTE,
            CHAR,
            DOUBLE,
            FLOAT,
            INT,
            LONG,
            SHORT,
            STRING
    );

    private static final Extractor<List<FieldReader>, Composite> COMP_EXTRACTOR =
            Extractors.combine(
                    Composite::new,
                    BOOLEAN.extractor().bind(0),
                    BYTE.extractor().bind(1),
                    CHAR.extractor().bind(2),
                    DOUBLE.extractor().bind(3),
                    FLOAT.extractor().bind(4),
                    INT.extractor().bind(5),
                    LONG.extractor().bind(6),
                    SHORT.extractor().bind(7),
                    STRING.extractor().bind(8)
            );

    private static Composite createComposite(int i) {
        return new Composite(
                BOOLEAN.getValue(i),
                BYTE.getValue(i),
                CHAR.getValue(i),
                DOUBLE.getValue(i),
                FLOAT.getValue(i),
                INT.getValue(i),
                LONG.getValue(i),
                SHORT.getValue(i),
                STRING.getValue(i)
        );
    }

    @Test
    public void roundTripFlightData() {
        final BufferAllocator allocator = new RootAllocator();

        final List<FieldVector> vectors =
                TEST_DATA.stream().map(td -> {
                        final FieldVector fv = td.createVector(allocator);
                        fv.allocateNew();
                        for (int i = 0; i < N; ++i) {
                            td.addToVector(fv, i);
                        }
                        return fv;
                }).collect(toList());
        vectors.forEach(FieldVector::allocateNew);

        final List<Field> fields = vectors.stream().map(FieldVector::getField).collect(toList());
        final VectorSchemaRoot vectorSchemaRoot = new VectorSchemaRoot(fields, vectors);

        final List<FieldReader> frs = vectors.stream().map(FieldVector::getReader).collect(toList());

        for (int i = 0; i < vectorSchemaRoot.getRowCount(); ++i) {
            final int pos = i;
            frs.forEach(fr -> fr.setPosition(pos));

            final Composite expected = createComposite(i);
            final Composite actual = COMP_EXTRACTOR.extract(frs);

            Assert.assertEquals("" + i, expected, actual);
        }
    }
}
