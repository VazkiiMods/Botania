/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Jul 31, 2014, 7:19:26 PM (GMT)]
 */
package vazkii.botania.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.rod.ItemExchangeRod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemEnderHand extends ItemMod implements IManaUsingItem, IBlockProvider {

	private static final int COST_PROVIDE = 5;
	private static final int COST_SELF = 250;
	private static final int COST_OTHER = 5000;

	public ItemEnderHand() {
		super(LibItemNames.ENDER_HAND);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		if(ManaItemHandler.requestManaExact(stack, player, COST_SELF, false)) {
			if(!player.world.isRemote)
				player.displayGUIChest(player.getInventoryEnderChest());
			ManaItemHandler.requestManaExact(stack, player, COST_SELF, true);
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}

	@Override
	public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer player, EntityLivingBase entity, EnumHand hand) {
		if(ConfigHandler.enderPickpocketEnabled && entity instanceof EntityPlayer && ManaItemHandler.requestManaExact(stack, player, COST_OTHER, false)) {
			if(!player.world.isRemote)
				player.displayGUIChest(((EntityPlayer) entity).getInventoryEnderChest());
			ManaItemHandler.requestManaExact(stack, player, COST_OTHER, true);
			player.playSound(SoundEvents.ENTITY_ENDERMEN_TELEPORT, 1F, 1F);
			return true;
		}

		return false;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(requestor != null && requestor.getItem() == this)
			return false;

		ItemStack istack = ItemExchangeRod.removeFromInventory(player, new InvWrapper(player.getInventoryEnderChest()), stack, block, meta, false);
		if(!istack.isEmpty()) {
			boolean mana = ManaItemHandler.requestManaExact(stack, player, COST_PROVIDE, false);
			if(mana) {
				if(doit) {
					ManaItemHandler.requestManaExact(stack, player, COST_PROVIDE, true);
					ItemExchangeRod.removeFromInventory(player, new InvWrapper(player.getInventoryEnderChest()), stack, block, meta, true);
				}

				return true;
			}
		}

		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor,  ItemStack stack, Block block, int meta) {
		if(requestor != null && requestor.getItem() == this)
			return 0;

		return ItemExchangeRod.getInventoryItemCount(player, new InvWrapper(player.getInventoryEnderChest()), stack, block, meta);
	}

}
