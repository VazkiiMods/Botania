package vazkii.botania.fabric.integration.corporea;

import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.corporea.CorporeaNode;
import vazkii.botania.api.corporea.CorporeaNodeDetector;
import vazkii.botania.api.corporea.CorporeaSpark;

public class FabricTransferCorporeaNodeDetector implements CorporeaNodeDetector {
	@Nullable
	@Override
	public CorporeaNode getNode(Level world, CorporeaSpark spark) {
		var inv = ItemStorage.SIDED.find(world, spark.getAttachPos(), Direction.UP);
		if (inv != null && inv.supportsExtraction()) {
			return new FabricTransferCorporeaNode(world, spark.getAttachPos(), inv, spark);
		}
		return null;
	}
}
