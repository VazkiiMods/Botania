/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Apr 11, 2014, 2:56:39 PM (GMT)]
 */
package vazkii.botania.common.item.rod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;
import vazkii.botania.api.item.IBlockProvider;
import vazkii.botania.api.item.IManaProficiencyArmor;
import vazkii.botania.api.mana.IManaUsingItem;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.api.subtile.ISpecialFlower;
import vazkii.botania.common.Botania;
import vazkii.botania.common.achievement.ICraftAchievement;
import vazkii.botania.common.achievement.ModAchievements;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;
import vazkii.botania.common.lib.LibMisc;

public class ItemTerraformRod extends ItemMod implements IManaUsingItem, IBlockProvider, ICraftAchievement{

	private static final int COST_PER = 55;

	static final List<String> validBlocks = Arrays.asList(new String[] {
			"stone",
			"dirt",
			"grass",
			"sand",
			"gravel",
			"hardenedClay",
			"snowLayer",
			"mycelium",
			"podzol",
			"sandstone",

			// Mod support
			"blockDiorite",
			"stoneDiorite",
			"blockGranite",
			"stoneGranite",
			"blockAndesite",
			"stoneAndesite",
			"marble",
			"blockMarble",
			"limestone",
			"blockLimestone"
	});

	public ItemTerraformRod() {
		super();
		setMaxStackSize(1);
		setUnlocalizedName(LibItemNames.TERRAFORM_ROD);
	}

	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.bow;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 72000;
	}

	@Override
	public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
		if(count != getMaxItemUseDuration(stack) && count % 10 == 0)
			terraform(stack, player.worldObj, player);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		par3EntityPlayer.setItemInUse(par1ItemStack, getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	public void terraform(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int range = IManaProficiencyArmor.Helper.hasProficiency(par3EntityPlayer) ? 22 : 16;

		int xCenter = (int) par3EntityPlayer.posX;
		int yCenter = (int) par3EntityPlayer.posY - (par2World.isRemote ? 2 : 1);
		int zCenter = (int) par3EntityPlayer.posZ;

		if(yCenter < 62) // Not below sea level
			return;

		int yStart = yCenter + range;

		List<CoordsWithBlock> blocks = new ArrayList();

		for(int i = -range; i < range + 1; i++)
			for(int j = -range; j < range + 1; j++) {
				int k = 0;
				while(true) {
					if(yStart + k < 0)
						break;

					int x = xCenter + i;
					int y = yStart + k;
					int z = zCenter + j;

					Block block = par2World.getBlock(x, y, z);
					int meta = par2World.getBlockMetadata(x, y, z);

					int[] ids = OreDictionary.getOreIDs(new ItemStack(block, 1, meta));
					for(int id : ids)
						if(validBlocks.contains(OreDictionary.getOreName(id))) {
							boolean hasAir = false;
							List<ChunkCoordinates> airBlocks = new ArrayList();

							for(ForgeDirection dir : LibMisc.CARDINAL_DIRECTIONS) {
								int x_ = x + dir.offsetX;
								int y_ = y + dir.offsetY;
								int z_ = z + dir.offsetZ;

								Block block_ = par2World.getBlock(x_, y_, z_);
								if(block_.isAir(par2World, x_, y_, z_) || block_.isReplaceable(par2World, x_, y_, z_) || block_ instanceof BlockFlower && !(block_ instanceof ISpecialFlower) || block_ == Blocks.double_plant) {
									airBlocks.add(new ChunkCoordinates(x_, y_, z_));
									hasAir = true;
								}
							}

							if(hasAir) {
								if(y > yCenter)
									blocks.add(new CoordsWithBlock(x, y, z, Blocks.air));
								else for(ChunkCoordinates coords : airBlocks) {
									if(par2World.getBlock(coords.posX, coords.posY - 1, coords.posZ) != Blocks.air)
										blocks.add(new CoordsWithBlock(coords.posX, coords.posY, coords.posZ, Blocks.dirt));
								}
							}
							break;
						}
					--k;
				}
			}

		int cost = COST_PER * blocks.size();

		if(par2World.isRemote || ManaItemHandler.requestManaExactForTool(par1ItemStack, par3EntityPlayer, cost, true)) {
			if(!par2World.isRemote)
				for(CoordsWithBlock block : blocks)
					par2World.setBlock(block.posX, block.posY, block.posZ, block.block);

			if(!blocks.isEmpty()) {
				for(int i = 0; i < 10; i++)
					par2World.playSoundAtEntity(par3EntityPlayer, "step.sand", 1F, 0.4F);
				for(int i = 0; i < 120; i++)
					Botania.proxy.sparkleFX(par2World, xCenter - range + range * 2 * Math.random(), yCenter + 2 + (Math.random() - 0.5) * 2, zCenter - range + range * 2 * Math.random(), 0.35F, 0.2F, 0.05F, 2F, 5);
			}
		}
	}

	@Override
	public boolean isFull3D() {
		return true;
	}

	class CoordsWithBlock extends ChunkCoordinates {

		final Block block;

		public CoordsWithBlock(int x, int y, int z, Block block) {
			super(x, y, z);
			this.block = block;
		}

	}

	@Override
	public boolean usesMana(ItemStack stack) {
		return true;
	}

	@Override
	public boolean provideBlock(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta, boolean doit) {
		if(block == Blocks.dirt && meta == 0)
			return !doit || ManaItemHandler.requestManaExactForTool(requestor, player, ItemDirtRod.COST, true);
		return false;
	}

	@Override
	public int getBlockCount(EntityPlayer player, ItemStack requestor, ItemStack stack, Block block, int meta) {
		if(block == Blocks.dirt && meta == 0)
			return -1;
		return 0;
	}

	@Override
	public Achievement getAchievementOnCraft(ItemStack stack, EntityPlayer player, IInventory matrix) {
		return ModAchievements.terraformRodCraft;
	}

}
