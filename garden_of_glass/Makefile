VERSION=1.12

.PHONY: jar
jar: build
	@cd build && jar -c -f ../gardenofglass-${VERSION}.jar *

.PHONY: build
build:
	@mkdir -p build
	@cp -r assets data fabric.mod.json logo_gog.png META-INF pack.mcmeta build/
	@sed -i 's/@VERSION@/${VERSION}/' build/fabric.mod.json build/META-INF/mods.toml

.PHONY: clean
clean:
	@rm -rf build gardenofglass-*.jar
