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

The library provides implementations of extractors and injectors for the above context types,
but can can also support any type of context.

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

Consider a simple class consisting of a few member fields:

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

If we want to write a `Config` object to the properties,
we would have to write each field individually:

```java
props.setProperty("endDate", before.endDate().toString());
before.numThreads().ifPresent(n -> props.setProperty("numThreads", Integer.toString(n)));
props.setProperty("env", before.env());
```

Likewise, to read the config back out we have to read the individual fields
and then construct the `Config` object:

```java
final String numThreads = props.getProperty("numThreads");
final Config after = new Config(
        LocalDate.parse(props.getProperty("endDate")),
        numThreads == null ? OptionalInt.empty() : OptionalInt.of(Integer.parseInt(numThreads)),
        props.getProperty("env")
);
```

Using context, we can define an injector, which will inject or write a `Config` object into a `Properties` object:

```java
final Injector<Properties, Config> setConfigProp =
        Injectors.combine(
            PropertiesInjectors.LOCALDATE.bind("endDate").premap(Config::endDate),
            PropertiesInjectors.OPT_INTEGER.bind("numThreads").premap(Config::numThreads),
            PropertiesInjectors.STRING.bind("env").premap(Config::env)
        );
```

and an extractor, which will extract a `Config` object from a `Properties` object:

```java
final Extractor<Properties, Config> getConfigProp =
        Extractors.combine(
                PropertiesExtractors.LOCALDATE.bind("endDate"),
                PropertiesExtractors.OPT_INTEGER.bind("numThreads"),
                PropertiesExtractors.STRING.bind("env"),
                Config::new
        );
```

and use them like this:

```java
// Write the config data to the props.
setConfigProp.inject(props, config);

// Read the config back out.
final Config config2 = getConfigProp.extract(props);
```

# User Guide

*Combinators* are an approach to organising libraries
by providing a set of primitive constructs,
along with a set of functions that can combine existing constructs to form new ones.
Context provides two sets of combinators - extractors and injectors.

## Extractors

Extractors extract a value from a context.
The primary interface is `Extractor`, which has one abstract method:

```java
@FunctionalInterface
public interface Extractor<CTX, T> {
    
    T extract(CTX ctx);
    
    // ...
}
```

I.e. an extractor is a function that takes a context,
extracts a value and returns the extracted value.
Contexts can be any type that supports the retrieval of values.
Taking the Java `Optional` type as an example context,
we can define an extractor for Optional:

```java
Extractor<Optional<String>, String> optGet = Optional::get;
```

and use it to extract a value from an optional value:

```java
final Optional<String> optStr = Optional.of("test");
final String s = optGet.extract(optStr);
assert(s.equals("test"));
```

We can convert this extractor into one for a different type by mapping a function over it:

```java
final Extractor<Optional<String>, Integer> optLen = optGet.map(String::length);
final int len = optLen.extract(optStr);
assert(len == 4);
```

### ExtractorByName

Extractors can be built for more interesting types of context,
such as the Java `Properties` class.
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
but where the `extract` method expects an integer index instead of a name.

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
However, the `ResultSet` get methods (e.g. `ResultSet.getBoolean`) all throw a `SQLException` in their signature.
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

These interfaces can then be used to construct extractors from methods that throw an exception:

```java
final ExtractorByName.Checked<ResultSet, Boolean, SQLException> BOOLEAN = ResultSet::getBoolean;
```

We can also convert a `Checked` extractor instance to an unchecked one by calling `Checked.unchecked`:

```java
final ExtractorByName<ResultSet, Boolean> BOOLEAN2 = BOOLEAN.unchecked();
```

### Specialisations

The generic type parameter `T` in the extractor interfaces specifies the type of the extracted value.
Currently Java generic types do not support primitive types (byte, int, etc) directly,
which means their boxed equivalents (Byte, Integer, ...) must be used.
This introduces the possibility of null values, as well as a slight performance overhead.
If the value being extracted can never be null then there exists specialised equivalents of the extractor interfaces,
which can be used instead:

| Base Type | Double Specialisation | Integer Specialisation | Long Specialisation |
|---|---|---|---|
| `Extractor` | `DoubleExtractor` | `IntegerExtractor` | `LongExtractor` |
| `ExtractorByName` | `DoubleExtractorByName` | `IntegerExtractorByName` | `LongExtractorByName` |
| `ExtractorByIndex` | `DoubleExtractorByIndex` | `IntegerExtractorByIndex` | `LongExtractorByIndex` |

Each specialised class provides an alternative extract method
that supports extracting the primitive type:

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
final Extractor<Optional<String>, String> optGet = Optional::get;
```

Each extractor type also has a static `of` constructor method (e.g. `Extractor.of`)
that can be used where a lambda or method reference can't be used directly:

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
final Extractor<Properties, Properties> getProps = Extractor.id();

// konst always returns the given value (and ignores the context).
final Extractor<Properties, String> alwaysRed = Extractor.konst("Red");
```

### Combinators

The library also provides some methods that can be used to construct new extractors
from existing ones.

#### `map`

The extractor `map` method creates an extractor that
applies a function to result of the given extractor:

```java
final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
final Extractor<Properties, LocalDate> getJavaVerDate =
        getPropVal.bind("java.version.date")
                .map(s -> LocalDate.parse(s, DateTimeFormatter.ISO_LOCAL_DATE));

final LocalDate javaVerDate = getJavaVerDate.extract(System.getProperties());
System.out.println(javaVerDate);
```

#### `flatMap`

The `flatMap` method creates a new extractor by chaining two extractors together:

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

#### `mapContext`

The `mapContext` method creates an extractor that applies a function to the context,
before applying the given extractor:

```java
final ExtractorByIndex<String, Character> getChar = String::charAt;

// Convert getChar into an extractor that operates on integer values.
final Extractor<Integer, Character> getFirstHexChar =
        getChar.bind(0).mapContext(Integer::toHexString);

final char c = getFirstHexChar.extract(0xfebca987);

// Prints "f".
System.out.println("c=" + c);
```

#### `optional`

The `optional` method converts an extractor into one that extracts optional values.
By default the optional extractor calls the original extractor to get a value,
and then passes it to `Optional.ofNullable` to covert it to an optional value 
(note some extractors override this behaviour).

```java
final ExtractorByName<Properties, String> getPropVal = Properties::getProperty;
final ExtractorByName<Properties, Optional<String>> getPropOptVal = getPropVal.optional();

final Optional<String> empty = getPropOptVal.extract(System.getProperties(), "no_such_key");
assert(!empty.isPresent());

final Optional<String> notEmpty = getPropOptVal.extract(System.getProperties(), "java.home");
assert(notEmpty.isPresent());
```

#### `combine`

`combine` is a standalone method that can be used to combine one more extractors
into one that extracts an object.
The extractors being combined correspond to the fields that comprise the object.

The `Config` example earlier in this guide illustrates the usage:

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

In the above example, the three extractors being combined correspond
to the three fields that comprise the `Config` class.
So `combine` creates an extractor for `Config`,
by calling each field extractor to extract the field values,
and then calling the given constructor for `Config`.

### Reader Monad

As a side note, the `Extractor` type is in fact the ubiquitous
[Reader Monad](http://learnyouahaskell.com/for-a-few-monads-more#reader),
also known as the function monad.

The `konst` and `flatMap` methods correspond to the monadic `pure` (aka `return`) and `bind` respectively.

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
The injector can either modify the context as a side effect and return the updated context,
or it can return a new context value.

```java
// Example 1: Injector returns a new context.

final Injector<Optional<String>, String> setOptVal = (os, s) -> Optional.ofNullable(s);

final Optional<String> os = setOptVal.inject(Optional.empty(), "test");

assert(os.get().equals("test"));

// Example 2: Injector modifies context value in place.

final Injector<AtomicInteger, Integer> setAtomVal = Injector.of(AtomicInteger::set);

final AtomicInteger ai = new AtomicInteger(0);
setAtomVal.inject(ai, 100);

assert(ai.get() == 100);
```

In common with Extractors, Injectors provide the following functionality,
each of which operates in a similar fashion to its extractor counterpart:

* **InjectorByName** - injectors that take a string name argument.
* **InjectorByIndex** - injectors that take an integer index argument.
* **Checked Injectors** - injectors that throw a checked exception.
* **Specialisations** - injectors that operate on primitive types.


### Constructors

There are various ways to construct an injector.
The first and most common is to construct one via a lambda or method reference:

```java
final Injector<Optional<String>, String> optSet = (os, s) -> Optional.ofNullable(s);
```

Each injector type also has a static `of` constructor method (e.g. `Injector.of`)
that can be used where a lambda or method reference can't be used directly.

### Combinators



#### `combine`

`combine` is a standalone method that can be used to combine one more injectors
into one that injects an object.
The extractors being combined correspond to the fields that comprise the object.

The `Config` example earlier in this guide illustrates the usage:

```java
```java
final Injector<Properties, Config> setConfigProp =
        Injectors.combine(
            PropertiesInjectors.LOCALDATE.bind("endDate").premap(Config::endDate),
            PropertiesInjectors.OPT_INTEGER.bind("numThreads").premap(Config::numThreads),
            PropertiesInjectors.STRING.bind("env").premap(Config::env)
        );

// Write the config data to the props.
setConfigProp.inject(props, config);
```

In the above example, the three injectors being combined correspond
to the three fields that comprise the `Config` class.
So `combine` creates an injector for `Config`,
by calling each field injector to inject the field values.

