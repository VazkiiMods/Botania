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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EnderOverseerBlockEntity extends BotaniaBlockEntity {

	public static final int RANGE = 64;

	private static long lastTickedGameTime = -1;
	private long lookedAtGameTime = -1;

	public EnderOverseerBlockEntity(BlockPos pos, BlockState state) {
		super(BotaniaBlockEntities.ENDER_EYE, pos, state);
	}

	public static void serverTick(Level level, BlockPos pos, BlockState state, EnderOverseerBlockEntity self) {
		long gameTime = level.getGameTime();
		lastTickedGameTime = gameTime;

		boolean wasLooking = state.getValue(BlockStateProperties.POWERED);
		boolean looking = gameTime - self.lookedAtGameTime < 2;

		if (looking != wasLooking) {
			level.setBlockAndUpdate(pos, state.setValue(BlockStateProperties.POWERED, looking));
		}
	}

	public static void checkLookingAtEnderOverseer(Player player) {
		Level level = player.level();
		long gameTime = level.getGameTime();
		if (Math.abs(gameTime - lastTickedGameTime) > 5) {
			// don't even bother if there are no loaded Ender Overseer blocks
			return;
		}
		if (!player.canBeSeenByAnyone()) {
			// exclude spectators and dead players
			return;
		}

		ItemStack helm = player.getItemBySlot(EquipmentSlot.HEAD);
		if (!helm.isEmpty() && helm.is(Blocks.CARVED_PUMPKIN.asItem())) {
			return;
		}

		Vec3 from = player.getEyePosition(1);
		Vec3 dir = player.getViewVector(1);
		Vec3 to = from.add(dir.scale(RANGE));
		BlockHitResult hit = level.clip(
				new ClipContext(from, to, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, player));
		if (hit.getType() == HitResult.Type.BLOCK
				&& level.getBlockEntity(hit.getBlockPos()) instanceof EnderOverseerBlockEntity enderOverseer) {
			enderOverseer.lookedAtGameTime = gameTime;
		}
	}
}
