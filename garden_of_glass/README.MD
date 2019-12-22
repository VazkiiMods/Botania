Garden of Glass
---

This subdirectory is a separate mod workspace for Garden of Glass, Botania's skyblock mode.

This mod is nothing more than a dressed-up datapack. It contains recipe and other overrides in its /data, but otherwise has no code.

The code side of GoG lives in Botania itself.

Yes, this *could* be a proper Gradle subproject, but it's too small to be worth the effort to figure that out.

So to test, simply `gradlew build` then copy it to your main Botania workspace's mods/ folder and run from there.

Note that since nothing touches vanilla code, there's no need for a deobf JAR.
