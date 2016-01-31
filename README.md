# johnson


[![Build Status](https://travis-ci.org/Richou/johnson.svg?branch=master)](https://travis-ci.org/Richou/johnson)

Jackson based deserialisation with variable injection

Introduction
============================

This library allow to put some variables in json, after the deserialization performed by jackson, it will replace the variable by the value found in the object.

For example :

```
{
  "title" : "Test",
  "author": "Johnson",
  "subtitle" : "${title} - Subtest"
}
```

The subtitle field will contains "Test - Subtest"
