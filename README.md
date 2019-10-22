Plexus Containers
============

[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.plexus/plexus-containers.svg?label=Maven%20Central)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22org.codehaus.plexus%22%20a%3A%plexus-containers%22)
[![Build Status](https://travis-ci.org/codehaus-plexus/plexus-containers.svg?branch=master)](https://travis-ci.org/codehaus-plexus/plexus-containers)

Plexus IoC Container core with companion tools.

## Attention

We have changed the default branch in GitHub repository from `plexus-containers-1.x` to `master`

## Release Notes

You can find details about the different releases in the [Release Notes](https://github.com/codehaus-plexus/plexus-containers/blob/master/ReleaseNotes.md).

 * [Release 2.1.0](https://github.com/codehaus-plexus/plexus-containers/blob/master/ReleaseNotes.md#plexus-containers-210).
 * [Release 2.0.0](https://github.com/codehaus-plexus/plexus-containers/blob/master/ReleaseNotes.md#plexus-containers-200).

## Site publishing

For publishing [the site](https://codehaus-plexus.github.io/plexus-containers/) do the following:

```
mvn -Preporting verify site site:stage
mvn scm-publish:publish-scm
```
