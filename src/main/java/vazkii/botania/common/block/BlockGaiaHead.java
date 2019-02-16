/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Botania Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Botania
 *
 * Botania is Open Source and distributed under the
 * Botania License: http://botaniamod.net/license.php
 *
 * File Created @ [Sep 23, 2015, 11:44:35 PM (GMT)]
 */
package vazkii.botania.common.block;

import net.minecraft.block.BlockAbstractSkull;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import vazkii.botania.client.render.IModelRegister;
import vazkii.botania.common.block.tile.TileGaiaHead;
import vazkii.botania.common.item.ModItems;
import vazkii.botania.common.lib.LibBlockNames;
import vazkii.botania.common.lib.LibMisc;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BlockGaiaHead extends BlockAbstractSkull {
	public static final BlockSkull.ISkullType GAIA_TYPE = new BlockSkull.ISkullType() {};

	public BlockGaiaHead(Properties builder) {
		super(GAIA_TYPE, builder);
	}

	@Nonnull
	@Override
	public TileEntity createNewTileEntity(IBlockReader world) {
		return new TileGaiaHead();
	}
}
