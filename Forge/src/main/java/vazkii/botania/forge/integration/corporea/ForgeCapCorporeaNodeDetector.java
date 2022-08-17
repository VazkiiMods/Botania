package vazkii.botania.forge.integration.corporea;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.corporea.ICorporeaNode;
import vazkii.botania.api.corporea.ICorporeaNodeDetector;
import vazkii.botania.api.corporea.ICorporeaSpark;

public class ForgeCapCorporeaNodeDetector implements ICorporeaNodeDetector {
	@Nullable
	@Override
	public ICorporeaNode getNode(Level world, ICorporeaSpark spark) {
		IItemHandler inv = getInventory(world, spark.getAttachPos());
		if (inv != null) {
			return new ForgeCapCorporeaNode(world, spark.getAttachPos(), inv, spark);
		}
		return null;
	}

	@Nullable
	private static IItemHandler getInventory(Level level, BlockPos pos) {
		var be = level.getBlockEntity(pos);

		if (be == null) {
			return null;
		}

		LazyOptional<IItemHandler> ret = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, Direction.UP);
		if (!ret.isPresent()) {
			ret = be.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		}
		return ret.orElse(null);
	}
}
