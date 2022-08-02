package vazkii.botania.fabric.mixin;

import net.minecraft.data.HashCache;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.lib.LibMisc;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.TreeMap;

/**
 * Patches HashCache to write its results using system-agnostic paths, and in sorted order,
 * so that the datagen cache files can be committed.
 * Similar to the patch Forge does
 */
@Mixin(HashCache.class)
public abstract class FabricMixinHashCache {
	@Shadow
	protected abstract void removeStale() throws IOException;

	@Shadow
	@Final
	private Path cachePath;

	@Shadow
	@Final
	private static Logger LOGGER;

	@Shadow
	private int hits;

	@Shadow
	@Final
	private Map<Path, String> newCache;

	@Shadow
	@Final
	private Map<Path, String> oldCache;

	@Shadow
	@Final
	private Path path;

	@Inject(at = @At("HEAD"), method = "purgeStaleAndWrite", cancellable = true)
	private void hookWrite(CallbackInfo ci) throws IOException {
		if (!LibMisc.MOD_ID.equals(System.getProperty("fabric-api.datagen.modid"))) {
			return;
		}

		ci.cancel();
		// [VanillaCopy] Vanilla method but use system-agnostic paths, and sort by file path
		// before writing
		this.removeStale();
		try (Writer writer = Files.newBufferedWriter(this.cachePath)) {
			var sorted = new TreeMap<String, String>();
			for (var e : newCache.entrySet()) {
				var relativePath = this.path.relativize(e.getKey()).toString().replace('\\', '/');
				sorted.put(relativePath, e.getValue());
			}
			var lines = sorted.entrySet().stream().map(e -> e.getValue() + ' ' + e.getKey()).toList();
			IOUtils.writeLines(lines, System.lineSeparator(), writer);
		}

		LOGGER.debug("Caching: cache hits: {}, created: {} removed: {}", this.hits, this.newCache.size() - this.hits, this.oldCache.size());
	}
}
