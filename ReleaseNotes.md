Plexus Containers Release Notes
=========================

Plexus Containers 2.2.0
---------------------
Plexus Containers 2.2.0 requires Java 8 and Maven 3.5.4+

### Dependency updates
* Bump asm from 9.2 to 9.5
* Bump plexus-classworlds from 2.6.0 to 2.7.0
* Bump plexus-utils from 3.1.1 to 3.5.1
* Bump xbean-reflect from 3.7 to 4.22
* Update QDox to 2.0.3
* plexus-component metadata - remove dependency to plexus-container-default

### Maintenance
* Add gh actions
* Bump checkstyle from 9.2 to 9.3
* Bump maven-checkstyle-plugin from 3.1.2 to 3.2.2
* Bump maven-enforcer-plugin from 3.0.0 to 3.3.0
* Bump maven-invoker-plugin from 3.2.2 to 3.5.1
* Bump maven-plugin-annotations from 3.6.2 to 3.8.2
* Bump maven-plugin-plugin from 3.6.2 to 3.8.2
* Bump maven-project-info-reports-plugin from 3.1.2 to 3.4.3
* Bump maven-release-plugin from 2.5.3 to 3.0.0
* Bump maven-shared-resources from 4 to 5
* Bump modello-maven-plugin from 1.1 to 2.1.1
* Bump plexus parent from 6.5 to 10
* Add deprecation information to Plexus components

Plexus Containers 2.1.1
---------------------
Plexus Containers 2.1.1 requires Java 7 and Maven 3.2.5+

### Improvements

* Upgrade ASM to 9.2
* Upgrade JDOM2 to 2.0.6.1

Plexus Containers 2.1.0
---------------------

### Improvements
 
 * [Issue #27][issue-27] - Sort manually crafted descriptors files for Reproducible Builds

Plexus Containers 2.0.0
---------------------

Plexus Containers 2.0.0 requires Java 6 and Maven 3.0+

### Improvements
 
 * [Issue #23][issue-23] - Remove the PlexusMetadataGeneratorCLI
 * [Issue #19][issue-19] - Removed the plexus-component-javadoc module
 * [Pull Request #6][pr-6] - compatibility with latest versions of Qdox.
 * [Pull Request #7][pr-7] - Reproducible metadata ([Issue #8][issue-8]).

### Upgrades

 * [Issue #15][issue-15] - Migrate to Maven 3.0+ api
 * [Issue #20][issue-20] - Upgrade qdox to 2.0-M10
 * [Issue #21][issue-21] - Upgrade plexus-utils 3.1.1
 * [Issue #22][issue-22] - Upgrade classworlds 2.6.0

### Tasks

 * [Issue #13][issue-13] - Move default development back to master.

[issue-8]: https://github.com/codehaus-plexus/plexus-containers/issues/8
[issue-13]: https://github.com/codehaus-plexus/plexus-containers/issues/13
[issue-15]: https://github.com/codehaus-plexus/plexus-containers/issues/15
[issue-19]: https://github.com/codehaus-plexus/plexus-containers/issues/19
[issue-20]: https://github.com/codehaus-plexus/plexus-containers/issues/20
[issue-21]: https://github.com/codehaus-plexus/plexus-containers/issues/21
[issue-22]: https://github.com/codehaus-plexus/plexus-containers/issues/22
[issue-23]: https://github.com/codehaus-plexus/plexus-containers/issues/23
[issue-27]: https://github.com/codehaus-plexus/plexus-containers/issues/27

[pr-7]: https://github.com/codehaus-plexus/plexus-containers/pull/7
[pr-6]: https://github.com/codehaus-plexus/plexus-containers/pull/6
