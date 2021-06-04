[<img src="https://img.shields.io/maven-central/v/org.typemeta/context.svg"/>](https://search.maven.org/#search%7Cga%7C1%7Cfuncj)
[![Javadocs](https://www.javadoc.io/badge/org.typemeta/context.svg)](https://www.javadoc.io/doc/org.typemeta/funcj)
[![Build Status](https://travis-ci.org/typemeta/context.svg?branch=master)](https://travis-ci.org/typemeta/funcj)
[![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/typemeta/context.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/typemeta/funcj/context:java)

*Context* is a zero-dependency Java library that provides a combinator framework for building *extractors*,
that can read values from a context,
and *injectors*, that can write values into a context.

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

# Background

*Combinators* are an approach to organising libraries
by providing a set of primitive constructs,
along with a set of functions that can combine existing constructs to form new ones.
Context provides two sets of combinators, injector and extractors.

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
injects the value into the context, and returns the context.

Since it's a Single Abstract Method interface we can easily construct an injector:


WIP