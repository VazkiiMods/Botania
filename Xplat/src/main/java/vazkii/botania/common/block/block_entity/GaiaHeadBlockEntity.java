/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.block_entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class GaiaHeadBlockEntity extends BlockEntity {
	// [VanillaCopy] SkullBlockEntity
	private int animationTickCount;
	private boolean isAnimating;

	public GaiaHeadBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.GAIA_HEAD, pos, state);
	}

	@Override
	public BlockEntityType<GaiaHeadBlockEntity> getType() {
		return BotaniaBlockEntities.GAIA_HEAD;
	}

	// [VanillaCopy] SkullBlockEntity, which we cannot extend directly for various reasons
	public static void animation(Level level, BlockPos pos, BlockState state, GaiaHeadBlockEntity self) {
		if (state.hasProperty(SkullBlock.POWERED) && state.getValue(SkullBlock.POWERED)) {
			self.isAnimating = true;
			self.animationTickCount++;
		} else {
			self.isAnimating = false;
		}
	}

	// [VanillaCopy] SkullBlockEntity
	public float getAnimation(float partialTick) {
		return this.isAnimating ? this.animationTickCount + partialTick : this.animationTickCount;
	}
}
