/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 * 
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 * 
 * File Created @ [Feb 18, 2014, 10:15:50 PM (GMT)]
 */
package vazkii.botania.common.block.tile;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.server.gui.IUpdatePlayerListBox;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import vazkii.botania.common.Botania;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;

public class TilePylon extends TileEntity implements IUpdatePlayerListBox {

	boolean activated = false;
	BlockPos centerPos;
	int ticks = 0;



	@Override
	public void update() {
		++ticks;
		int meta = getBlockMetadata();

		if(activated && worldObj.isRemote) {
			if(worldObj.getBlockState(centerPos).getBlock() != getBlockForMeta() || meta != 0 && worldObj.getBlockMetadata(centerX, centerY, centerZ) == 0) {
				activated = false;
				return;
			}

			Vector3 centerBlock = new Vector3(centerPos.getX() + 0.5, centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), centerPos.getZ() + 0.5);

			if(meta == 1) {
				if(ConfigHandler.elfPortalParticlesEnabled) {
					double worldTime = ticks;
					worldTime += new Random(pos.hashCode()).nextInt(1000);
					worldTime /= 5;

					float r = 0.75F + (float) Math.random() * 0.05F;
					double x = pos.getX() + 0.5 + Math.cos(worldTime) * r;
					double z = pos.getZ() + 0.5 + Math.sin(worldTime) * r;

					Vector3 ourCoords = new Vector3(x, pos.getY() + 0.25, z);
					centerBlock.sub(new Vector3(0, 0.5, 0));
					Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);

					Botania.proxy.wispFX(worldObj, x, pos.getY() + 0.25, z, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, -0.075F - (float) Math.random() * 0.015F);
					if(worldObj.rand.nextInt(3) == 0)
						Botania.proxy.wispFX(worldObj, x, pos.getY() + 0.25, z, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 0.25F + (float) Math.random() * 0.1F, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			} else {
				Vector3 ourCoords = Vector3.fromTileEntityCenter(this).add(0, 1 + (Math.random() - 0.5 * 0.25), 0);
				Vector3 movementVector = centerBlock.sub(ourCoords).normalize().multiply(0.2);

				Block block = worldObj.getBlockState(pos.down()).getBlock();
				if(block == ModBlocks.flower || block == ModBlocks.shinyFlower) {
					int fmeta = worldObj.getBlockMetadata(xCoord, yCoord - 1, zCoord);
					float[] color = EntitySheep.fleeceColorTable[fmeta];

					if(worldObj.rand.nextInt(4) == 0)
						Botania.proxy.sparkleFX(worldObj, centerBlock.x + (Math.random() - 0.5) * 0.5, centerBlock.y, centerBlock.z + (Math.random() - 0.5) * 0.5, color[0], color[1], color[2], (float) Math.random(), 8);

					Botania.proxy.wispFX(worldObj, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.25, pos.getY() - 0.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, color[0], color[1], color[2], (float) Math.random() / 3F, -0.04F);
					Botania.proxy.wispFX(worldObj, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.125, pos.getY() + 1.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.125, color[0], color[1], color[2], (float) Math.random() / 5F, -0.001F);
					Botania.proxy.wispFX(worldObj, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.25, pos.getY() + 1.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, color[0], color[1], color[2], (float) Math.random() / 8F, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			}
		}

		if(worldObj.rand.nextBoolean() && worldObj.isRemote)
			Botania.proxy.sparkleFX(worldObj, pos.getX() + Math.random(), pos.getY() + Math.random() * 1.5, pos.getZ() + Math.random(), meta == 2 ? 1F : 0.5F, meta == 1 ? 1F : 0.5F, meta == 1 ? 0.5F : 1F, (float) Math.random(), 2);
	}

	private Block getBlockForMeta() {
		return getBlockMetadata() == 0 ? ModBlocks.enchanter : ModBlocks.alfPortal;
	}

}