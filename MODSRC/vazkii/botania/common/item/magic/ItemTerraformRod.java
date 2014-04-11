/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Apr 11, 2014, 2:56:39 PM (GMT)]
 */
package vazkii.botania.common.item.magic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import vazkii.botania.api.ISpecialFlower;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.Botania;
import vazkii.botania.common.item.ItemMod;
import vazkii.botania.common.lib.LibItemNames;

public class ItemTerraformRod extends ItemMod  {

	private static final int COST_PER = 55;

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
		par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
		return par1ItemStack;
	}

	public void terraform(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		int range = 16;

		int xCenter = (int) par3EntityPlayer.posX;
		int yCenter = (int) par3EntityPlayer.posY - (par2World.isRemote ? 2 : 1);
		int zCenter = (int) par3EntityPlayer.posZ;

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
					if(block == Blocks.grass || block == Blocks.dirt || block == Blocks.stone || block == Blocks.sand || block == Blocks.gravel) {
						boolean hasAir = false;
						List<ChunkCoordinates> airBlocks = new ArrayList();

						final ForgeDirection[] dirs = new ForgeDirection[] {
								ForgeDirection.NORTH, ForgeDirection.SOUTH, ForgeDirection.EAST, ForgeDirection.WEST
						};

						for(ForgeDirection dir : dirs) {
							int x_ = x + dir.offsetX;
							int y_ = y + dir.offsetY;
							int z_ = z + dir.offsetZ;

							Block block_ = par2World.getBlock(x_, y_, z_); 
							if(block_.isAir(par2World, x_, y_, z_) || block_.isReplaceable(par2World, x_, y_, z_) || (block_ instanceof BlockFlower && !(block_ instanceof ISpecialFlower))) {
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

		if(par2World.isRemote || ManaItemHandler.requestManaExact(par1ItemStack, par3EntityPlayer, cost, true)) {
			if(!par2World.isRemote)
				for(CoordsWithBlock block : blocks)
					par2World.setBlock(block.posX, block.posY, block.posZ, block.block);

			if(!blocks.isEmpty()) {
				par2World.playSoundAtEntity(par3EntityPlayer, "step.sand", 1F, 0.4F);
				for(int i = 0; i < 120; i++)
					Botania.proxy.sparkleFX(par2World, xCenter - range + range * 2 * Math.random(), yCenter + 2 + (Math.random() - 0.5) * 2, zCenter - range + range * 2 * Math.random(), 0.35F, 0.2F, 0.05F, 2F, 5);
			}
		}
	}

	class CoordsWithBlock extends ChunkCoordinates {

		final Block block;

		public CoordsWithBlock(int x, int y, int z, Block block) {
			super(x, y, z);
			this.block = block;
		}

	}

}
