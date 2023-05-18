package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

import javax.annotation.Nullable;

public class StructureHelper {
	public static Structure getStructure(ServerLevel level, ResourceLocation key) {
		return level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).get(key);
	}

	public static boolean isInStructureBounds(ServerLevel level, BlockPos pos, @Nullable Structure structure) {
		return structure != null && level.structureManager().getStructureAt(pos, structure).isValid();
	}
}
