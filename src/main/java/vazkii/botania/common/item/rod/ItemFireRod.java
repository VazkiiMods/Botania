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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityFlameRing;
import net.minecraft.item.Item;

import javax.annotation.Nonnull;

public class ItemFireRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_FIRE);

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;

	public ItemFireRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResultType onItemUse(ItemUseContext ctx) {
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();
		ItemStack stack = ctx.getItem();
		BlockPos pos = ctx.getPos();

		if(!world.isRemote && player != null && ManaItemHandler.requestManaExactForTool(stack, player, COST, false)) {
			EntityFlameRing entity = new EntityFlameRing(world);
			entity.setPosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			world.addEntity(entity);

			player.getCooldownTracker().setCooldown(this, IManaProficiencyArmor.hasProficiency(player, stack) ? COOLDOWN / 2 : COOLDOWN);
			ManaItemHandler.requestManaExactForTool(stack, player, COST, true);

			ctx.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_AMBIENT, player != null ? SoundCategory.PLAYERS : SoundCategory.BLOCKS, 1F, 1F);
		}

		return ActionResultType.SUCCESS;
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
			world.addEntity(entity);
			tile.recieveMana(-COST);
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
