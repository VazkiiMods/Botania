package vazkii.botania.forge.mixin.client;

import net.minecraft.world.item.Item;
import net.minecraftforge.client.IItemRenderProperties;

import org.spongepowered.asm.mixin.Mixin;

import vazkii.botania.common.item.block.ItemBlockWithSpecialRenderer;
import vazkii.botania.forge.client.ForgeBlockEntityItemRendererHelper;

import java.util.function.Consumer;

@Mixin(ItemBlockWithSpecialRenderer.class)
public abstract class ForgeMixinItemBlockWithSpecialRenderer extends Item {
	private ForgeMixinItemBlockWithSpecialRenderer(Properties props) {
		super(props);
	}

	@Override
	public void initializeClient(Consumer<IItemRenderProperties> consumer) {
		ForgeBlockEntityItemRendererHelper.initItem(consumer);
	}
}
