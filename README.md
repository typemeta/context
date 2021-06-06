[<img src="https://img.shields.io/maven-central/v/org.typemeta/context.svg"/>](https://search.maven.org/#search%7Cga%7C1%7Cfuncj)
[![Javadocs](https://www.javadoc.io/badge/org.typemeta/context.svg)](https://www.javadoc.io/doc/org.typemeta/funcj)
[![Build Status](https://travis-ci.org/typemeta/context.svg?branch=master)](https://travis-ci.org/typemeta/funcj)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/typemeta/context.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/typemeta/funcj/context:java)

*Context* is a zero-dependency Java library that provides a combinator framework for building *extractors*,
that can read values from a context,
and *injectors*, that can write values into a context.

A context is anything that acts as either a source of values (for extractors)
or as a target for values (for injectors).
Examples of contexts include Java `Properties` objects, JDBC `ResultSet` objects (for database extractors),
and JDBC `PreparedStatement` objects ( for database injectors).

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

we can define an injector, which will inject a `Config` object to a `Properties` object:

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
The simplest possible type of context is one that can only hold one value,
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
However, unlike `Optional`, in order to be able to extract a value from a `Properties` object,
a property key is required. Therefore we need a slightly different type of extractor:

```java
@FunctionalInterface
public interface ExtractorByName<CTX, T> {
    T extract(CTX ctx, String name);

    default Extractor<CTX, T> bind(String name) {
        return ctx -> extract(ctx, name);
    }
    
    // ...
}
```


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