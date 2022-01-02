package vazkii.botania.fabric.xplat;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.xplat.IXplatAbstractions;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;

public class FabricXplatImpl implements IXplatAbstractions {
	@Override
	public boolean isModLoaded(String modId) {
		return FabricLoader.getInstance().isModLoaded(modId);
	}

	@Override
	public boolean isDevEnvironment() {
		return FabricLoader.getInstance().isDevelopmentEnvironment();
	}

	@Override
	public boolean isPhysicalClient() {
		return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
	}

	@Override
	public <T extends BlockEntity> BlockEntityType<T> createBlockEntityType(BiFunction<BlockPos, BlockState, T> func, Block... blocks) {
		return FabricBlockEntityTypeBuilder.create(func::apply, blocks).build();
	}

	@Override
	public void registerReloadListener(PackType type, ResourceLocation id, PreparableReloadListener listener) {
		ResourceManagerHelper.get(type).registerReloadListener(new IdentifiableResourceReloadListener() {
			@Override
			public CompletableFuture<Void> reload(PreparationBarrier barrier, ResourceManager manager, ProfilerFiller prepProfiler,
					ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
				return listener.reload(barrier, manager, prepProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
			}

			@Override
			public ResourceLocation getFabricId() {
				return id;
			}
		});
	}
}
