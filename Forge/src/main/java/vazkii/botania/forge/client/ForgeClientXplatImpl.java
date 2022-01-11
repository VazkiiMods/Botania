package vazkii.botania.forge.client;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.IWandHUD;
import vazkii.botania.xplat.IClientXplatAbstractions;

public class ForgeClientXplatImpl implements IClientXplatAbstractions {
	@Override
	public void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay) {
		throw new UnsupportedOperationException("NYI");
	}

	@Nullable
	@Override
	public IWandHUD findWandHud(Level level, BlockPos pos, BlockState state, BlockEntity be) {
		throw new UnsupportedOperationException("NYI");
	}

	@Override
	public BakedModel wrapPlatformModel(BakedModel original) {
		return new ForgePlatformModel(original);
	}
}
