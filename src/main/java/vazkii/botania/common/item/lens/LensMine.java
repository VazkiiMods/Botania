/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Jan 24, 2015, 4:36:20 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.item.ModItems;

public class LensMine extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		World world = entity.worldObj;
		int x = pos.blockX;
		int y = pos.blockY;
		int z = pos.blockZ;
		Block block = world.getBlock(x, y, z);
		int meta = world.getBlockMetadata(x, y, z);
		ItemStack composite = ((ItemLens) ModItems.lens).getCompositeLens(stack);
		boolean warp = composite != null && composite.getItem() == ModItems.lens && composite.getItemDamage() == ItemLens.WARP;
		
		if(warp && (block == ModBlocks.pistonRelay || block == Blocks.piston || block == Blocks.piston_extension || block == Blocks.piston_head))
			return false;

		int harvestLevel = ConfigHandler.harvestLevelBore;
		
		TileEntity tile = world.getTileEntity(x, y, z);

		float hardness = block.getBlockHardness(world, x, y, z);
		int neededHarvestLevel = block.getHarvestLevel(meta);
		int mana = burst.getMana();

		ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
		if((coords.posX != x || coords.posY != y || coords.posZ != z) && !(tile instanceof IManaBlock) && neededHarvestLevel <= harvestLevel && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
			List<ItemStack> items = new ArrayList();

			items.addAll(block.getDrops(world, x, y, z, meta, 0));

			if(!burst.hasAlreadyCollidedAt(x, y, z)) {
				if(!burst.isFake() && !entity.worldObj.isRemote) {
					world.setBlockToAir(x, y, z);
					if(ConfigHandler.blockBreakParticles)
						entity.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));

					boolean offBounds = coords.posY < 0;
					boolean doWarp = warp && !offBounds;
					int dropX = doWarp ? coords.posX : x;
					int dropY = doWarp ? coords.posY : y;
					int dropZ = doWarp ? coords.posZ : z;

					for(ItemStack stack_ : items)
						world.spawnEntityInWorld(new EntityItem(world, dropX + 0.5, dropY + 0.5, dropZ + 0.5, stack_));

					burst.setMana(mana - 24);
				}
			}

			dead = false;
		}

		return dead;
	}

}
