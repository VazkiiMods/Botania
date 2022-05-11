# Xplat
This is the `Xplat` submodule of Botania. It contains all Botania code that can be
shared between Forge and Fabric. The term "xplat" may also appear and means the same:
something that can be shared between mod loaders.
"Xplat" stands for "cross platform". Other multi-loader literature may
call this the "Common" module, but that is easily conflated with the old
connotation of Common as "code shared between client and server".

`Xplat` code may occasionally need to make loader-specific calls, however. The
`IXplatAbstractions` interface is present and implemented by each loader-specific
submodules for such calls as the need arises.

## Build
The build system for the Xplat submodule works in a somewhat special manner.
There's two scenarios:

### Part of loader-specific build
When the Forge or Fabric specific artifacts are being built, the `Xplat` module's sources
are substituted into the project, as if the contents of `Xplat/src` are pasted into `Forge/src`
or `Fabric/src`. From the perspective of players or modders consuming a specific loader's
artifacts, it should be indistinguishable from having two separate branches.
Xplat code that ends up in the loader-specific artifact JARs are remapped to Intermediary/SRG
like any regular mod.

### Separate Xplat artifact
The `Xplat` module also has its own build.gradle, which uses `VanillaGradle` to set up
a barebones Mojmap-decompiled vanilla. This is mostly just to provide typecheck and IDE aid.
The `Xplat` module's code is also published independently to Maven in raw Mojmap without
any remapping. This is to aid in downstream addon authors that may also want to make their
addons cross-loader-compatible. They would have their own `Xplat` module that depends on
Botania's `Xplat` module. However, Botania's `Xplat` module JAR should
**never appear on the classpath at runtime**.
If a player finds themselves with a Botania-Xplat JAR, things have gone very, very wrong.
