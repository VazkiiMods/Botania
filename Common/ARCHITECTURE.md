# Common
This is the `Common` submodule of Botania. It contains all Botania code that can be
shared between Forge and Fabric. The term "xplat" may also appear and means the same:
something that can be shared between mod loaders.

`Common` code may occasionally need to make loader-specific calls, however. The
`IXplatAbstractions` interface is present and implemented by each loader-specific
submodules for such calls as the need arises.

## Build
The build system for the Common submodule works in a somewhat special manner.
There's two scenarios:

### Part of loader-specific build
When the Forge or Fabric specific artifacts are being built, the `Common` module's sources
are substituted into the project, as if the contents of `Common/src` are pasted into `Forge/src`
or `Fabric/src`. From the perspective of players or modders consuming a specific loader's
artifacts, it should be indistinguishable from having two separate branches.

### Separate Common artifact
The `Common` module also has its own build.gradle, which uses `VanillaGradle` to set up
a barebones Mojmap-decompiled vanilla. This is mostly just to provide typecheck and IDE aid.
The `Common` module's code is also published independently to Maven. This is to aid in downstream
addon authors that may also want to make their addons cross-loader-compatible. They would
have their own `Common` module that depends on Botania's `Common` module. However,
the `Common` module JAR should **never appear on the classpath at runtime**. If a player
finds themselves with a common JAR, things have gone very, very wrong.
