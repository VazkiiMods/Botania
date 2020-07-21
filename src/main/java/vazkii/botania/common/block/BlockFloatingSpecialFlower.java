/*
 * This class is distributed as part of the Botania Mod.
 * Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 */
package vazkii.botania.common.block;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
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

	public BlockFloatingSpecialFlower(Properties props, Supplier<? extends TileEntitySpecialFlower> teProvider) {
		super(DyeColor.WHITE, props);
		this.teProvider = teProvider;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void animateTick(BlockState state, World world, BlockPos pos, Random rand) {}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		return ((TileEntitySpecialFlower) world.getTileEntity(pos)).onWanded(player, stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity entity, ItemStack stack) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).onBlockPlacedBy(world, pos, state, entity, stack);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(MatrixStack ms, Minecraft mc, World world, BlockPos pos) {
		((TileEntitySpecialFlower) world.getTileEntity(pos)).renderHUD(ms, mc);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(@Nonnull IBlockReader world) {
		return teProvider.get();
	}
}
