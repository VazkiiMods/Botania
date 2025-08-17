package vazkii.botania.common.world;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;

public class BotaniaExplosionDamageCalculator extends ExplosionDamageCalculator {
	private final boolean shouldBreakBlocks;
	private final TagKey<EntityType<?>> ignoredEntities;

	public BotaniaExplosionDamageCalculator(boolean shouldBreakBlocks, TagKey<EntityType<?>> ignoredEntities) {
		this.shouldBreakBlocks = shouldBreakBlocks;
		this.ignoredEntities = ignoredEntities;
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state, float power) {
		return shouldBreakBlocks;
	}

	@Override
	public boolean shouldDamageEntity(Explosion explosion, Entity entity) {
		return !entity.getType().is(ignoredEntities);
	}
}
