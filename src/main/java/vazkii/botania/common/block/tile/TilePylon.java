/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.block.Block;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.Vector3;

import java.util.Random;

public class TilePylon extends TileEntity implements ITickableTileEntity {
	boolean activated = false;
	BlockPos centerPos;
	private int ticks = 0;

	public TilePylon() {
		super(ModTiles.PYLON);
	}

	@Override
	public void tick() {
		++ticks;

		if (!(getBlockState().getBlock() instanceof BlockPylon)) {
			return;
		}

		BlockPylon.Variant variant = ((BlockPylon) getBlockState().getBlock()).variant;

		if (activated && world.isRemote) {
			if (world.getBlockState(centerPos).getBlock() != getBlockForMeta()
					|| variant == BlockPylon.Variant.NATURA && (portalOff() || !(world.getBlockState(getPos().down()).getBlock() instanceof BlockPool))) {
				activated = false;
				return;
			}

			Vector3d centerBlock = new Vector3d(centerPos.getX() + 0.5, centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), centerPos.getZ() + 0.5);

			if (variant == BlockPylon.Variant.NATURA) {
				if (ConfigHandler.CLIENT.elfPortalParticlesEnabled.get()) {
					double worldTime = ticks;
					worldTime += new Random(pos.hashCode()).nextInt(1000);
					worldTime /= 5;

					float r = 0.75F + (float) Math.random() * 0.05F;
					double x = pos.getX() + 0.5 + Math.cos(worldTime) * r;
					double z = pos.getZ() + 0.5 + Math.sin(worldTime) * r;

					Vector3d ourCoords = new Vector3d(x, pos.getY() + 0.25, z);
					centerBlock = centerBlock.subtract(0, 0.5, 0);
					Vector3d movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

					WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 1);
					world.addParticle(data, x, pos.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
					if (world.rand.nextInt(3) == 0) {
						WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
						world.addParticle(data1, x, pos.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
					}
				}
			} else {
				Vector3d ourCoords = Vector3d.func_237489_a_(getPos()).add(0, 1 + (Math.random() - 0.5 * 0.25), 0);
				Vector3d movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

				Block block = world.getBlockState(pos.down()).getBlock();
				if (block instanceof BlockModFlower) {
					int hex = ((BlockModFlower) block).color.getColorValue();
					int r = (hex & 0xFF0000) >> 16;
					int g = (hex & 0xFF00) >> 8;
					int b = hex & 0xFF;

					if (world.rand.nextInt(4) == 0) {
						SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), r / 255F, g / 255F, b / 255F, 8);
						world.addParticle(data, centerBlock.x + (Math.random() - 0.5) * 0.5, centerBlock.y, centerBlock.z + (Math.random() - 0.5) * 0.5, 0, 0, 0);
					}

					WispParticleData data1 = WispParticleData.wisp((float) Math.random() / 3F, r / 255F, g / 255F, b / 255F, 1);
					world.addParticle(data1, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.25, pos.getY() - 0.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, 0, - -0.04F, 0);
					WispParticleData data = WispParticleData.wisp((float) Math.random() / 5F, r / 255F, g / 255F, b / 255F, 1);
					world.addParticle(data, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.125, pos.getY() + 1.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.125, 0, - -0.001F, 0);
					WispParticleData data2 = WispParticleData.wisp((float) Math.random() / 8F, r / 255F, g / 255F, b / 255F);
					world.addParticle(data2, pos.getX() + 0.5 + (Math.random() - 0.5) * 0.25, pos.getY() + 1.5, pos.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			}
		}

		if (world.rand.nextBoolean() && world.isRemote) {
			float r = variant == BlockPylon.Variant.GAIA ? 1F : 0.5F;
			float g = variant == BlockPylon.Variant.NATURA ? 1F : 0.5F;
			float b = variant == BlockPylon.Variant.NATURA ? 0.5F : 1F;
			SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), r, g, b, 2);
			world.addParticle(data, pos.getX() + Math.random(), pos.getY() + Math.random() * 1.5, pos.getZ() + Math.random(), 0, 0, 0);
		}
	}

	private Block getBlockForMeta() {
		return ((BlockPylon) getBlockState().getBlock()).variant == BlockPylon.Variant.MANA ? ModBlocks.enchanter : ModBlocks.alfPortal;
	}

	private boolean portalOff() {
		return world.getBlockState(centerPos).getBlock() != ModBlocks.alfPortal
				|| world.getBlockState(centerPos).get(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.OFF;
	}

}
