/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under a
 * Creative Commons Attribution-NonCommercial-ShareAlike 3.0 License
 * (http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB)
 * 
 * File Created @ [Feb 18, 2014, 10:15:50 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.tileentity.TileEntity;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.helper.Vector3;

public class TilePylon extends TileEntity {
	
	boolean activated = true; // TODO
	
	@Override
	public void updateEntity() {
		if(activated && worldObj.isRemote) {
			Vector3 centerBlock = new Vector3(-500.5, 4.75 + (Math.random() - 0.5 * 0.25), -411.5);
			
			int id = worldObj.getBlockId(xCoord, yCoord - 1, zCoord);
			if(id == ModBlocks.flower.blockID) {
				int meta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
				float[] color = EntitySheep.fleeceColorTable[meta];
				Botania.proxy.wispFX(worldObj, xCoord + 0.5 + (Math.random() - 0.5) * 0.25, yCoord - 0.5, zCoord + 0.5 + (Math.random() - 0.5) * 0.25, color[0], color[1], color[2], (float) Math.random() / 3F, -0.04F);
			
				Vector3 ourCoords = Vector3.fromTileEntityCenter(this).add(0, 1 + (Math.random() - 0.5 * 0.25), 0);
				Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);
				Botania.proxy.wispFX(worldObj, xCoord + 0.5 + (Math.random() - 0.5) * 0.25, yCoord + 1.5, zCoord + 0.5 + (Math.random() - 0.5) * 0.25, color[0], color[1], color[2], (float) Math.random() / 8F, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);

			}
		}
	}
	
}
