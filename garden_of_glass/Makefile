VERSION=14

.PHONY: jar
jar: build
	@cd build && jar -c -f ../gardenofglass-${VERSION}.jar *

.PHONY: build
build:
	@mkdir -p build
	@cp -r src/main/resources/. build/
	@cp -r src/generated/resources/data/. build/data/
	@sed -i 's/@VERSION@/${VERSION}/' build/fabric.mod.json build/META-INF/neoforge.mods.toml

.PHONY: clean
clean:
	@rm -rf build gardenofglass-*.jar
