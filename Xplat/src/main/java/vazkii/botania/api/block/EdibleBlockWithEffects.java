package vazkii.botania.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import vazkii.botania.api.capability.BlockApiNoContext;

import static vazkii.botania.api.BotaniaAPI.botaniaRL;

/**
 * When a block of this kind is consumed by a mob, it can have certain side effects on that mob.
 */
public interface EdibleBlockWithEffects {
	ResourceLocation ID = botaniaRL("edible_with_effects");
	BlockApiNoContext<EdibleBlockWithEffects> LOOKUP = new BlockApiNoContext<>(ID, EdibleBlockWithEffects.class);

	void onEatenBy(BlockPos pos, BlockState state, LivingEntity entity);
}
