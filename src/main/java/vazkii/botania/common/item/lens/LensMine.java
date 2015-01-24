/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Jan 24, 2015, 4:36:20 PM (GMT)]
 */
package vazkii.botania.common.item.lens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import vazkii.botania.api.internal.IManaBurst;
import vazkii.botania.api.mana.BurstProperties;
import vazkii.botania.api.mana.IManaBlock;
import vazkii.botania.common.core.handler.ConfigHandler;

public class LensMine extends Lens {

	@Override
	public boolean collideBurst(IManaBurst burst, EntityThrowable entity, MovingObjectPosition pos, boolean isManaBlock, boolean dead, ItemStack stack) {
		World world = entity.worldObj;
		int x = pos.blockX;
		int y = pos.blockY;
		int z = pos.blockZ;
		Block block = world.getBlock(x, y, z);
		TileEntity tile = world.getTileEntity(x, y, z);

		int meta = world.getBlockMetadata(x, y, z);
		float hardness = block.getBlockHardness(world, x, y, z);
		int mana = burst.getMana();

		ChunkCoordinates coords = burst.getBurstSourceChunkCoordinates();
		if((coords.posX != x || coords.posY != y || coords.posZ != z) && !(tile instanceof IManaBlock) && block != null && hardness != -1 && hardness < 50F && (burst.isFake() || mana >= 24)) {
			List<ItemStack> items = new ArrayList();

			items.addAll(block.getDrops(world, x, y, z, meta, 0));

			if(!burst.hasAlreadyCollidedAt(x, y, z)) {
				if(!burst.isFake() && !entity.worldObj.isRemote) {
					world.setBlockToAir(x, y, z);
					if(ConfigHandler.blockBreakParticles)
						entity.worldObj.playAuxSFX(2001, x, y, z, Block.getIdFromBlock(block) + (meta << 12));

					for(ItemStack stack_ : items)
						world.spawnEntityInWorld(new EntityItem(world, x + 0.5, y + 0.5, z + 0.5, stack_));
					burst.setMana(mana - 24);
				}
			}

			dead = false;
		}
		
		return dead;
	}
	
}
