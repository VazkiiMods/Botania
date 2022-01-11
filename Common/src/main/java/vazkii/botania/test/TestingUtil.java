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
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.GameTestAssertPosException;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.common.item.ModItems;

import javax.annotation.Nullable;

import java.util.Objects;
import java.util.function.Supplier;

public class TestingUtil {
	public static void throwPositionedAssertion(GameTestHelper helper, BlockPos relativePos, Supplier<String> msg) {
		//A couple of GameTestHelper's assertion errors throw this exception, but it's inconvenient to throw yourself
		throw new GameTestAssertPosException(msg.get(), helper.absolutePos(relativePos), relativePos, helper.getTick());
	}

	public static void assertThat(boolean value, Supplier<String> message) {
		//The same as Preconditions.checkArgument but throws a GameTestAssertException
		if (!value) {
			throw new GameTestAssertException(message.get());
		}
	}

	public static void assertAt(GameTestHelper helper, BlockPos relativePos, boolean value, Supplier<String> message) {
		//The same as Preconditions.checkArgument but throws a GameTestAssertPosException
		if (!value) {
			throwPositionedAssertion(helper, relativePos, message);
		}
	}

	public static void assertEquals(@Nullable Object a, @Nullable Object b) {
		assertEquals(a, b, () -> "Expected " + a + " to equal " + b);
	}

	public static void assertEquals(@Nullable Object a, @Nullable Object b, Supplier<String> message) {
		assertThat(Objects.equals(a, b), message);
	}

	public static void assertEqualsAt(GameTestHelper helper, BlockPos relativePos, @Nullable Object a, @Nullable Object b) {
		assertEqualsAt(helper, relativePos, a, b, () -> "Expected " + a + " to equal " + b);
	}

	public static void assertEqualsAt(GameTestHelper helper, BlockPos relativePos, @Nullable Object a, @Nullable Object b, Supplier<String> message) {
		assertAt(helper, relativePos, Objects.equals(a, b), message);
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> T assertBlockEntity(GameTestHelper helper, BlockPos relativePos, BlockEntityType<T> type) {
		BlockEntity be = helper.getBlockEntity(relativePos);

		assertAt(helper, relativePos, be != null, () -> "Expected BlockEntity of type " + Registry.BLOCK_ENTITY_TYPE.getKey(type) + " but found no BlockEntity");
		assertAt(helper, relativePos, be.getType() == type, () -> "Expected BlockEntity of type " + Registry.BLOCK_ENTITY_TYPE.getKey(type) + " but found " + Registry.BLOCK_ENTITY_TYPE.getKey(be.getType()));

		return (T) be;
	}

	@SuppressWarnings("unchecked")
	public static <T extends BlockEntity> T assertBlockEntity(GameTestHelper helper, BlockPos relativePos, Class<T> classs) {
		BlockEntity be = helper.getBlockEntity(relativePos);

		assertAt(helper, relativePos, be != null, () -> "Expected BlockEntity of class " + classs.getSimpleName() + " but found no BlockEntity");
		assertAt(helper, relativePos, classs.isAssignableFrom(be.getClass()), () -> "Expected BlockEntity to be an instance of " + classs.getSimpleName() + " but found " + be.getClass().getSimpleName());

		return (T) be;
	}

	public static BlockEntity assertAnyBlockEntity(GameTestHelper helper, BlockPos relativePos) {
		BlockEntity be = helper.getBlockEntity(relativePos);
		assertAt(helper, relativePos, be != null, () -> "Expected any BlockEntity but found nothing");
		return be;
	}

	public static void useItemOn(GameTestHelper helper, Player player, InteractionHand hand, BlockPos pos) {
		//Gametest gotcha: You don't have a ClientPlayer or a ServerPlayer, you literally just have a Player.
		//So there's no XxxxPlayerGameMode, which is where Item#useOn(UseOnContext) style interactions typically happen.
		//This is sorta a discount version of ServerPlayerGameMode#useItemOn, no cheat checks or criteria triggers or anything.
		BlockPos absolutePos = helper.absolutePos(pos);
		BlockHitResult result = new BlockHitResult(Vec3.atCenterOf(absolutePos), Direction.NORTH, absolutePos, true);
		ItemStack stack = player.getItemInHand(hand);
		UseOnContext useOnContext = new UseOnContext(player, hand, result);
		stack.useOn(useOnContext);
	}

	public static void bindWithWandOfTheForest(GameTestHelper helper, BlockPos first, BlockPos second) {
		//Conjure a player with Wand of the Forest
		Player player = helper.makeMockPlayer();
		player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(ModItems.twigWand));
		player.setShiftKeyDown(true);

		//Move the player to each destination just to make sure they're in-range
		player.setPos(Vec3.atCenterOf(first));
		useItemOn(helper, player, InteractionHand.MAIN_HAND, first);
		player.setPos(Vec3.atCenterOf(second));
		useItemOn(helper, player, InteractionHand.MAIN_HAND, second);
	}
}
