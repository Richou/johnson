# johnson


[![Build Status](https://travis-ci.org/Richou/johnson.svg?branch=master)](https://travis-ci.org/Richou/johnson)

Jackson based deserialisation with variable injection

Introduction
============================

This library allow to put some variables in json, after the deserialization performed by jackson, it will replace the variable by the value found in the object.

For example :

```json
{
  "title" : "Test",
  "author": "Johnson",
  "subtitle" : "${title} - Subtest"
}
```

The subtitle field will contains "Test - Subtest"

Usage 
============================

To perform the variable injection, I will suppose that you have the json string in the variable "json" 

```java

String json = "{...}"
// Instanciate the mapper
Mapper mapper = new Mapper();
// The Book class is the object representation of the json
Book book = mapper.readValue(json, Book.class);

```

That it's !
