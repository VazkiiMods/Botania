/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jun 26, 2014, 12:08:06 AM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemFireRod extends ItemMod implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_FIRE);

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;

	public ItemFireRod() {
		super(LibItemNames.FIRE_ROD);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
			EntityFlameRing entity = new EntityFlameRing(player.world);
			entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			player.world.spawnEntity(entity);

			player.getCooldownTracker().setCooldown(this, IManaProficiencyArmor.Helper.hasProficiency(player, stack) ? COOLDOWN / 2 : COOLDOWN);

			ManaItemHandler.requestManaExactForTool(stack, player, COST, true);
			world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_BLAZE_AMBIENT, SoundCategory.PLAYERS, 1F, 1F);

			// Fix up rods from old versions which used meta instead of cooldown tracker
			if (stack.getItemDamage() > 0)
				stack.setItemDamage(0);
		}

		return EnumActionResult.SUCCESS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		TileEntity te = (TileEntity) tile;
		World world = te.getWorld();

		if(!world.isRemote && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 300 == 0 && tile.isEnabled()) {
			EntityFlameRing entity = new EntityFlameRing(world);
			entity.setPosition(te.getPos().getX() + 0.5, te.getPos().getY(), te.getPos().getZ() + 0.5);
			world.spawnEntity(entity);
			tile.recieveMana(-COST);
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
