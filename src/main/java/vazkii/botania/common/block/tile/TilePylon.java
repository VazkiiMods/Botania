/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import vazkii.botania.api.state.BotaniaStateProps;
import vazkii.botania.api.state.enums.AlfPortalState;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.client.fx.WispParticleData;
import vazkii.botania.common.block.BlockModFlower;
import vazkii.botania.common.block.BlockPylon;
import vazkii.botania.common.block.ModBlocks;
import vazkii.botania.common.block.mana.BlockPool;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ColorHelper;

import java.util.Random;

public class TilePylon extends BlockEntity {
	boolean activated = false;
	BlockPos centerPos;
	private int ticks = 0;

	public TilePylon(BlockPos pos, BlockState state) {
		super(ModTiles.PYLON, pos, state);
	}

	public static void commonTick(Level level, BlockPos worldPosition, BlockState state, TilePylon self) {
		++self.ticks;

		BlockPylon.Variant variant = ((BlockPylon) state.getBlock()).variant;

		if (self.activated && level.isClientSide) {
			if (level.getBlockState(self.centerPos).getBlock() != variant.getTargetBlock()
					|| variant == BlockPylon.Variant.NATURA && (self.portalOff() || !(level.getBlockState(worldPosition.below()).getBlock() instanceof BlockPool))) {
				self.activated = false;
				return;
			}

			Vec3 centerBlock = new Vec3(self.centerPos.getX() + 0.5, self.centerPos.getY() + 0.75 + (Math.random() - 0.5 * 0.25), self.centerPos.getZ() + 0.5);

			if (variant == BlockPylon.Variant.NATURA) {
				if (ConfigHandler.CLIENT.elfPortalParticlesEnabled.getValue()) {
					double worldTime = self.ticks;
					worldTime += new Random(worldPosition.hashCode()).nextInt(1000);
					worldTime /= 5;

					float r = 0.75F + (float) Math.random() * 0.05F;
					double x = worldPosition.getX() + 0.5 + Math.cos(worldTime) * r;
					double z = worldPosition.getZ() + 0.5 + Math.sin(worldTime) * r;

					Vec3 ourCoords = new Vec3(x, worldPosition.getY() + 0.25, z);
					centerBlock = centerBlock.subtract(0, 0.5, 0);
					Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

					WispParticleData data = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F, 1);
					level.addParticle(data, x, worldPosition.getY() + 0.25, z, 0, -(-0.075F - (float) Math.random() * 0.015F), 0);
					if (level.random.nextInt(3) == 0) {
						WispParticleData data1 = WispParticleData.wisp(0.25F + (float) Math.random() * 0.1F, (float) Math.random() * 0.25F, 0.75F + (float) Math.random() * 0.25F, (float) Math.random() * 0.25F);
						level.addParticle(data1, x, worldPosition.getY() + 0.25, z, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
					}
				}
			} else {
				Vec3 ourCoords = Vec3.atCenterOf(worldPosition).add(0, 1 + (Math.random() - 0.5 * 0.25), 0);
				Vec3 movementVector = centerBlock.subtract(ourCoords).normalize().scale(0.2);

				Block block = level.getBlockState(worldPosition.below()).getBlock();
				if (block instanceof BlockModFlower) {
					int hex = ColorHelper.getColorValue(((BlockModFlower) block).color);
					int r = (hex & 0xFF0000) >> 16;
					int g = (hex & 0xFF00) >> 8;
					int b = hex & 0xFF;

					if (level.random.nextInt(4) == 0) {
						SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), r / 255F, g / 255F, b / 255F, 8);
						level.addParticle(data, centerBlock.x + (Math.random() - 0.5) * 0.5, centerBlock.y, centerBlock.z + (Math.random() - 0.5) * 0.5, 0, 0, 0);
					}

					WispParticleData data1 = WispParticleData.wisp((float) Math.random() / 3F, r / 255F, g / 255F, b / 255F, 1);
					level.addParticle(data1, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.25, worldPosition.getY() - 0.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, 0, - -0.04F, 0);
					WispParticleData data = WispParticleData.wisp((float) Math.random() / 5F, r / 255F, g / 255F, b / 255F, 1);
					level.addParticle(data, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.125, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.125, 0, - -0.001F, 0);
					WispParticleData data2 = WispParticleData.wisp((float) Math.random() / 8F, r / 255F, g / 255F, b / 255F);
					level.addParticle(data2, worldPosition.getX() + 0.5 + (Math.random() - 0.5) * 0.25, worldPosition.getY() + 1.5, worldPosition.getZ() + 0.5 + (Math.random() - 0.5) * 0.25, (float) movementVector.x, (float) movementVector.y, (float) movementVector.z);
				}
			}
		}

		if (level.random.nextBoolean() && level.isClientSide) {
			SparkleParticleData data = SparkleParticleData.sparkle((float) Math.random(), variant.r, variant.g, variant.b, 2);
			level.addParticle(data, worldPosition.getX() + Math.random(), worldPosition.getY() + Math.random() * 1.5, worldPosition.getZ() + Math.random(), 0, 0, 0);
		}
	}

	private boolean portalOff() {
		return level.getBlockState(centerPos).getBlock() != ModBlocks.alfPortal
				|| level.getBlockState(centerPos).getValue(BotaniaStateProps.ALFPORTAL_STATE) == AlfPortalState.OFF;
	}

}
