/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Nov 24, 2014, 5:58:16 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.UseAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.Botania;
import vazkii.botania.common.core.handler.ModSounds;
import vazkii.botania.common.entity.EntityMagicMissile;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemMissileRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_MISSILE);

	private static final int COST_PER = 120;
	private static final int COST_AVATAR = 40;

	public ItemMissileRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public UseAction getUseAction(ItemStack par1ItemStack) {
		return UseAction.BOW;
	}

	@Override
	public int getUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, LivingEntity living, int count) {
		if(!(living instanceof PlayerEntity)) return;
		PlayerEntity player = (PlayerEntity) living;

		if(count != getUseDuration(stack) && count % (IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? 1 : 2) == 0 && !player.world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST_PER, false)) {
			if(spawnMissile(player.world, player, player.posX + (Math.random() - 0.5 * 0.1), player.posY + 2.4 + (Math.random() - 0.5 * 0.1), player.posZ + (Math.random() - 0.5 * 0.1)))
				ManaItemHandler.requestManaExactForTool(stack, player, COST_PER, true);

			Botania.proxy.sparkleFX(player.posX, player.posY + 2.4, player.posZ, 1F, 0.4F, 1F, 6F, 6);
		}
	}

	public boolean spawnMissile(World world, LivingEntity thrower, double x, double y, double z) {
		EntityMagicMissile missile;
		if(thrower != null)
			missile = new EntityMagicMissile(thrower, false);
		else missile = new EntityMagicMissile(world);

		missile.setPosition(x, y, z);
		if(missile.findTarget()) {
			if(!world.isRemote) {
				missile.playSound(ModSounds.missile, 0.6F, 0.8F + (float) Math.random() * 0.2F);
				world.spawnEntity(missile);
			}

			return true;
		}
		return false;
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		player.setActiveHand(hand);
		return ActionResult.newResult(ActionResultType.SUCCESS, player.getHeldItem(hand));
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();
		if(tile.getCurrentMana() >= COST_AVATAR && tile.getElapsedFunctionalTicks() % 3 == 0 && tile.isEnabled())
			if(spawnMissile(world, null, te.getPos().getX() + 0.5 + (Math.random() - 0.5 * 0.1), te.getPos().getY() + 2.5 + (Math.random() - 0.5 * 0.1), te.getPos().getZ() + (Math.random() - 0.5 * 0.1))) {
				if(!world.isRemote)
					tile.recieveMana(-COST_AVATAR);
				Botania.proxy.sparkleFX(te.getPos().getX() + 0.5, te.getPos().getY() + 2.5, te.getPos().getZ() + 0.5, 1F, 0.4F, 1F, 6F, 6);
			}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}
}
