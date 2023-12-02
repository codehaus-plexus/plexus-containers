Plexus Containers (deprecated)
============

[![Maven Central](https://img.shields.io/maven-central/v/org.codehaus.plexus/plexus-containers.svg?label=Maven%20Central)](https://search.maven.org/artifact/org.codehaus.plexus/plexus-containers)

Plexus IoC Container core with companion tools.

## Deprecated

Plexus IoC Container is replaced by [Eclipse Sisu](https://www.eclipse.org/sisu/): [sisu.inject](https://github.com/eclipse/sisu.inject), [sisu.plexus](https://github.com/eclipse/sisu.plexus), [sisu.mojos](https://github.com/eclipse/sisu.mojos/). See [Maven & JSR-330](https://maven.apache.org/maven-jsr330.html) for more details in Maven context.

## Attention

We have changed the default branch in GitHub repository from `plexus-containers-1.x` to `master`

## Release Notes

You can find details about the different releases in the [Release Notes](https://github.com/codehaus-plexus/plexus-containers/releases).

## Site publishing

For publishing [the site](https://codehaus-plexus.github.io/plexus-containers/) do the following:

```
mvn -Preporting verify site site:stage
mvn scm-publish:publish-scm
```
