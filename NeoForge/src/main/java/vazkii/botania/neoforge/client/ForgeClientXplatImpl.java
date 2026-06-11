/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */

package vazkii.botania.neoforge.client;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.network.PacketDistributor;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.neoforge.client.integration.embeddium.EmbeddiumHelper;
import vazkii.botania.neoforge.client.integration.sodium.SodiumNeoforgeHelper;
import vazkii.botania.xplat.ClientXplatAbstractions;
import vazkii.botania.xplat.XplatAbstractions;

public class ForgeClientXplatImpl implements ClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		NeoForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, name, tickDelta, ms, buffers, light, overlay));
	}

	@Override
	public void sendToServer(CustomPacketPayload packet) {
		PacketDistributor.sendToServer(packet);
	}

	@Nullable
	@Override
	public BakedModel getResourceModel(ResourceLocation id) {
		BakedModel model = Minecraft.getInstance().getModelManager().getModel(ModelResourceLocation.standalone(id));
		return model != Minecraft.getInstance().getModelManager().getMissingModel() ? model : null;
	}

	@Override
	public BakedModel wrapPlatformModel(BakedModel original) {
		return new ForgePlatformModel(original);
	}

	@Override
	public int getGuiRightHeight(Player player) {
		return Minecraft.getInstance().gui.rightHeight;
	}

	@Override
	public void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap) {
		texture.setBlurMipmap(filter, mipmap);
	}

	@Override
	public void restoreLastFilter(AbstractTexture texture) {
		texture.restoreLastBlurMipmap();
	}

	@Override
	public void tessellateBlock(Level level, BlockState state, BlockPos pos, PoseStack ps, MultiBufferSource buffers, int overlay) {
		var renderer = Minecraft.getInstance().getBlockRenderer();
		var model = renderer.getBlockModel(state);
		var modelData = model.getModelData(level, pos, state, ModelData.EMPTY);
		var rand = RandomSource.create();
		for (RenderType type : model.getRenderTypes(state, rand, modelData)) {
			renderer.getModelRenderer().tesselateBlock(level, model,
					state, pos, ps, buffers.getBuffer(type), false, rand,
					state.getSeed(pos), overlay, modelData, type);
		}
	}

	@Override
	public void markSpriteActive(TextureAtlasSprite sprite) {
		if (sodiumLoaded.get()) {
			SodiumNeoforgeHelper.markSpriteActive(sprite);
		}
		if (embeddiumLoaded.get()) {
			EmbeddiumHelper.markSpriteActive(sprite);
		}
	}

	private final Supplier<Boolean> sodiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("sodium"));
	private final Supplier<Boolean> embeddiumLoaded = Suppliers.memoize(() -> XplatAbstractions.INSTANCE.isModLoaded("embeddium"));
}
