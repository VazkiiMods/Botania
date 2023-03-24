package vazkii.botania.fabric.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.hash.HashCode;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import vazkii.botania.common.lib.LibMisc;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

/**
 * Patches HashCache to write its results using system-agnostic paths, and in sorted order,
 * so that the datagen cache files can be committed.
 * Similar to the patch Forge does
 */
@Mixin(targets = { "net.minecraft.data.HashCache$ProviderCache" })
public abstract class HashCacheFabricMixin {
	@Shadow
	public abstract String version();

	@Shadow
	public abstract ImmutableMap<Path, HashCode> data();

	@Inject(at = @At("HEAD"), method = "save", cancellable = true)
	private void hookWrite(Path path, Path outputPath, String header, CallbackInfo ci) throws IOException {
		if (!LibMisc.MOD_ID.equals(System.getProperty("fabric-api.datagen.modid"))) {
			return;
		}

		ci.cancel();
		// [VanillaCopy] Vanilla method but use system-agnostic paths, sort by file path
		// before writing, and don't write date as part of header.
		// Forge has a patch to not rewrite if the contents are the same, to reduce git churn,
		// here we just remove the date from the header to reduce churn.
		try (BufferedWriter writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8)) {
			writer.write("// ");
			writer.write(this.version());
			writer.write('\t');
			writer.write(header.substring(header.indexOf('\t') + 1));
			writer.newLine();
			var sorted = new TreeMap<String, String>();
			for (var e : this.data().entrySet()) {
				var relativePath = path.relativize(e.getKey()).toString().replace('\\', '/');
				sorted.put(relativePath, e.getValue().toString());
			}
			for (var e : sorted.entrySet()) {
				writer.write(e.getValue() + ' ' + e.getKey());
				writer.newLine();
			}
		}
	}
}
