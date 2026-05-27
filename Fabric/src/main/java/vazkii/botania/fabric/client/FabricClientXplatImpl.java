package vazkii.botania.fabric.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.item.TinyPotatoRenderCallback;
import vazkii.botania.fabric.integration.sodium.SodiumFabricHelper;
import vazkii.botania.xplat.ClientXplatAbstractions;
import vazkii.botania.xplat.XplatAbstractions;

public class FabricClientXplatImpl implements ClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		TinyPotatoRenderCallback.EVENT.invoker().onRender(potato, name, tickDelta, ms, buffers, light, overlay);
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		ClientPlayNetworking.send(packet);
	}

	@Override
	public BakedModel getResourceModel(ResourceLocation id) {
		return Minecraft.getInstance().getModelManager().getModel(id);
	}

	@Override
	public BakedModel wrapPlatformModel(BakedModel original) {
		return new FabricPlatformModel(original);
	}

	@Override
	public void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap) {
		((ExtendedTexture) texture).setFilterSave(filter, mipmap);
	}

	@Override
	public void restoreLastFilter(AbstractTexture texture) {
		((ExtendedTexture) texture).restoreLastFilter();
	}

	@Override
	public void tessellateBlock(Level level, BlockState state, BlockPos pos, PoseStack ps, MultiBufferSource buffers, int overlay) {
		var brd = Minecraft.getInstance().getBlockRenderer();
		var buffer = buffers.getBuffer(ItemBlockRenderTypes.getRenderType(state, false));
		brd.getModelRenderer().tesselateBlock(level, brd.getBlockModel(state), state, pos, ps,
				buffer, true, RandomSource.create(), state.getSeed(pos), overlay);
	}

	@Override
	public void markSpriteActive(TextureAtlasSprite sprite) {
		if (sodiumLoaded.get()) {
			SodiumFabricHelper.markSpriteActive(sprite);
		}
	}

	private final Supplier<Boolean> sodiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("sodium"));
}
