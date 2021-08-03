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
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

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

	private static final ResourceLocation avatarOverlay = new ResourceLocation(LibResources.MODEL_AVATAR_FIRE);

	private static final int COST = 900;
	private static final int COOLDOWN = 1200;

	public ItemFireRod(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public InteractionResult useOn(UseOnContext ctx) {
		Level world = ctx.getLevel();
		Player player = ctx.getPlayer();
		ItemStack stack = ctx.getItemInHand();
		BlockPos pos = ctx.getClickedPos();

		if (!world.isClientSide && player != null && ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, false)) {
			EntityFlameRing entity = ModEntities.FLAME_RING.create(world);
			entity.setPos(pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5);
			world.addFreshEntity(entity);

			if (!player.isCreative()) {
				player.getCooldowns().addCooldown(this, IManaProficiencyArmor.hasProficiency(player, stack) ? COOLDOWN / 2 : COOLDOWN);
			}
			ManaItemHandler.instance().requestManaExactForTool(stack, player, COST, true);

			ctx.getLevel().playSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.BLAZE_AMBIENT, player != null ? SoundSource.PLAYERS : SoundSource.BLOCKS, 1F, 1F);
		}

		return InteractionResult.SUCCESS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public void onAvatarUpdate(IAvatarTile tile, ItemStack stack) {
		BlockEntity te = tile.tileEntity();
		Level world = te.getLevel();

		if (!world.isClientSide && tile.getCurrentMana() >= COST && tile.getElapsedFunctionalTicks() % 300 == 0 && tile.isEnabled()) {
			EntityFlameRing entity = ModEntities.FLAME_RING.create(world);
			entity.setPos(te.getBlockPos().getX() + 0.5, te.getBlockPos().getY(), te.getBlockPos().getZ() + 0.5);
			world.addFreshEntity(entity);
			tile.receiveMana(-COST);
		}
	}

	@Override
	public ResourceLocation getOverlayResource(IAvatarTile tile, ItemStack stack) {
		return avatarOverlay;
	}

}
