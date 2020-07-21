/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import vazkii.botania.api.item.IAvatarTile;
import vazkii.botania.api.item.IAvatarWieldable;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.client.lib.LibResources;
import vazkii.botania.common.entity.EntityFlameRing;
import vazkii.botania.common.entity.ModEntities;

import javax.annotation.Nonnull;

public class ItemFireRod extends Item implements IManaUsingItem, IAvatarWieldable {

	private static final Identifier avatarOverlay = new Identifier(LibResources.MODEL_AVATAR_FIRE);

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;

	public ItemFireRod(Settings props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult useOnBlock(ItemUsageContext ctx) {
		World world = ctx.getWorld();
		PlayerEntity player = ctx.getPlayer();
		ItemStack stack = ctx.getStack();
		BlockPos pos = ctx.getBlockPos();

		if (!world.isClient && player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			EntityFlameRing entity = ModEntities.FLAME_RING.create(world);
			entity.updatePosition(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			world.spawnEntity(entity);

			player.getItemCooldownManager().set(this, IManaProficiencyArmor.hasProficiency(player, stack) ? COOLDOWN / 2 : COOLDOWN);
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);

			ctx.getWorld().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_BLAZE_AMBIENT, player != null ? SoundCategory.PLAYERS : SoundCategory.BLOCKS, 1F, 1F);
		}

		return ActionResult.SUCCESS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = (BlockEntity) tile;
		World world = te.getWorld();

		if (!world.isClient && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 300 == 0 && tile.isEnabled()) {
			EntityFlameRing entity = ModEntities.FLAME_RING.create(world);
			entity.updatePosition(te.getPos().getX() + 0.5, te.getPos().getY(), te.getPos().getZ() + 0.5);
			world.spawnEntity(entity);
			tile.receiveMana(-COST);
		}
	}

	@Override
	public Identifier getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
