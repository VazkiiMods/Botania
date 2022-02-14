package vazkii.botania.forge.internal_caps;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

import vazkii.botania.common.block.tile.string.TileRedStringContainer;

import javax.annotation.Nonnull;

public class RedStringContainerCapProvider implements ICapabilityProvider {
	private static final LazyOptional<IItemHandler> EMPTY = LazyOptional.of(EmptyHandler::new);

	private final TileRedStringContainer container;

	public RedStringContainerCapProvider(TileRedStringContainer container) {
		this.container = container;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, Direction side) {
		if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			BlockEntity binding = container.getTileAtBinding();
			if (binding != null) {
				LazyOptional<?> optional = binding.getCapability(cap, side);
				if (optional.isPresent()) {
					return optional.cast();
				} else {
					return EMPTY.cast();
				}
			}
		}
		return LazyOptional.empty();
	}
}
