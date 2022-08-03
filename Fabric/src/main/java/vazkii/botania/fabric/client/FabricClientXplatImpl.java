package vazkii.botania.fabric.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaFabricClientCapabilities;
import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.api.item.TinyPotatoRenderCallback;
import vazkii.botania.network.IPacket;
import vazkii.botania.xplat.IClientXplatAbstractions;

import javax.annotation.Nullable;

import java.util.Random;

public class FabricClientXplatImpl implements IClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		TinyPotatoRenderCallback.EVENT.invoker().onRender(potato, name, tickDelta, ms, buffers, light, overlay);
	}

	@Override
	public void sendToServer(IPacket packet) {
		ClientPlayNetworking.send(packet.getFabricId(), packet.toBuf());
	}

	@Nullable
	@Override
	public IWandHUD findWandHud(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		return BotaniaFabricClientCapabilities.WAND_HUD.find(level, pos, state, be, Unit.INSTANCE);
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
				buffer, true, new Random(), state.getSeed(pos), overlay);
	}
}
