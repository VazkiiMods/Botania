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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import vazkii.botania.api.block.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

public class ItemMissileRod extends Item {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_MISSILE);

	private static final int COST_PER = 120;
	private static final int COST_AVATAR = 40;

	public ItemMissileRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUseTick(Level world, LivingEntity living, ItemStack stack, int count) {
		if (!(living instanceof Player player)) {
			return;
		}

		if (count != getUseDuration(stack) && count % (IManaProficiencyArmor.hasProficiency(player, stack) ? 1 : 2) == 0 && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, false)) {
			if (!world.isClientSide && spawnMissile(world, player, player.getX() + (Math.random() - 0.5 * 0.1), player.getY() + 2.4 + (Math.random() - 0.5 * 0.1), player.getZ() + (Math.random() - 0.5 * 0.1))) {
				ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, true);
			}

			SparkleParticleData data = SparkleParticleData.sparkle(6F, 1F, 0.4F, 1F, 6);
			world.addParticle(data, player.getX(), player.getY() + 2.4, player.getZ(), 0, 0, 0);
		}
	}

	public static boolean spawnMissile(Level world, LivingEntity thrower, double x, double y, double z) {
		EntityMagicMissile missile;
		if (thrower != null) {
			missile = new EntityMagicMissile(thrower, false);
		} else {
			missile = ModEntities.MAGIC_MISSILE.create(world);
		}

		missile.setPos(x, y, z);
		if (missile.findTarget()) {
			if (!world.isClientSide) {
				missile.playSound(world.random.nextInt(100) == 0 ? ModSounds.missileFunny : ModSounds.missile, 1F, 0.8F + (float) Math.random() * 0.2F);
				world.addFreshEntity(missile);
			}

			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level world, Player player, @Nonnull InteractionHand hand) {
		return ItemUtils.startUsingInstantly(world, player, hand);
	}

	public static class AvatarBehavior implements IAvatarWieldable {
		@Override
		public void onAvatarUpdate(IAvatarTile tile) {
			BlockEntity te = tile.tileEntity();
			Level world = te.getLevel();
			BlockPos pos = te.getBlockPos();
			if (tile.getCurrentMana() >= COST_AVATAR && tile.getElapsedFunctionalTicks() % 3 == 0 && tile.isEnabled()) {
				if (spawnMissile(world, null, pos.getX() + 0.5 + (Math.random() - 0.5 * 0.1), pos.getY() + 2.5 + (Math.random() - 0.5 * 0.1), pos.getZ() + (Math.random() - 0.5 * 0.1))) {
					if (!world.isClientSide) {
						tile.receiveMana(-COST_AVATAR);
					}
					SparkleParticleData data = SparkleParticleData.sparkle(6F, 1F, 0.4F, 1F, 6);
					world.addParticle(data, pos.getX() + 0.5, pos.getY() + 2.5, pos.getZ() + 0.5, 0, 0, 0);
				}
			}
		}

		@Override
		public ResourceLocation getOverlayResource(IAvatarTile tile) {
			return avatarOverlay;
		}
	}
}
