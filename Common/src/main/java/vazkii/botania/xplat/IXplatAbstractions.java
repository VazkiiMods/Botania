package vazkii.botania.xplat;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.BotaniaAPI;

import java.util.ServiceLoader;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public interface IXplatAbstractions {
	// FML/Fabric Loader
	boolean isModLoaded(String modId);
	boolean isDevEnvironment();
	boolean isPhysicalClient();
	String getBotaniaVersion();

	// Misc
	<T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks);

	void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener);

	IXplatAbstractions INSTANCE = find();

	private static IXplatAbstractions find() {
		var providers = ServiceLoader.load(IXplatAbstractions.class).stream().toList();
		if (providers.size() != 1) {
			var names = providers.stream().map(p -> p.type().getName()).collect(Collectors.joining(",", "[", "]"));
			throw new IllegalStateException("There should be exactly one IXplatAbstractions implementation on the classpath. Found: " + names);
		} else {
			var provider = providers.get(0);
			BotaniaAPI.LOGGER.debug("Instantiating xplat impl: " + provider.type().getName());
			return provider.get();
		}
	}
}
