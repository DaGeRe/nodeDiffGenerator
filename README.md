nodeDiffDetector
===================

This project detects differences between two commits of a project at call tree node (=method) level. This is done in order to spot where potential performance changes might arise and be rooted. The main use case if the performance analysis using Peass (https://github.com/DaGeRe/peass).

The main methods provided to other projects are:
- `de.dagere.nodeDiffDetector.diffDetection.ChangeDetector.compareClazz`: Obtains the differences between two commits of a method and stores them to a `Map<MethodCall, ClazzChangeData>`
- `de.dagere.nodeDiffDetector.clazzFinding.TypeFileFinder.getTypes`: Obtains all types (classes, interfaces, enums, or types from other languages than Java) that are defined in a given folder set, including inner classes etc.
- `de.dagere.nodeDiffDetector.sourceReading.SourceReadUtils.getMethod`: Obtains the CallableDeclaration of a given `MethodCall`
- `de.dagere.nodeDiffDetector.sourceReading.SourceReadUtils.getNamedClasses`: Obtains all type declarations below a call tree node (might also be anonymous or inner types)
- ... to be completed

Currently, this is only provided for Java source code. In the future, it is planned to extend this functionality to other languages.

# License

nodeDiffDetector is **licensed** under the **[MIT License]** and **[AGPL License]**. This means you can use nodeDiffDetector under the conditions of one of these licenses, but future forks might choose to only continue using one of these licenses.

[MIT License]: https://github.com/DaGeRe/peass/blob/main/LICSENSE.MIT
[AGPL License]: https://github.com/DaGeRe/peass/blob/main/LICENSE.AGPL
