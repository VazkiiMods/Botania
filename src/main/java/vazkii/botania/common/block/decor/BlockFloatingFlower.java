/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block.decor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import vazkii.botania.api.internal.VanillaPacketDispatcher;
import vazkii.botania.api.item.IFloatingFlower;
import vazkii.botania.api.item.IFloatingFlower.IslandType;
import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.client.fx.SparkleParticleData;
import vazkii.botania.common.block.BlockModWaterloggable;
import vazkii.botania.common.block.tile.TileFloatingFlower;
import vazkii.botania.common.core.handler.ConfigHandler;
import vazkii.botania.common.core.helper.ColorHelper;
import vazkii.botania.common.item.IFloatingFlowerVariant;

import javax.annotation.Nonnull;

import java.util.Random;

public class BlockFloatingFlower extends BlockModWaterloggable implements BlockEntityProvider {

	private static final VoxelShape SHAPE = createCuboidShape(1.6, 1.6, 1.6, 14.4, 14.4, 14.4);
	public final DyeColor color;

	public BlockFloatingFlower(DyeColor color, Settings props) {
		super(props);
		this.color = color;
	}

	@Nonnull
	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext ctx) {
		return SHAPE;
	}

	@Nonnull
	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return ConfigHandler.CLIENT.staticFloaters.getValue() ? BlockRenderType.MODEL : BlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
		int hex = ColorHelper.getColorValue(color);
		int r = (hex & 0xFF0000) >> 16;
		int g = (hex & 0xFF00) >> 8;
		int b = hex & 0xFF;

		if (rand.nextDouble() < ConfigHandler.CLIENT.flowerParticleFrequency.getValue()) {
			SparkleParticleData data = SparkleParticleData.sparkle(rand.nextFloat(), r / 255F, g / 255F, b / 255F, 5);
			world.addParticle(data, pos.getX() + 0.3 + rand.nextFloat() * 0.5, pos.getY() + 0.5 + rand.nextFloat() * 0.5, pos.getZ() + 0.3 + rand.nextFloat() * 0.5, 0, 0, 0);
		}
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ItemStack stack = player.getStackInHand(hand);
		BlockEntity te = world.getBlockEntity(pos);
		if (!stack.isEmpty() && te != null && te.getCapability(TileEntitySpecialFlower.FLOATING_FLOWER_CAP).isPresent()) {
			IFloatingFlower flower = te.getCapability(TileEntitySpecialFlower.FLOATING_FLOWER_CAP).orElseThrow(IllegalStateException::new);
			IslandType type = null;
			if (stack.getItem() == Items.SNOWBALL) {
				type = IslandType.SNOW;
			} else if (stack.getItem() instanceof IFloatingFlowerVariant) {
				IslandType newType = ((IFloatingFlowerVariant) stack.getItem()).getIslandType(stack);
				if (newType != null) {
					type = newType;
				}
			}

			if (type != null && type != flower.getIslandType()) {
				if (!world.isClient) {
					flower.setIslandType(type);
					VanillaPacketDispatcher.dispatchTEToNearbyPlayers(te);
				}

				if (!player.abilities.creativeMode) {
					stack.decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return ActionResult.PASS;
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return new TileFloatingFlower();
	}
}
