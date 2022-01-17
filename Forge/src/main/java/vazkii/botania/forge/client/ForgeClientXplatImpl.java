package vazkii.botania.forge.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;

import vazkii.botania.api.BotaniaForgeClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.item.TinyPotatoRenderEvent;
import vazkii.botania.forge.network.ForgePacketHandler;
import vazkii.botania.network.IPacket;
import vazkii.botania.xplat.IClientXplatAbstractions;

import javax.annotation.Nullable;

public class ForgeClientXplatImpl implements IClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		MinecraftForge.EVENT_BUS.post(new TinyPotatoRenderEvent(potato, name, tickDelta, ms, buffers, light, overlay));
	}

	@Override
	public void sendToServer(IPacket packet) {
		ForgePacketHandler.CHANNEL.sendToServer(packet);
	}

	@Nullable
	@Override
	public IWandHUD findWandHud(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		// todo 1.18-forge lookaside registry for non-BE's
		return be.getCapability(BotaniaForgeClientCapabilities.WAND_HUD).orElse(null);
	}

	@Override
	public BakedModel wrapPlatformModel(BakedModel original) {
		return new ForgePlatformModel(original);
	}

	@Override
	public void setFilterSave(AbstractTexture texture, boolean filter, boolean mipmap) {
		texture.setBlurMipmap(filter, mipmap);
	}

	@Override
	public void restoreLastFilter(AbstractTexture texture) {
		texture.restoreLastBlurMipmap();
	}
}
