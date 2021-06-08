[<img src="https://img.shields.io/maven-central/v/org.typemeta/context.svg"/>](https://search.maven.org/search?q=org.typemeta.context)
[![Javadocs](https://www.javadoc.io/badge/org.typemeta/context.svg)](https://www.javadoc.io/doc/org.typemeta/context)
[![Build Status](https://travis-ci.com/typemeta/context.svg)](https://travis-ci.com/typemeta/context)

*Context* is a zero-dependency Java library that provides a combinator framework for building *extractors*,
that can read values from a context,
and *injectors*, that can write values into a context.

A context is anything that acts as either a source of values (for extractors)
or as a target for values (for injectors).
Examples of contexts include Java `Properties` objects, JDBC `ResultSet` objects (for database extractors),
and JDBC `PreparedStatement` objects (for database injectors).

The library provides implemenetation of extractors and injectos for the above context types,
but can can support any type of context.

# Getting Started

## Requirements

Requires Java 1.8 or higher.

## Resources

* **Release builds** are available on the [Releases](https://github.com/typemeta/context/releases) page.
* **Maven artifacts** are available on the [Sonatype Nexus repository](https://repository.sonatype.org/#nexus-search;quick~context-core)
* **Javadocs** are for the latest build are on [javadocs.io](http://www.javadoc.io/doc/org.typemeta/context-core) page.

## Maven

Add this dependency to your project pom.xml:

```xml
<dependency>
    <groupId>org.typemeta</groupId>
    <artifactId>context-core</artifactId>
    <version>${context-core.version}</version>
</dependency>
```

(and define `context-core.version` accordingly)

## Example

Given a simple class consisting of a few member fields:

```java
static class Config {
    final LocalDate endDate;
    final OptionalInt numThreads;
    final String env;

    // Usual constructor, member retrieval methods, and toString.
}
```

along with a `Config` instance and a `Properties` instance:
        
```java
final Config config = new Config(
        LocalDate.of(2021, 04, 19),
        OptionalInt.of(24),
        "DEV"
);

final Properties props = new Properties();
```

we can define an injector, which will inject a `Config` object into a `Properties` object:

```java
final Injector<Properties, Config> INJR =
        Injectors.combine(
            PropertiesInjectors.LOCALDATE.bind("endDate").premap(Config::endDate),
            PropertiesInjectors.OPT_INTEGER.bind("numThreads").premap(Config::numThreads),
            PropertiesInjectors.STRING.bind("env").premap(Config::env)
        );

INJR.inject(props, config);
```

and an extractor, which will extract a `Config` object from a `Properties` object:

```java
final Extractor<Properties, Config> EXTR =
        Extractors.combine(
                PropertiesExtractors.LOCALDATE.bind("endDate"),
                PropertiesExtractors.OPT_INTEGER.bind("numThreads"),
                PropertiesExtractors.STRING.bind("env"),
                Config::new
        );

final Config config2 = EXTR.extract(props);
assert(config2.equals(config));
```

# User Guide

*Combinators* are an approach to organising libraries
by providing a set of primitive constructs,
along with a set of functions that can combine existing constructs to form new ones.
Context provides two sets of combinators, extractors and injectors.

## Extractors

Extractors extract a value from a context.
The primary interface is `Extractor`, which essentially looks as follows:

```java
@FunctionalInterface
public interface Extractor<CTX, T> {
    
    T extract(CTX ctx);
    
    // ...
}
```

I.e. an extractor is a function that takes a context,
extracts a value and returns it.
Contexts can be any type that supports the retrieval of values.
By way of example, the simplest possible type of context is one that can hold at most one value,
namely the Java `Optional` type.
We can define an extractor for Optional:

```java
Extractor<Optional<String>, String> optGet = Optional::get;
```

and use it to extract a value from an optional value:

```java
final Optional<String> optStr = Optional.of("test");
final String s = optGet.extract(optStr);
assert(s.equals("test"));
```

We can convert this extractor into one for a different type by mapping a function over the extractor:

```java
final Extractor<Optional<String>, Integer> optLen = optGet.map(String::length);
final int len = optLen.extract(optStr);
assert(len == 4);
```

### ExtractorByName

Extractors can be built for more interesting types of context,
such as the Java `Properties` type.
However, unlike `Optional`,
in order to be able to extract a value from a `Properties` object,
a property key is required.
Therefore we need a slightly different type of extractor,
that adds an extra string parameter to the `extract` method:

```java
@FunctionalInterface
public interface ExtractorByName<CTX, T> {
    
    T extract(CTX ctx, String name);
    
    // ...
}
```

We can construct an instance of this type of extractor as before:

```java
final ExtractorByName<Properties, String> getProp = Properties::getProperty;
```

and can use it by calling the extract method with a `Properties` object and a key name:

```java
final String javaVer = getPropVal.extract(System.getProperties(), "java.version");
```

Alternatively, we can bind this `ExtractorByName` to a name,
which then gives us a standard `Extractor`:

```java
final Extractor<Properties, String> getJavaVer = getPropVal.bind("java.version");
final String javaVer = getJavaVer.extract(System.getProperties());
System.out.println(javaVer);
```

### ExtractorByIndex

An `ExtractorByIndex` is similar to `ExtractorByName`,
where instead the `extract` method expects an integer index instead of a name.

```java
@FunctionalInterface
public interface ExtractorByIndex<CTX, T> {
    
    T extract(CTX ctx, int index);
    
    // ...
}
```

As before, an `ExtractorByIndex` can be bound to an integer value, to create a standard extractor.

### Checked Extractors

At first glance, the JDBC `ResultSet` class seems like a suitable candidate for converting into an extractor.
However the `ResultSet` get methods (e.g. `ResultSet.getBoolean`) all throw a `SQLException` in their signature.
This prevents us from creating an extractor directly:

```java
// Compile error.
final ExtractorByName<ResultSet, Boolean> BOOLEAN = ResultSet::getBoolean;
```

To address this, each extractor interface contains an inner interface named `Checked`, e.g. `Extractor.Checked`.
Each `Checked` interface is similar to its outer interface, with one difference - the extract method throws an exception.
The exact type of exception is specified as a type argument to the interface:

```java
@FunctionalInterface
interface Checked<CTX, T, EX extends Exception> {
    
    T extract(CTX ctx) throws EX;
    
    // ...
}
```

These interfaces can then be used to construct extractors from methods that throw an exception, e.g.:

```java
final ExtractorByName.Checked<ResultSet, Boolean, SQLException> BOOLEAN = ResultSet::getBoolean;
```

We can also convert a `Checked` extractor instance to an unchecked one by calling `Checked.unchecked`:

```java
final ExtractorByName<ResultSet, Boolean> BOOLEAN2 = BOOLEAN.unchecked();
```

### Specialiations

The generic type parameter `T` in the extractor interfaces specifies the type of the extracted value.
Currently generic types do not support primitive types (byte, int etc) directly,
which means their boxed equivalents (Byte, Integer, ...) must be used.
This introduces the possibility of null values, as well as a slight performance overhead.
If the value being extracted can never be null then there exists specialised equivalents of the extractor interfaces,
which can be used instead:

| Base Type | Double Specialisation | Integer Specialisation | Long Specialisation |
|---|---|---|---|
| `Extractor` | `DoubleExtractor` | `IntegerExtractor` | `LongExtractor` |
| `ExtractorByName` | `DoubleExtractorByName` | `IntegerExtractorByName` | `LongExtractorByName` |
| `ExtractorByIndex` | `DoubleExtractorByIndex` | `IntegerExtractorByIndex` | `LongExtractorByIndex` |

Each specialised class provides an alternative extract method that supports the primitive type:

```java
public interface DoubleExtractor<CTX> extends Extractor<CTX, Double> {

    double extractDouble(CTX ctx);

    @Override
    default Double extract(CTX ctx) {
        return extractDouble(ctx);
    }
    
    // ...
}
```

### Constructors

There are various ways to construct an extractor.
The first and most common is to construct one via a lambda or method reference:

```java
Extractor<Optional<String>, String> optGet = Optional::get;
```

Each extractor type also has a static `of` constructor method (e.g. `Extractor.of`)
that can be used where a lambda or method reference can't be used directly, e.g.:

```java
// Commpile error
final ExtractorByName<ResultSet, Boolean> BOOLEAN = ResultSet::getBoolean.unchecked();

// Ok.
final ExtractorByName<ResultSet, Boolean> BOOLEAN =
        ExtractorByName.of(ResultSet::getBoolean).unchecked();
```

Each extractor type also provides some basic extractors:

```java
// The id extractor always returns the context.
final Extractor<Properties, Properties> id = Extractor.id();

// konst always returns the given value (and ignores the context).
final Extractor<Properties, String> alwaysRed = Extractor.konst("Red");
```

### Combinators

The library provides a number of methods that can be used to construct new extractors
from existing ones.

The extractor `map` method creates an extractor that
applies a function to result of the given extractor.
E.g.:

```java
final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
final Extractor<Properties, LocalDate> getJavaVerDate =
        getPropVal.bind("java.version.date")
                .map(s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));

final LocalDate javaVerDate = getJavaVerDate.extract(System.getProperties());
System.out.println(javaVerDate);
```

The `flatMap` method creates a new extractor by chaining two extractors together, e.g.:

```java
// Build a simple properties map.
final Properties props = new Properties();
props.put("keyA", "valueA");
props.put("keyB", "valueB");
props.put("whichKey", "keyA");

// Define some extractors.
final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
final Extractor<Properties, String> getKey = getPropVal.bind("whichKey");

// Use flatMap to compose getKey and getPropVal.
final Extractor<Properties, String> getKeyVal = getKey.flatMap(key -> getPropVal.bind(key));

// Test it.
final String value = getKeyVal.extract(props);

// Outputs "valueA".
System.out.println(value);
```

The `mapContext` method creates an extractor that applies a function to the context,
before applying the given extractor, e.g.:

```java
final ExtractorByIndex<String, Character> getChar = String::charAt;

// Convert getChar into an extractor that operates on integer values.
final Extractor<Integer, Character> getFirstHexChar =
        getChar.bind(0).mapContext(Integer::toHexString);

final char c = getFirstHexChar.extract(987654);

// Prints "f".
System.out.println("c=" + c);
```

The `optional` method converts an extractor into one that extracts optional values.
If the extracted value is null then it's converted to an empty optional value.

```java
final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
final ExtractorByName<Properties, Optional<String>> getPropOptVal = getPropVal.optional();

final Optional<String> empty = getPropOptVal.extract(System.getProperties(), "no_such_key");
assert(!empty.isPresent());

final Optional<String> notEmpty = getPropOptVal.extract(System.getProperties(), "java.home");
assert(notEmpty.isPresent());
```
        
### Reader Monad

The `Extractor` type is an essentially the ubiquitous
[Reader Monad](http://learnyouahaskell.com/for-a-few-monads-more#reader),
also known as the function monad.

## Injectors

Injectors inject a value into a context.
The primary interface is `Injector`, which essentially looks as follows:

```java
@FunctionalInterface
public interface Injector<CTX, T> {
    
    CTX inject(CTX ctx, T value);
    
    // ...
}
```

I.e. an injector is a function that takes a context and a value,
injects the value into the context, and returns the updated context.
The simplest possible context is an Optional value.
Since it's a Single Abstract Method interface we can easily construct an injector:

```java
final Extractor<Optional<String>, String> optGet = Optional::get;
```

To use it we just pass an Optional value to the
WIP
