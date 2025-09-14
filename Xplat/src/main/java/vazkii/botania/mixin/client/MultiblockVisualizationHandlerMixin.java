package vazkii.botania.mixin.client;

import net.minecraft.core.BlockPos;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import vazkii.patchouli.client.handler.MultiblockVisualizationHandler;

@Mixin(MultiblockVisualizationHandler.class)
public interface MultiblockVisualizationHandlerMixin {
	// There doesn't seem to be any other way to access the anchor position of the current Patchouli multiblock.
	@Accessor("pos")
	static BlockPos botania_getPos() {
		throw new IllegalStateException();
	}
}
