/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.test;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

import java.util.Objects;

public class TestingUtil {
	public static void assertThat(boolean value, String message) {
		//The same as Preconditions.checkArgument but throws a GameTestAssertException
		if (!value) {
			throw new GameTestAssertException(message);
		}
	}

	public static <T> void assertEquals(@Nullable T a, @Nullable T b) {
		assertThat(Objects.equals(a, b), "Expected " + a + " to equal " + b);
	}

	public static InteractionResult useItemOn(GameTestHelper helper, Player player, InteractionHand hand, BlockPos pos) {
		//Gametest gotcha: You don't have a ClientPlayer or a ServerPlayer, you literally just have a Player.
		//So there's no XxxxPlayerGameMode, which is where Item#useOn(UseOnContext) style interactions typically happen.
		//This is sorta a discount version of ServerPlayerGameMode#useItemOn, no cheat checks or criteria triggers or anything.
		BlockPos absolutePos = helper.absolutePos(pos);
		BlockHitResult result = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.NORTH, absolutePos, true);
		ItemStack stack = player.getItemInHand(hand);
		UseOnContext useOnContext = new UseOnContext(player, hand, result);
		return stack.useOn(useOnContext);
	}
}
