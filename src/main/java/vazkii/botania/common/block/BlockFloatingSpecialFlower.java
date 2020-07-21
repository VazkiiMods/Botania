/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import vazkii.botania.api.subtile.TileEntitySpecialFlower;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.decor.BlockFloatingFlower;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class BlockFloatingSpecialFlower extends BlockFloatingFlower implements IWandable, IWandHUD {
	private final Supplier<? extends TileEntitySpecialFlower> teProvider;

	public BlockFloatingSpecialFlower(Settings props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(DyeColor.WHITE, props);
		this.teProvider = teProvider;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getBlockEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, MinecraftClient mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getBlockEntity(pos)).renderHUD(ms, mc);
	}

	@Nonnull
	@Override
	public BlockEntity createBlockEntity(@Nonnull BlockView world) {
		return teProvider.get();
	}
}
