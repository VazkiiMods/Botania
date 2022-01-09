package vazkii.botania.xplat;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;
import vazkii.botania.api.block.IWandHUD;

import javax.annotation.Nullable;

import java.util.ServiceLoader;
import java.util.stream.Collectors;

public interface IClientXplatAbstractions {
	// Event firing
	void fireRenderTinyPotato(BlockEntity potato, Component name, float tickDelta, PoseStack ms, MultiBufferSource buffers, int light, int overlay);

	// Capability access
	@Nullable
	IWandHUD findWandHud(Level level, BlockPos pos, BlockState state, BlockEntity be);

	IClientXplatAbstractions INSTANCE = find();

	private static IClientXplatAbstractions find() {
		var providers = ServiceLoader.load(IClientXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
			throw new IllegalStateException("There should be exactly one IClientXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			BotaniaAPI.LOGGER.debug("Instantiating client xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
