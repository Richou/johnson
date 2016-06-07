# johnson


[![Build Status](https://travis-ci.org/Richou/johnson.svg?branch=master)](https://travis-ci.org/Richou/johnson)

Jackson based deserialisation with variable injection/replacement

Introduction
============================

This library allow to put some variables in json, after the deserialization performed by jackson, it will replace the variable by the value found in the object.

__For example__ :

```json
{
  "title" : "Test",
  "author": "Johnson",
  "subtitle" : "${title} - Subtest"
}
```
__In java side__ : 

```java
foo.getSubtitle(); // Will returns "Test - Subtest"
```

__For example 2__ :

```json
{
  "url": {
    "prod": "http://some.prod.url/",
    "dev": "http://some.dev.url/"
  },
  "sites": {
    "images": "${url.prod}images",
    "thumbnails": "${url.dev}thumbs"
  }
}
```

__In Java side__ : 

```java
foo2.getSites().getImages(); // Will returns "http://some.prod.url/images"
foo2.getSites().getThumbnails(); // Will returns "http://some.dev.url/thumbs"
```

Usage 
============================

To perform the variable injection, let's suppose that you have the json string in the variable "json" 

```java

String json = "{...}"
// Instanciate the mapper
Mapper mapper = new Mapper();
// The Book class is the object representation of the json
Book book = mapper.readValue(json, Book.class);

```

Or if you don't need to perform the serialization stuff

```java
// Instanciate the mapper
Mapper mapper = new Mapper();
// Just perform the variable substitution in an object
Book injectedBook = mapper.injectValue(book);
```

That it's !

Warning
============================

This mapper is suitable for small, medium objects.
Do not use this for huge, deep object, it perform java reflection and performance is really low.
