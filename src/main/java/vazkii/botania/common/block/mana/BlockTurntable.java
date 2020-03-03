/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Mar 16, 2014, 10:08:14 PM (GMT)]
 */
package vazkii.botania.common.block.mana;

import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.api.wand.IWandHUD;
import vazkii.botania.api.wand.IWandable;
import vazkii.botania.common.block.BlockMod;
import vazkii.botania.common.block.tile.mana.TileTurntable;

import javax.annotation.Nonnull;

public class BlockTurntable extends BlockMod implements IWandable, IWandHUD {

	public BlockTurntable(Properties builder) {
		super(builder);
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nonnull
	@Override
	public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
		return new TileTurntable();
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderHUD(Minecraft mc, World world, BlockPos pos) {
		((TileTurntable) world.getTileEntity(pos)).renderHUD(mc);
	}

	@Override
	public boolean onUsedByWand(PlayerEntity player, ItemStack stack, World world, BlockPos pos, Direction side) {
		((TileTurntable) world.getTileEntity(pos)).onWanded(player, stack, side);
		return true;
	}

}
