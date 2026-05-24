package vazkii.botania.mixin.datafixers;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.MapCodec;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.storage.ChunkStorage;
import net.minecraft.world.level.storage.DimensionDataStorage;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import vazkii.botania.datafix.ChunkStorageMigration;

import java.util.Optional;
import java.util.function.Supplier;

@Mixin(ChunkStorage.class)
public class ChunkStorageMixin {
	@Inject(method = "upgradeChunkTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/NbtUtils;addCurrentDataVersion(Lnet/minecraft/nbt/CompoundTag;)Lnet/minecraft/nbt/CompoundTag;"))
	private void injectChunkFixer(
			ResourceKey<Level> levelKey,
			Supplier<DimensionDataStorage> storage,
			CompoundTag chunkData,
			Optional<ResourceKey<MapCodec<? extends ChunkGenerator>>> chunkGeneratorKey,
			CallbackInfoReturnable<CompoundTag> cir,
			@Local(ordinal = 0) int oldVersion) {
		// 3955 is the data version of 1.21.1
		if (oldVersion < 3955) {
			ChunkStorageMigration.rewriteChunkData(chunkData);
		}
	}
}
