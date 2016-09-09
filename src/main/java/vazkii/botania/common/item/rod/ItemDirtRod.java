/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 11, 2014, 2:53:41 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

import javax.annotation.Nonnull;

public class ItemDirtRod extends ItemMod implements IManaUsingItem, ICraftAchievement, IBlockProvider {

	static final int COST = 75;

	public ItemDirtRod() {
		this(LibItemNames.DIRT_ROD);
	}

	public ItemDirtRod(String name) {
		super(name);
		setMaxStackSize(1);
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUse(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10) {
		return place(par1ItemStack, player, world, pos, hand, side, par8, par9, par10, Blocks.DIRT, COST, 0.35F, 0.2F, 0.05F);
	}

	public static EnumActionResult place(ItemStack par1ItemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float par8, float par9, float par10, Block block, int cost, float r, float g, float b) {
		if(ManaItemHandler.requestManaExactForTool(par1ItemStack, player, cost, false)) {
			int entities = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos.offset(side), pos.offset(side).add(1, 1, 1))).size();

			if(entities == 0) {
				ItemStack stackToPlace = new ItemStack(block);
				stackToPlace.onItemUse(player, world, pos, hand, side, par8, par9, par10);

				if(stackToPlace.stackSize == 0) {
					ManaItemHandler.requestManaExactForTool(par1ItemStack, player, cost, true);
					for(int i = 0; i < 6; i++)
						Botania.proxy.sparkleFX(pos.getX() + side.getFrontOffsetX() + Math.random(), pos.getY() + side.getFrontOffsetY() + Math.random(), pos.getZ() + side.getFrontOffsetZ() + Math.random(), r, g, b, 1F, 5);
					return EnumActionResult.SUCCESS;
				}
			}

			return EnumActionResult.FAIL;
		}

		return EnumActionResult.PASS;
	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.dirtRodCraft;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(block == Blocks.DIRT && meta == 0)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, COST, true);
		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		if(block == Blocks.DIRT && meta == 0)
			return -1;
		return 0;
	}


}