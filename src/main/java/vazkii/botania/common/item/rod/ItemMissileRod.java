/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

public class ItemMissileRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_MISSILE);

	private static final int COST_PER = 120;
	private static final int COST_AVATAR = 40;

	public ItemMissileRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack stack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 72000;
	}

	@Override
	public void onUse(World world, LivingEntity living, ItemStack stack, int count) {
		if (!(living instanceof PlayerEntity)) {
			return;
		}
		PlayerEntity player = (PlayerEntity) living;

		if (count != getUseDuration(stack) && count % (IManaProficiencyArmor.hasProficiency(player, stack) ? 1 : 2) == 0 && !world.isRemote && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, false)) {
			if (spawnMissile(world, player, player.getPosX() + (Math.random() - 0.5 * 0.1), player.getPosY() + 2.4 + (Math.random() - 0.5 * 0.1), player.getPosZ() + (Math.random() - 0.5 * 0.1))) {
				ManaItemHandler.instance().requestManaExactForTool(stack, player, COST_PER, true);
			}

			SparkleParticleData data = SparkleParticleData.sparkle(6F, 1F, 0.4F, 1F, 6);
			world.addParticle(data, player.getPosX(), player.getPosY() + 2.4, player.getPosZ(), 0, 0, 0);
		}
	}

	public boolean spawnMissile(World world, LivingEntity thrower, double x, double y, double z) {
		EntityMagicMissile missile;
		if (thrower != null) {
			missile = new EntityMagicMissile(thrower, false);
		} else {
			missile = ModEntities.MAGIC_MISSILE.create(world);
		}

		missile.setPosition(x, y, z);
		if (missile.findTarget()) {
			if (!world.isRemote) {
				missile.playSound(ModSounds.missile, 0.6F, 0.8F + (float) Math.random() * 0.2F);
				world.addEntity(missile);
			}

			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.resultSuccess(player.getHeldItem(hand));
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();
		if (tile.getCurrentMana() >= COST_AVATAR && tile.getElapsedFunctionalTicks() % 3 == 0 && tile.isEnabled()) {
			if (spawnMissile(world, null, te.getPos().getX() + 0.5 + (Math.random() - 0.5 * 0.1), te.getPos().getY() + 2.5 + (Math.random() - 0.5 * 0.1), te.getPos().getZ() + (Math.random() - 0.5 * 0.1))) {
				if (!world.isRemote) {
					tile.receiveMana(-COST_AVATAR);
				}
				SparkleParticleData data = SparkleParticleData.sparkle(6F, 1F, 0.4F, 1F, 6);
				world.addParticle(data, te.getPos().getX() + 0.5, te.getPos().getY() + 2.5, te.getPos().getZ() + 0.5, 0, 0, 0);
			}
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}
}
