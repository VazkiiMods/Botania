package vazkii.botania.common.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.structure.Structure;

public class StructureHelper {
    public static Structure getStructure(ServerLevel level, ResourceKey<Structure> key) {
        return level.registryAccess().registryOrThrow(Registry.STRUCTURE_REGISTRY).get(key);
    }

    public static boolean isInStructureBounds(ServerLevel level, BlockPos pos, Structure structure) {
        return structure == null ? false : level.structureManager().getStructureAt(pos, structure).isValid();
    }
}
