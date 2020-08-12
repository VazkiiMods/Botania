package vazkii.botania.common.components;

import dev.onyxstudios.cca.api.v3.block.BlockComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.block.BlockComponentInitializer;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import vazkii.botania.api.item.IExoflameHeatable;
import vazkii.botania.common.core.handler.ExoflameFurnaceHandler;

public class BlockEntityComponents implements BlockComponentInitializer {
	public static final ComponentKey<IExoflameHeatable> EXOFLAME_HEATABLE = ComponentRegistryV3.INSTANCE.getOrCreate(IExoflameHeatable.ID, IExoflameHeatable.class);

	@Override
	public void registerBlockComponentFactories(BlockComponentFactoryRegistry registry) {
		registry.registerFor(AbstractFurnaceBlockEntity.class, EXOFLAME_HEATABLE, ExoflameFurnaceHandler.FurnaceExoflameHeatable::new);
	}
}
