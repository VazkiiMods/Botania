/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package org.violetmoon.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import org.violetmoon.botania.api.block.Avatar;
import org.violetmoon.botania.api.item.AvatarWieldable;
import org.violetmoon.botania.api.mana.ManaItemHandler;
import org.violetmoon.botania.api.mana.ManaReceiver;
import org.violetmoon.botania.client.fx.WispParticleData;
import org.violetmoon.botania.client.lib.ResourcesLib;
import org.violetmoon.botania.common.handler.BotaniaSounds;
import org.violetmoon.botania.common.helper.MathHelper;
import org.violetmoon.botania.common.lib.BotaniaTags;
import org.violetmoon.botania.network.clientbound.RodOfThePlentifulMantleEffectPacket;
import org.violetmoon.botania.xplat.XplatAbstractions;

import java.util.Random;

public class PlentifulMantleRodItem extends Item {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_DIVINING);

	public static final int COST = 3000;
	public static final byte RANGE_PROFICIENCY = 20;
	public static final byte RANGE_DEFAULT = 15;
	public static final byte RANGE_AVATAR = 18;
	public static final int COOLDOWN_AVATAR = 200;

	public PlentifulMantleRodItem(Properties props) {
		super(props);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true)) {
			if (!level.isClientSide()) {
				byte range = ManaItemHandler.instance().hasProficiency(player, stack) ? RANGE_PROFICIENCY : RANGE_DEFAULT;
				XplatAbstractions.instance().sendToPlayer(player,
						new RodOfThePlentifulMantleEffectPacket(player.blockPosition(), range, true));
				player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
				player.getCooldowns().addCooldown(this, 20);
			}
			player.playSound(BotaniaSounds.ROD_OF_THE_PLENTIFUL_MANTLE, 1, 1);
			return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
		}

		return InteractionResultHolder.pass(stack);
	}

	// TODO: technically it's more accurate to determine ore positions serverside,
	//  since clients might not know about enclosed blocks in the presence of anti-xray measures
	//  (but then we'd have to figure out how to transfer potentially a LOT of data in a network packet)
	public static void doHighlight(Level level, BlockPos centerPos, int range, long seedXor) {
		for (BlockPos pos : MathHelper.aroundPosClosed(centerPos, range)) {
			BlockState state = level.getBlockState(pos);

			Block block = state.getBlock();
			if (state.is(BotaniaTags.Blocks.ROD_OF_THE_PLENTIFUL_MANTLE_HIGHLIGHTED)) {
				Random rand = new Random(BuiltInRegistries.BLOCK.getId(block) ^ seedXor);
				WispParticleData data = WispParticleData.wisp(0.25F,
						rand.nextFloat(), rand.nextFloat(), rand.nextFloat(),
						8, false);
				level.addParticle(data, true,
						pos.getX() + level.random.nextFloat(),
						pos.getY() + level.random.nextFloat(),
						pos.getZ() + level.random.nextFloat(),
						0, 0, 0);
			}
		}
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(ServerLevel level, BlockPos pos, ManaReceiver receiver) {
			if (receiver.getCurrentMana() >= COST && avatar.isEnabled()
					&& getTimeSinceLastActivation(level) >= COOLDOWN_AVATAR) {
				XplatAbstractions.instance().sendToNear(level, pos,
						new RodOfThePlentifulMantleEffectPacket(pos, RANGE_AVATAR, false));
				receiver.receiveMana(-COST);
				setLastActivationTime(level);
			}
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}
	}
}
