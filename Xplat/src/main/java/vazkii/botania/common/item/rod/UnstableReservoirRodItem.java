/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import org.jetbrains.annotations.Nullable;

import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.mana.ManaReceiver;
import vazkii.botania.client.lib.ResourcesLib;
import vazkii.botania.common.entity.BotaniaEntities;
import vazkii.botania.common.entity.MagicMissileEntity;
import vazkii.botania.common.handler.BotaniaSounds;
import vazkii.botania.network.clientbound.RodOfTheUnstableReservoirEffectPacket;
import vazkii.botania.xplat.XplatAbstractions;

public class UnstableReservoirRodItem extends Item {

	private static final ResourceLocation AVATAR_OVERLAY = ResourceLocation.parse(ResourcesLib.MODEL_AVATAR_MISSILE);

	private static final int COST_PER = 120;
	private static final int COST_AVATAR = 40;

	public UnstableReservoirRodItem(Properties props) {
		super(props);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 72000;
	}

	@Override
	public void onUseTick(Level level, LivingEntity living, ItemStack stack, int remainingUseDuration) {
		if (level.isClientSide || !(living instanceof Player player)
				|| remainingUseDuration == getUseDuration(stack, living)
				|| remainingUseDuration % (ManaItemHandler.instance().hasProficiency(player, stack) ? 1 : 2) != 0
				|| !ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, false)) {
			return;
		}

		if (spawnMissile(level, player,
				player.getX() + (level.getRandom().nextDouble() - 0.5) * 0.1,
				player.getY(1) + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.1,
				player.getZ() + (level.getRandom().nextDouble() - 0.5) * 0.1)) {
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, true);
			XplatAbstractions.instance().sendToNear(level, player.blockPosition(),
					new RodOfTheUnstableReservoirEffectPacket(
							new Vec3(player.getX(), player.getY(1) + 0.5, player.getZ()))
			);
		}
	}

	public static boolean spawnMissile(Level level, @Nullable LivingEntity thrower, double x, double y, double z) {
		MagicMissileEntity missile = thrower != null
				? new MagicMissileEntity(thrower, false)
				: BotaniaEntities.MAGIC_MISSILE.create(level);
		if (missile == null) {
			return false;
		}
		missile.setPos(x, y, z);
		if (missile.findTarget()) {
			if (!level.isClientSide) {
				missile.playSound(level.random.nextInt(100) == 0 ? BotaniaSounds.MISSILE_FUNNY : BotaniaSounds.MISSILE,
						1, 0.8f + level.getRandom().nextFloat() * 0.2f);
				level.addFreshEntity(missile);
			}

			return true;
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		return ItemUtils.startUsingInstantly(level, player, hand);
	}

	public record AvatarBehavior(ItemStack rod, Avatar avatar) implements AvatarWieldable {
		@Override
		public void onAvatarUpdate(ServerLevel level, BlockPos pos, ManaReceiver receiver) {
			if (receiver.getCurrentMana() >= COST_AVATAR && avatar.isEnabled() && getTimeSinceLastActivation(level) >= 3) {
				double yOffset = level.getBlockState(pos.above()).isAir() ? 1.5 : 2.5;
				if (spawnMissile(level, null,
						pos.getX() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.1,
						pos.getY() + yOffset + (level.getRandom().nextDouble() - 0.5) * 0.1,
						pos.getZ() + 0.5 + (level.getRandom().nextDouble() - 0.5) * 0.1)) {
					receiver.receiveMana(-COST_AVATAR);
					XplatAbstractions.instance().sendToNear(level, pos, new RodOfTheUnstableReservoirEffectPacket(
							new Vec3(pos.getX() + 0.5, pos.getY() + yOffset, pos.getZ() + 0.5)));
				}
				setLastActivationTime(level);
			}
		}

		@Override
		public ResourceLocation getOverlayResource() {
			return AVATAR_OVERLAY;
		}
	}
}
